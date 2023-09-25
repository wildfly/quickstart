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

import org.junit.Test;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The thread racing runtime integration testing.
 * @author Emmanuel Hugonnet
 * @author emartins
 */
public class ThreadRacingIT {

    private static final Set<String> messages = new LinkedHashSet<>();
    private volatile boolean notFinished = true;

    protected URI getWebSocketEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "ws://localhost:8080/thread-racing";
        }
        try {
            return new URI(host + "/race");
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
    public void testRace() throws Exception {
        race();
    }

    public void race() throws IOException, InterruptedException {
        System.out.println("Endpoint " + getWebSocketEndpoint());
        EchoListener listener = new EchoListener(this);
        WebSocket webSocket = HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(getWebSocketEndpoint(), listener)
                .join();
        assertTrue("Connection should be opened", listener.isConnected());
        while (notFinished) {
            webSocket.request(1);
        }
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Done");
        webSocket.request(1);
        assertTrue("Connection should be closed", webSocket.isOutputClosed());
        assertFalse("Connection should be closed", listener.isConnected());
        webSocket.abort();
        assertTrue("Connection should be closed", webSocket.isInputClosed());
        String[] result = messages.toArray(new String[0]);
        assertEquals("<br/>Please await() the official results ", result[messages.size() - 6]);
    }

    public void stop() {
        notFinished = false;
    }

    public static void addMessage(String message) {
        messages.add(message);
    }

}
