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
package org.wildfly.quickstarts.security_domain_to_domain_web;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * The very basic runtime integration testing.
 * @author Prarthona Paul
 * @author emartins
 */
public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/security-domain-to-domain";

    protected URI getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = DEFAULT_SERVER_HOST;
        }
        try {
            return new URI(host + "/SecuredServlet");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String auth = Base64.getEncoder().encodeToString(("quickstartUser" + ":" + "quickstartPwd1!").getBytes(StandardCharsets.UTF_8));

        final HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("Authorization", "Basic " + auth)
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
}
