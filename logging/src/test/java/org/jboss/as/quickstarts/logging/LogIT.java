/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.quickstarts.logging;

import jakarta.json.JsonArray;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/jboss-logging";

    private static Client client;

    @BeforeAll
    public static void createClients() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    public static void closeClients() {
        if (client != null) client.close();
    }

    @Test
    @Order(0)
    public void fatal() {
        doLog("fatal");
        checkLines("quickstart-fatal.log", 1);
    }

    @Test
    @Order(1)
    public void error() {
        doLog("error");
        checkLines("quickstart-error.log", 2);
        checkLines("quickstart-fatal.log", 1);
    }

    @Test
    @Order(2)
    public void warn() {
        doLog("warn");
        checkLines("quickstart-warn.log", 3);
        checkLines("quickstart-error.log", 2);
        checkLines("quickstart-fatal.log", 1);
    }

    @Test
    @Order(3)
    public void info() {
        doLog("info");
        checkLines("quickstart-info.log", 4);
        checkLines("quickstart-warn.log", 3);
        checkLines("quickstart-error.log", 2);
        checkLines("quickstart-fatal.log", 1);
    }

    @Test
    @Order(4)
    public void debug() {
        doLog("debug");
        checkLines("quickstart-debug.log", 5);
        checkLines("quickstart-info.log", 4);
        checkLines("quickstart-warn.log", 3);
        checkLines("quickstart-error.log", 2);
        checkLines("quickstart-fatal.log", 1);
    }

    @Test
    @Order(5)
    public void trace() {
        doLog("trace");
        checkLines("quickstart-trace.log", 6);
        checkLines("quickstart-debug.log", 5);
        checkLines("quickstart-info.log", 4);
        checkLines("quickstart-warn.log", 3);
        checkLines("quickstart-error.log", 2);
        checkLines("quickstart-fatal.log", 1);
    }

    private void checkLines(final String logFile, final int expectedLines) {
        final JsonArray lines = readLogFile(logFile);
        Assertions.assertEquals(expectedLines, lines.size(), () -> String.format("Expected %d entry got %d: %s", expectedLines, lines.size(), lines));
        // We make some assumptions here based on the number of lines, we assume the lines are in order and contain a specific level in the message
        switch (expectedLines) {
            case 6:
                Assertions.assertTrue(lines.getString(5).contains("TRACE"));
            case 5:
                Assertions.assertTrue(lines.getString(4).contains("DEBUG"));
            case 4:
                Assertions.assertTrue(lines.getString(3).contains("INFO"));
            case 3:
                Assertions.assertTrue(lines.getString(2).contains("WARN"));
            case 2:
                Assertions.assertTrue(lines.getString(1).contains("ERROR"));
            case 1:
                Assertions.assertTrue(lines.getString(0).contains("FATAL"));
                break;
            default:
                Assertions.fail("No logs found in " + logFile);
        }
    }

    private JsonArray readLogFile(final String logFile) {
        final UriBuilder uriBuilder = resolveBaseUrl().path("read").path(logFile);
        try (
                Response response = client.target(uriBuilder).request().get()
        ) {
            final Response.Status expectedStatus = Response.Status.OK;
            Assertions.assertEquals(expectedStatus, response.getStatusInfo(), () ->
                    String.format("Expected status %s for resource %s got \"%d: %s\": %s", expectedStatus, uriBuilder.build(),
                            response.getStatus(), response.getStatusInfo(), response.readEntity(String.class)));
            return response.readEntity(JsonArray.class);
        }
    }

    private static void doLog(final String level) {
        final Response.Status expectedStatus = Response.Status.OK;
        final UriBuilder uriBuilder = resolveBaseUrl().path(level);
        try (
                Response response = client.target(uriBuilder).request().post(Entity.text(""))
        ) {
            Assertions.assertEquals(expectedStatus, response.getStatusInfo(), () ->
                    String.format("Expected status %s for resource %s got \"%d: %s\": %s", expectedStatus, uriBuilder.build(),
                            response.getStatus(), response.getStatusInfo(), response.readEntity(String.class)));
        }
    }

    private static UriBuilder resolveBaseUrl() {
        final String baseUrl = resolveServerHost();
        return UriBuilder.fromUri(baseUrl).path("api/logs");
    }

    private static String resolveServerHost() {
        final String result = System.getenv("SERVER_HOST");
        return result != null ? result : System.getProperty("server.host", DEFAULT_SERVER_HOST);
    }
}
