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
package org.jboss.quickstarts.websocket_endpoint;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 * @author istudens
 */
public class BidWebSocketEndpointIT {

    protected URI getWebSocketEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "ws://localhost:8080/websocket-endpoint";
        }
        try {
            return new URI(host + "/bidsocket");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        if (host != null) {
            host = host.replaceFirst("http", "ws");
        }
        return host;
    }

    @Test
    public void testConnectOk() throws Exception {
        testWebSocket(2000);
    }

    public void testWebSocket(long wait) throws IOException, InterruptedException {
        EchoListener listener = new EchoListener();
        WebSocket webSocket = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(getWebSocketEndpoint(), listener)
                .join();

        Assertions.assertTrue(listener.isConnected(), "Connection should be opened");

        sendWebSocketMessage(webSocket, "newBid", 10);
        Thread.sleep(wait);
        JsonObject bidMessage = jsonFromString(listener.getMessage());
        Assertions.assertEquals("STARTED", bidMessage.getString("bidStatus"));
        Assertions.assertEquals(110, bidMessage.getInt("currentPrice"));

        sendWebSocketMessage(webSocket, "buyItNow");
        Thread.sleep(wait);
        JsonObject buyItNowMessage = jsonFromString(listener.getMessage());
        Assertions.assertEquals("SOLD", buyItNowMessage.getString("bidStatus"));

        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Done");
        Thread.sleep(wait);
        Assertions.assertTrue(webSocket.isOutputClosed(), "Connection should be closed");
        Assertions.assertFalse(listener.isConnected(), "Connection should be closed");
        webSocket.abort();
        Assertions.assertTrue(webSocket.isInputClosed(), "Connection should be closed");
    }

    private void sendWebSocketMessage(WebSocket webSocket, String command) {
        sendWebSocketMessage(webSocket, command, null);
    }

    private void sendWebSocketMessage(WebSocket webSocket, String command, Integer bidValue) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                .add("command", command);
        if (bidValue != null) {
            jsonBuilder.add("bidValue", bidValue);
        }
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(jsonBuilder.build());
        jsonWriter.close();
        webSocket.sendText(stWriter.toString(), true);
    }

    private static JsonObject jsonFromString(String jsonMessage) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonMessage));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }
}
