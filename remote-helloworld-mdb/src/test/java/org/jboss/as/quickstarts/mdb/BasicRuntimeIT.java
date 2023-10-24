/*
 * Copyright 2022 JBoss by Red Hat.
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
package org.jboss.as.quickstarts.mdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2023 Red Hat, Inc.
 */
public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/remote-helloworld-mdb";

    @Test
    public void testSendToQueue() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getHTTPEndpoint())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Optional<String> contentType = response.headers().firstValue("Content-Type");
        assertTrue(contentType.isPresent());
        assertEquals("text/html;charset=ISO-8859-1", contentType.get());
        String[] content = response.body().split(getLineSeparator());
        assertEquals(9, content.length);
        assertEquals("<h1>Quickstart: Example demonstrates the use of <strong>Jakarta Messaging 3.1</strong> and <strong>Jakarta Enterprise Beans 4.0 Message-Driven Bean</strong> in a JakartaEE server.</h1>", content[0].trim());
        assertEquals("<p>Sending messages to <em>ActiveMQQueue[HelloWorldMDBQueue]</em></p>", content[1].trim());
        assertEquals("<h2>The following messages will be sent to the destination:</h2>", content[2].trim());
        assertEquals("Message (0): This is message 1<br/>", content[3].trim());
        assertEquals("Message (1): This is message 2<br/>", content[4].trim());
        assertEquals("Message (2): This is message 3<br/>", content[5].trim());
        assertEquals("Message (3): This is message 4<br/>", content[6].trim());
        assertEquals("Message (4): This is message 5<br/>", content[7].trim());
        assertEquals("<p><i>Go to your JakartaEE server console or server log to see the result of messages processing.</i></p>", content[8].trim());
    }

    @Test
    public void testSendToTopic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getHTTPEndpoint().toString() + "?topic"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Optional<String> contentType = response.headers().firstValue("Content-Type");
        assertTrue(contentType.isPresent());
        assertEquals("text/html;charset=ISO-8859-1", contentType.get());
        String[] content = response.body().split(getLineSeparator());
        assertEquals(9, content.length);
        assertEquals("<h1>Quickstart: Example demonstrates the use of <strong>Jakarta Messaging 3.1</strong> and <strong>Jakarta Enterprise Beans 4.0 Message-Driven Bean</strong> in a JakartaEE server.</h1>", content[0].trim());
        assertEquals("<p>Sending messages to <em>ActiveMQTopic[HelloWorldMDBTopic]</em></p>", content[1].trim());
        assertEquals("<h2>The following messages will be sent to the destination:</h2>", content[2].trim());
        assertEquals("Message (0): This is message 1<br/>", content[3].trim());
        assertEquals("Message (1): This is message 2<br/>", content[4].trim());
        assertEquals("Message (2): This is message 3<br/>", content[5].trim());
        assertEquals("Message (3): This is message 4<br/>", content[6].trim());
        assertEquals("Message (4): This is message 5<br/>", content[7].trim());
        assertEquals("<p><i>Go to your JakartaEE server console or server log to see the result of messages processing.</i></p>", content[8].trim());
    }

    protected String getLineSeparator() {
        return "\n";
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host", DEFAULT_SERVER_HOST);
        }
        return host;
    }

    protected URI getHTTPEndpoint() {
        try {
            return new URI(getServerHost() + "/HelloWorldMDBServletClient");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
}
