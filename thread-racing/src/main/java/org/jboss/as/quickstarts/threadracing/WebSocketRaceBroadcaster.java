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

import org.jboss.as.quickstarts.threadracing.results.ChampionshipStandings;
import org.jboss.as.quickstarts.threadracing.results.RaceResult;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;

/**
 * A {@link org.jboss.as.quickstarts.threadracing.RaceBroadcaster} that sends html/text messages through a Web Socket {@link javax.websocket.Session}.
 *
 * @author Eduardo Martins
 */
public class WebSocketRaceBroadcaster implements RaceBroadcaster {

    /**
     * the web socket's session where messages are sent.
     */
    private final Session session;

    /**
     * Creates a new web socket broadcaster
     * @param session the session where race progress messages will be sent.
     */
    public WebSocketRaceBroadcaster(Session session) {
        this.session = session;
    }

    /**
     * Sends a message to the web socket client.
     * @param message
     */
    private void sendToClient(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pauses the broadcast for the specified amount of milliseconds.
     * @param millis
     */
    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        sendToClient("Wow, what a crowd today at Threadianopolis... ");
        pause(2000);
        sendToClient("There must be more than a GIGABYTE of thread racing fans going nuts.<br/><br/>");
        pause(3000);
    }

    @Override
    public void startYourEngines() {
        pause(1000);
        sendToClient("<br/>Racers, ");
        pause(2000);
        sendToClient("start ");
        pause(1000);
        sendToClient("your ");
        pause(1000);
        sendToClient("THREADS.<br/>");
    }

    @Override
    public void readySetGo() {
        pause(2000);
        sendToClient("<br/>Ready... ");
        pause(2000);
        sendToClient("Set... ");
        pause(2000);
        sendToClient("Go! <br/><br/>");
    }

    @Override
    public void raceProgress(String msg) {
        sendToClient(msg + "<br/>");
    }

    @Override
    public void raceEnd() {
        sendToClient("<br/>Please await() the official results ");
        pause(2000);
        sendToClient(":");
        pause(2000);
        sendToClient("-");
        pause(2000);
        sendToClient(")<br/>");
    }

    @Override
    public void raceResult(RaceResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Official Race Results</h2>");
        sb.append("<ol>");
        for (String racer : result.getSortedRacers()) {
            sb.append("<li>" + racer + "</li>");
        }
        sb.append("</ol>");
        sendToClient(sb.toString());
    }

    @Override
    public void championshipStandings(List<ChampionshipStandings.Entry> entries) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Championship Standings</h2>");
        sb.append("<ol>");
        for (ChampionshipStandings.Entry entry : entries) {
            sb.append("<li>" + entry.getName() + ", " + entry.getPoints() + " points</li>");
        }
        sb.append("</ol>");
        sendToClient(sb.toString());
    }
}
