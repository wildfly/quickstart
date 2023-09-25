package org.jboss.as.quickstarts.threadracing;


/*
 * Copyright 2022 Red Hat, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
public class EchoListener implements WebSocket.Listener {

    private final ThreadRacingIT test;

    public EchoListener(ThreadRacingIT test) {
        this.test = test;
    }

    private static final Logger logger = Logger.getLogger(EchoListener.class.getName());
    private boolean connected = false;
    private StringBuilder buffer = new StringBuilder();
    private String message = null;
    CompletableFuture<String> accumulatedMessage = new CompletableFuture<>();

    @Override
    public void onOpen(WebSocket webSocket) {
        logger.info("CONNECTED");
        connected = true;
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        logger.info("Receiving Message -->");
        logger.info(String.format("onText received a data: %s", data));
        buffer.append(data);
        webSocket.request(1);
        if (last) {
            if (buffer.toString() != null && !buffer.toString().equals(message)) {
                message = buffer.toString();
                accumulatedMessage.complete(buffer.toString());
                buffer = new StringBuilder();
                CompletionStage<?> cf = accumulatedMessage.thenAccept(ThreadRacingIT::addMessage);
                accumulatedMessage = new CompletableFuture<>();
                return cf;
            }
        }
        return accumulatedMessage;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        logger.log(Level.SEVERE, "Error!" + webSocket.toString(), error);
        WebSocket.Listener.super.onError(webSocket, error);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        logger.info("CLOSING");
        connected = false;
        logger.info(String.format("Closed with status %d, and reason: %s", statusCode, reason));
        CompletableFuture.completedFuture(reason).thenAccept(s -> test.stop());
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

    public boolean isConnected() {
        return connected;
    }
}
