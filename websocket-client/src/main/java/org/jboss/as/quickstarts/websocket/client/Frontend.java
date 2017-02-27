/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.websocket.client;

import static java.lang.String.format;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */

@ApplicationScoped
public class Frontend extends Endpoint {

    private Logger log = Logger.getLogger(Frontend.class.getName());

    public static final String WEBSOCKET_PATH = "/relay";

    private SessionManager sessionManager = new SessionManager();

    @Inject
    @ToBackend
    private Event<SessionMessage> backendTrigger;

    public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext servletContext) {
        if (servletContext == null) {
            log.severe(format("Failed to deploy frontend endpoint %s: %s", WEBSOCKET_PATH, "ServletContext not available"));
            return;
        }

        ServerContainer serverContainer = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");
        if (serverContainer == null) {
            log.severe(format("Failed to deploy frontend endpoint %s: %s", WEBSOCKET_PATH, "javax.websocket.server.ServerContainer ServerContainer not available"));
            return;
        }

        // WebSocket does not honor CDI contexts in the default configuration; by default a new object is created for each new
        // websocket.
        // We can work around this by supplying a custom ServerEndpointConfig.Configurator that returns the same instance of the
        // endpoint for each new websocket. Unfortunately this precludes us from using annotations, and forces us to extend the
        // abstract class Endpoint, and forces us to add the server endpoint programmatically upon startup of the servlet
        // context.

        ServerEndpointConfig config = ServerEndpointConfig.Builder
                .create(Frontend.class, WEBSOCKET_PATH)
                .configurator(new ServerEndpointConfig.Configurator() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T> T getEndpointInstance(final Class<T> endpointClass) throws InstantiationException {
                        if (endpointClass.isAssignableFrom(Frontend.class)) { return (T) Frontend.this; }
                        return super.getEndpointInstance(endpointClass);
                    }
                })
                .build();

        try {
            serverContainer.addEndpoint(config);
        } catch (DeploymentException e) {
            log.log(Level.SEVERE, format("Failed to deploy frontend endpoint %s: %s", WEBSOCKET_PATH, e.getMessage()), e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        // Endpoint does not handle onMessage; messages are handled by a subclass of MessageHandler.
        // Unfortunately onMessage does not pass the Session as a parameter (although it can be injected when using
        // annotations).
        // Therefore we do not know which session the message came from, which precludes us from using a single instance
        // of MessageHandler to handle all session.
        session.addMessageHandler(String.class, new FrontendMessageHandler(session));
        sessionManager.open(session);
        sendMessage(session, format("Opened frontend session %s", session.getId()));
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        log.info(format("Closed frontend session %s", session.getId()));
        sessionManager.close(session.getId());
    }

    @Override
    public void onError(Session session, Throwable thr) {
        log.log(Level.WARNING, format("Error from frontend session %s", session.getId()), thr);
    }

    public void sendMessage(@Observes @ToFrontend SessionMessage message) {
        Session session = sessionManager.get(message.getSessionId());
        if (session == null) {
            log.warning(format("Frontend session %s not found", message.getSessionId()));
            return;
        }

        sendMessage(session, message.getText());
    }

    public void sendBroadcast(@Observes @ToBroadcast String text) {
        log.info(format("BROADCAST: %s", text));
        for (Session session : sessionManager.getAll()) {
            sendMessage(session, format("BROADCAST: %s", text));
        }
    }

    public void sendMessage(Session session, String text) {
        session.getAsyncRemote().sendText(text);
    }

    protected class FrontendMessageHandler implements MessageHandler.Whole<String> {

        private Session session;

        protected FrontendMessageHandler(Session session) {
            this.session = session;
        }

        protected Session getSession() {
            return session;
        }

        @Override
        public void onMessage(String text) {
            backendTrigger.fire(new SessionMessage(session.getId(), text));
        }
    }

}
