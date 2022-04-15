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
package org.jboss.as.quickstarts.numberguess;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.logging.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoteNumberGuessIT {

    private java.util.Random random = new java.util.Random(System.currentTimeMillis());
    private static final Logger log = Logger.getLogger(RemoteNumberGuessIT.class.getName());

    protected URI getHTTPEndpoint(String path) {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/numberguess";
        }
        try {
            return new URI(host + path);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testGuessNumber() throws Exception {
        guessNumber();
    }

    public void guessNumber() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .build();
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint("/home.jsf"))
                .POST(ofFormData(Map.of("numberGuess:inputGuess", (Object) getNumber())))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().toString().contains("<form id=\"numberGuess\" name=\"numberGuess\" method=\"post\""));
    }

    private int getNumber() {
        return random.nextInt(99) + 1;
    }

    public static BodyPublisher ofFormData(Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder
                    .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder
                    .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return BodyPublishers.ofString(builder.toString());
    }

}
