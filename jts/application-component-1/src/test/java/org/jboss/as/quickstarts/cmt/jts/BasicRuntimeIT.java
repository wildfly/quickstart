/*
 * Copyright 2026 JBoss by Red Hat.
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
package org.jboss.as.quickstarts.cmt.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost";
    private static final int DEFAULT_SERVER_PORT = 8080;
    private static final String APP_CONTEXT = "/jts-application-component-1";

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = getServerHost(0);
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverHost + APP_CONTEXT + "/"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(
                "<meta http-equiv=\"Refresh\" content=\"0; URL=addCustomer.jsf\">"));
    }

    @Test
    public void testAddCustomer() throws Exception {
        HttpClient client = createHttpClient();
        String uniqueName = "TestCustomer_" + System.currentTimeMillis();
        long baselineMessages = getMessagesReceived();

        HttpResponse<String> response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(uniqueName));
        assertTrue(response.uri().toString().contains("customers.jsf"));

        waitForMessagesReceived(baselineMessages + 1, 30_000);
    }

    @Test
    public void testDuplicateCustomerRollback() throws Exception {
        HttpClient client = createHttpClient();
        String uniqueName = "DuplicateTest_" + System.currentTimeMillis();
        long baselineMessages = getMessagesReceived();

        HttpResponse<String> response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(uniqueName));
        assertTrue(response.uri().toString().contains("customers.jsf"));

        waitForMessagesReceived(baselineMessages + 1, 30_000);

        response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.uri().toString().contains("duplicate.jsf"));

        assertMessagesStayAt(baselineMessages + 1, 10_000);
    }

    // SERVER_HOST is expected to include the port (e.g. http://localhost:8080).
    // When portOffset is non-zero, the port is parsed and the offset is added
    // (defaulting to 80 for http or 443 for https if no port is present).
    private String getServerHost(int portOffset) {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        if (host == null) {
            host = DEFAULT_SERVER_HOST + ":" + (DEFAULT_SERVER_PORT + portOffset);
        } else if (portOffset != 0) {
            try {
                URI uri = new URI(host);
                int defaultPort = "https".equals(uri.getScheme()) ? 443 : 80;
                int port = (uri.getPort() != -1 ? uri.getPort() : defaultPort) + portOffset;
                host = uri.getScheme() + "://" + uri.getHost() + ":" + port;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return host;
    }

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    private HttpResponse<String> submitCustomerForm(HttpClient client, String name) throws IOException, InterruptedException, URISyntaxException {
        String serverHost = getServerHost(0);
        URI formUri = new URI(serverHost + APP_CONTEXT + "/addCustomer.jsf");

        HttpResponse<String> getResponse = client.send(
                HttpRequest.newBuilder(formUri).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        String body = getResponse.body();
        // JSF requires the ViewState token from the hidden form field to accept a POST submission
        Matcher viewStateMatcher = Pattern.compile("name=\"jakarta\\.faces\\.ViewState\"[^>]*value=\"([^\"]+)\"").matcher(body);
        assertTrue("ViewState not found in response", viewStateMatcher.find());
        String viewState = viewStateMatcher.group(1);

        HttpRequest postRequest = HttpRequest.newBuilder(formUri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(Map.of(
                        "addCustomerForm", "addCustomerForm",
                        "addCustomerForm:name", name,
                        "addCustomerForm:add", "Add",
                        "jakarta.faces.ViewState", viewState)))
                .build();
        return client.send(postRequest, HttpResponse.BodyHandlers.ofString());
    }

    private void waitForMessagesReceived(long expected, long timeoutMs) throws IOException, InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (getMessagesReceived() == expected) return;
            Thread.sleep(500);
        }
        assertEquals("Timed out waiting for messages received to reach " + expected,
                expected, getMessagesReceived());
    }

    private void assertMessagesStayAt(long expected, long durationMs) throws IOException, InterruptedException {
        long deadline = System.currentTimeMillis() + durationMs;
        while (System.currentTimeMillis() < deadline) {
            assertEquals(expected, getMessagesReceived());
            Thread.sleep(500);
        }
    }

    private long getMessagesReceived() throws IOException, InterruptedException {
        String server2Host = getServerHost(100);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server2Host + "/jts-application-component-2/messages/count"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("Message count request failed with status " + response.statusCode(),
                200, response.statusCode());
        return Long.parseLong(response.body().trim());
    }

    private static BodyPublisher ofFormData(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return BodyPublishers.ofString(builder.toString());
    }
}
