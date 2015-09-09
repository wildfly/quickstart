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
package org.jboss.quickstarts.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.quickstarts.websocket.model.Bid;
import org.jboss.quickstarts.websocket.model.Bidding;
import org.jboss.quickstarts.websocket.model.BiddingFactory;

/**
 * WebSocket server endpoint example
 * <p/>
 *
 * This class produces a Websocket endpoint to receive messages from clients.
 *
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
@ServerEndpoint(value = "/bidsocket", encoders = { BiddingEncoder.class }, decoders = { MessageDecoder.class })
public class BidWebSocketEndpoint {

    private Logger logger = Logger.getLogger(getClass().getName());

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    private Runnable intervalNotifier;

    // It starts a Thread that notifies all sessions each second
    @PostConstruct
    public void startIntervalNotifier() {
        logger.info("Starting interval notifier");
        intervalNotifier = new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        notifyAllSessions(BiddingFactory.getBidding());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(intervalNotifier).start();
    }

    // store the session once that it's opened
    @OnOpen
    public void onOpen(Session session) {
        logger.info("New websocket session opened: " + session.getId());
        clients.add(session);

    }

    // remove the session after it's closed
    @OnClose
    public void onClose(Session session) {
        logger.info("Websoket session closed: " + session.getId());
        clients.remove(session);
    }

    // This method receives a Message that contains a command
    // The Message object is "decoded" by the MessageDecoder class
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        if (message.getCommand().equals("newBid")) {
            Bidding bidding = BiddingFactory.getBidding();
            bidding.addBid(new Bid(session.getId(), message.getBidValue()));
        }
        if (message.getCommand().equals("buyItNow")) {
            Bidding bidding = BiddingFactory.getBidding();
            bidding.buyItNow();
        }
        if (message.getCommand().equals("resetBid")) {
            BiddingFactory.resetBidding();
        }
        notifyAllSessions(BiddingFactory.getBidding());
    }

    // Exception handling
    @OnError
    public void error(Session session, Throwable t) {
        t.printStackTrace();
    }

    // This method sends the same Bidding object to all opened sessions
    private void notifyAllSessions(Bidding bidding) throws EncodeException, IOException {
        for (Session s : clients) {
            s.getBasicRemote().sendObject(bidding);
        }

    }
}
