/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.wildfly.quickstart.microprofile.openapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.junit.Test;

/**
 * Tests that the /openapi context is available.
 * This test would thus correctly fail (404) if run against a server without an OpenAPI subsystem.
 *
 * @author Radoslav Husar
 */
public class OpenAPIContextIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    @Test
    public void testOpenAPIContextIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverHost + "/openapi"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("/openapi context is not available", 200, response.statusCode());

        // First line are just dashes, so lets check the second line with OpenAPI version key for the prefix
        String[] bodyLines = response.body().split("\n");
        assertTrue(bodyLines[1].startsWith("openapi:"));
    }

}
