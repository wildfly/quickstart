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
package org.jboss.as.quickstarts.threadracing;

import org.jboss.as.quickstarts.threadracing.legends.JimmieThronson;
import org.jboss.as.quickstarts.threadracing.legends.MichaelThrumacher;
import org.jboss.as.quickstarts.threadracing.legends.SebastienThroeb;
import org.jboss.as.quickstarts.threadracing.legends.ValentinoThrossi;
import org.jboss.as.quickstarts.threadracing.results.RaceResults;

import javax.inject.Inject;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The app's entry point, a Web Socket {@link javax.websocket.server.ServerEndpoint}, which runs a race for each client connection established.
 *
 * The server endpoint will update the client of the race progress and results, through text messages, and will close the session once the race ends.
 *
 * @author Eduardo Martins
 */
@javax.websocket.server.ServerEndpoint(value = WebSocketRace.PATH, configurator = WebSocketRace.ServerEndpointConfigurator.class)
public class WebSocketRace {

    public static final String PATH = "/race";

    /**
     * CDI injection of racer #1
     */
    @Inject
    private JimmieThronson racer1;

    /**
     * CDI injection of racer #2
     */
    @Inject
    private MichaelThrumacher racer2;

    /**
     * CDI injection of racer #3
     */
    @Inject
    private SebastienThroeb racer3;

    /**
     * CDI injection of racer #4
     */
    @Inject
    private ValentinoThrossi racer4;

    /**
     * CDI injection of all race results
     */
    @Inject
    private RaceResults raceResults;

    /**
     * Session opened handler, creates and starts a race.
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            new Race(racer1, racer2, racer3, racer4, buildRaceEnvironment(session), new WebSocketRaceBroadcaster(session), raceResults).run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Builds the race's environment, from the specified session.
     * @param session
     * @return
     */
    private Map<String, String> buildRaceEnvironment(Session session) {
        final Map<String, String> environment = new HashMap<>();
        final String host = (String) session.getUserProperties().get(ServerEndpointConfigurator.HOST_USER_PROP);
        if (host != null) {
            final String[] hostSplit = host.split(":");
            environment.put(EnvironmentProperties.SERVER_NAME, hostSplit[0]);
            environment.put(EnvironmentProperties.SERVER_PORT, (hostSplit.length > 1 ? hostSplit[1] : "80"));
        }
        final String requestURI = session.getRequestURI().toString();
        final String rootPath = requestURI.equals(PATH) ? "" : requestURI.substring(0, (requestURI.length() - PATH.length()));
        environment.put(EnvironmentProperties.ROOT_PATH, rootPath);
        return environment;
    }

    /**
     * This configurator will capture the environment properties, when handshaking a client.
     */
    public static class ServerEndpointConfigurator extends ServerEndpointConfig.Configurator {
        static final String HOST_USER_PROP = "Host";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            sec.getUserProperties().put(HOST_USER_PROP, request.getHeaders().get(HOST_USER_PROP).get(0));
        }
    }
}
