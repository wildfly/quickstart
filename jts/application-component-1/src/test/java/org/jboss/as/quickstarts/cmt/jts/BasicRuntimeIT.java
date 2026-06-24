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

import static org.jboss.as.controller.client.helpers.ClientConstants.CHILD_TYPE;
import static org.jboss.as.controller.client.helpers.ClientConstants.NAME;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP;
import static org.jboss.as.controller.client.helpers.ClientConstants.OP_ADDR;
import static org.jboss.as.controller.client.helpers.ClientConstants.OUTCOME;
import static org.jboss.as.controller.client.helpers.ClientConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.client.helpers.ClientConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.client.helpers.ClientConstants.RESULT;
import static org.jboss.as.controller.client.helpers.ClientConstants.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetAddress;
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

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.junit.Test;

public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";
    private static final String APP_CONTEXT = "/jts-application-component-1";
    private static final String DEFAULT_SERVER2_MGMT_HOST = "localhost";
    private static final int DEFAULT_SERVER2_MGMT_PORT = 10090;

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = getServerHost();
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
        long baselineMessages = getMessagesAdded();

        HttpResponse<String> response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(uniqueName));
        assertTrue(response.uri().toString().contains("customers.jsf"));

        waitForMessagesAdded(baselineMessages + 1, 30_000);
    }

    @Test
    public void testDuplicateCustomerRollback() throws Exception {
        HttpClient client = createHttpClient();
        String uniqueName = "DuplicateTest_" + System.currentTimeMillis();
        long baselineMessages = getMessagesAdded();

        HttpResponse<String> response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains(uniqueName));
        assertTrue(response.uri().toString().contains("customers.jsf"));

        waitForMessagesAdded(baselineMessages + 1, 30_000);

        response = submitCustomerForm(client, uniqueName);
        assertEquals(200, response.statusCode());
        assertTrue(response.uri().toString().contains("duplicate.jsf"));

        assertMessagesStayAt(baselineMessages + 1, 10_000);
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        if (host == null) {
            host = DEFAULT_SERVER_HOST;
        }
        return host;
    }

    private String getServer2MgmtHost() {
        String host = System.getenv("SERVER2_MGMT_HOST");
        if (host == null) {
            host = System.getProperty("server2.mgmt.host");
        }
        if (host == null) {
            host = DEFAULT_SERVER2_MGMT_HOST;
        }
        return host;
    }

    private int getServer2MgmtPort() {
        String port = System.getProperty("server2.mgmt.port");
        return port != null ? Integer.parseInt(port) : DEFAULT_SERVER2_MGMT_PORT;
    }

    private HttpClient createHttpClient() {
        return HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    private HttpResponse<String> submitCustomerForm(HttpClient client, String name) throws IOException, InterruptedException, URISyntaxException {
        String serverHost = getServerHost();
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

    private void waitForMessagesAdded(long expected, long timeoutMs) throws IOException, InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (getMessagesAdded() == expected) return;
            Thread.sleep(500);
        }
        assertEquals("Timed out waiting for messages-added to reach " + expected,
                expected, getMessagesAdded());
    }

    private void assertMessagesStayAt(long expected, long durationMs) throws IOException, InterruptedException {
        long deadline = System.currentTimeMillis() + durationMs;
        while (System.currentTimeMillis() < deadline) {
            assertEquals(expected, getMessagesAdded());
            Thread.sleep(500);
        }
    }

    private long getMessagesAdded() throws IOException {
        try (ModelControllerClient client = ModelControllerClient.Factory.create(
                InetAddress.getByName(getServer2MgmtHost()), getServer2MgmtPort())) {
            String deploymentName = findComponent2Deployment(client);
            ModelNode op = new ModelNode();
            op.get(OP).set(READ_ATTRIBUTE_OPERATION);
            op.get(OP_ADDR).add("deployment", deploymentName)
                           .add("subsystem", "messaging-activemq")
                           .add("server", "default")
                           .add("jms-queue", "jts-quickstart");
            op.get(NAME).set("messages-added");
            ModelNode result = client.execute(op);
            if (!SUCCESS.equals(result.get(OUTCOME).asString())) {
                throw new RuntimeException("Failed to read messages-added: "
                        + result.get("failure-description").asString());
            }
            return result.get(RESULT).asLong();
        }
    }

    private String findComponent2Deployment(ModelControllerClient client) throws IOException {
        ModelNode op = new ModelNode();
        op.get(OP).set(READ_CHILDREN_NAMES_OPERATION);
        op.get(CHILD_TYPE).set("deployment");
        ModelNode result = client.execute(op);
        if (!SUCCESS.equals(result.get(OUTCOME).asString())) {
            throw new RuntimeException("Failed to list deployments on Server 2: "
                    + result.get("failure-description").asString());
        }
        for (ModelNode entry : result.get(RESULT).asList()) {
            String name = entry.asString();
            if (name.startsWith("jts-application-component-2")) {
                return name;
            }
        }
        throw new RuntimeException("jts-application-component-2 deployment not found on Server 2");
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
