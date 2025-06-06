package org.jboss.as.quickstarts.ear.controller;

/*
 * Copyright 2023 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.Assert.assertEquals;

/**
 * The very basic runtime integration testing.
 * @author emartins
 */
public class BasicRuntimeIT {
    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = System.getProperty("server.host");
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverHost+"/ejb-throws-exception-web"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final String[] bodyLines = response.body().lines().toArray(String[]::new);
        assertEquals("<meta http-equiv=\"Refresh\" content=\"0; URL=index.jsf\">", bodyLines[bodyLines.length-3].strip());
    }
}