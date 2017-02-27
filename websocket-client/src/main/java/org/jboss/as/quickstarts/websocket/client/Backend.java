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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.net.ssl.SSLContext;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 * @author <a href="http://monospacesoftware.com">Paul Cowan</a>
 */
// This class is the WebSocket client
@ApplicationScoped
public class Backend extends Endpoint implements MessageHandler.Whole<SessionMessage> {

    private Logger log = Logger.getLogger(Backend.class.getName());

    private String echoServerUrl = "wss://echo.websocket.org";

    @Inject
    @ToFrontend
    private Event<SessionMessage> frontendTrigger;

    @Inject
    @ToBroadcast
    private Event<String> broadcastTrigger;

    private ClientEndpointConfig config;
    private Session session;

    private ConnectionGate connectionGate = new ConnectionGate();

    @PostConstruct
    public void init() {
        List<Class<? extends Encoder>> encoders = new ArrayList<>();
        encoders.add(SessionMessageEncoder.class);

        List<Class<? extends Decoder>> decoders = new ArrayList<>();
        decoders.add(SessionMessageDecoder.class);

        config = ClientEndpointConfig.Builder.create()
                // .encoders(Arrays.asList(SessionMessageEncoder.class)) // too bad this doesn't work...
                .encoders(encoders)
                .decoders(decoders)
                .build();

        try {
            config.getUserProperties().put("io.undertow.websocket.SSL_CONTEXT", SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, format("Failed to deploy backend endpoint: %s", e.getMessage()), e);
        }

        // To use secure WebSocket (wss://) we must supply an SSLContext via the ClientEndpointConfig
        // Unfortunately there is no API standard way to do this, so it's being set here via a user property which is Undertow
        // specific.
        // Additionally, there is no connectToServer method that accepts an annotated Object and a ClientEndpointConfig
        // Therefore we're forced to use the method that accepts an Endpoint instance, so we're forced to implement Endpoint,
        // making annotations irrelevant.

        connect();
    }

    public void connect() {
        broadcastTrigger.fire(format("Connecting to backend %s", echoServerUrl));
        try {
            ContainerProvider.getWebSocketContainer().connectToServer(this, config, URI.create(echoServerUrl));
        } catch (Exception e) {
            broadcastTrigger.fire(format("Failed to connect to backend %s: %s", echoServerUrl, e.getMessage()));
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        session.addMessageHandler(SessionMessage.class, this);
        broadcastTrigger.fire(format("Opened backend session %s", session.getId()));
        connectionGate.connected();
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        connectionGate.disconnected();
        broadcastTrigger.fire(format("Closed backend session %s due to %s", session.getId(), closeReason.getReasonPhrase()));
        connect(); // reconnect; probably should do this asynchronously and add an exponential backoff
    }

    @Override
    public void onError(Session session, Throwable t) {
        broadcastTrigger.fire(format("Error from backend session %s: %s", session.getId(), t.getMessage()));
        // reconnect?
    }

    @Override
    public void onMessage(SessionMessage message) {
        frontendTrigger
                .fire(new SessionMessage(message.getSessionId(), format("Received message from backend session %s to frontend session %s ", session.getId(), message.getSessionId())));
        frontendTrigger.fire(message);
    }

    public void sendMessage(@Observes @ToBackend SessionMessage message) throws Exception {
        connectionGate.waitForConnection(5, TimeUnit.SECONDS);
        if (!connectionGate.isConnected()) {
            frontendTrigger
                    .fire(new SessionMessage(message.getSessionId(), format("Failed to send frontend session %s message: not connected to backend", message.getSessionId())));
            return;
        }

        frontendTrigger
                .fire(new SessionMessage(message.getSessionId(), format("Sending message from frontend session %s to backend session %s", message.getSessionId(), session.getId())));
        session.getBasicRemote().sendObject(message);
    }

    // JSON is overkill for this but it's good as an example
    public static class SessionMessageEncoder implements Encoder.TextStream<SessionMessage> {

        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public void destroy() {
        }

        @Override
        public void encode(SessionMessage object, Writer writer) throws EncodeException, IOException {
            JsonObject jsonObject = Json.createObjectBuilder()
                    .add("sessionId", object.getSessionId())
                    .add("txt", object.getText())
                    .build();
            Json.createWriter(writer).writeObject(jsonObject);
        }

    }

    // JSON is overkill for this but it's good as an example
    public static class SessionMessageDecoder implements Decoder.TextStream<SessionMessage> {

        @Override
        public void init(EndpointConfig config) {
        }

        @Override
        public void destroy() {
        }

        @Override
        public SessionMessage decode(Reader reader) throws DecodeException, IOException {
            JsonObject jsonObject = Json.createReader(reader).readObject();
            return new SessionMessage(jsonObject.getString("sessionId"), jsonObject.getString("txt"));
        }

    }

}
