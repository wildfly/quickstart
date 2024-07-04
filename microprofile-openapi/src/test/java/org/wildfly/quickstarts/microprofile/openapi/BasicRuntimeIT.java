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
package org.wildfly.quickstarts.microprofile.openapi;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The very basic runtime integration test to verify that /openapi context is registered by the OpenAPI subsystem.
 *
 * @author emartins
 * @author Radoslav Husar
 */
public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        final HttpRequest request = HttpRequest.newBuilder()
                // Check the /openapi context being available (rather than checking dummy /microprofile-openapi response)
                // This test would thus correctly fail (404) if run against a server without an OpenAPI subsystem
                .uri(new URI(serverHost + "/openapi"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // First line are just dashes, so lets check the second line with OpenAPI version key for the prefix
        String[] bodyLines = response.body().split("\n");
        assertTrue(bodyLines[1].startsWith("openapi:"));
    }
}