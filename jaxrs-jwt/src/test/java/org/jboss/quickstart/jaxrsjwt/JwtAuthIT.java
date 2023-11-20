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

package org.jboss.quickstart.jaxrsjwt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class JwtAuthIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/jaxrs-jwt";

    private static Client adminClient;
    private static Client customerClient;
    private static Client noAuthClient;

    @BeforeAll
    public static void createClients() {
        adminClient = createAdminClient();
        customerClient = createCustomerClient();
        noAuthClient = ClientBuilder.newClient();
    }

    @AfterAll
    public static void closeClients() {
        if (adminClient != null) adminClient.close();
        if (customerClient != null) customerClient.close();
        if (noAuthClient != null) noAuthClient.close();
    }

    @Test
    public void adminProtected() {
        final JsonObject json = get(adminClient, Response.Status.OK, resolveBaseUrl().path("protected"));
        Assertions.assertEquals("Hello admin!", json.getString("result"));
    }

    @Test
    public void adminPublic() {
        final JsonObject json = get(adminClient, Response.Status.OK, resolveBaseUrl().path("public"));
        Assertions.assertEquals("Hello admin!", json.getString("result"));
    }

    @Test
    public void adminCustomer() {
        get(adminClient, Response.Status.FORBIDDEN, resolveBaseUrl().path("customer"));
    }

    @Test
    public void adminClaims() {
        final JsonObject json = get(adminClient, Response.Status.OK, resolveBaseUrl().path("claims"));
        Assertions.assertEquals("admin", json.getJsonArray("groups").getString(0));
    }

    @Test
    public void customerProtected() {
        get(customerClient, Response.Status.FORBIDDEN, resolveBaseUrl().path("protected"));
    }

    @Test
    public void customerPublic() {
        final JsonObject json = get(customerClient, Response.Status.OK, resolveBaseUrl().path("public"));
        Assertions.assertEquals("Hello customer!", json.getString("result"));
    }

    @Test
    public void customerCustomer() {
        final JsonObject json = get(customerClient, Response.Status.OK, resolveBaseUrl().path("customer"));
        Assertions.assertEquals("Hello customer!", json.getString("result"));
    }

    @Test
    public void customerClaims() {
        final JsonObject json = get(customerClient, Response.Status.OK, resolveBaseUrl().path("claims"));
        Assertions.assertEquals("customer", json.getJsonArray("groups").getString(0));
    }

    @Test
    public void noAuthProtected() {
        get(noAuthClient, Response.Status.UNAUTHORIZED, resolveBaseUrl().path("protected"));
    }

    @Test
    public void noAuthPublic() {
        final JsonObject json = get(noAuthClient, Response.Status.OK, resolveBaseUrl().path("public"));
        Assertions.assertEquals("Hello anonymous!", json.getString("result"));
    }

    @Test
    public void noAuthCustomer() {
        get(noAuthClient, Response.Status.UNAUTHORIZED, resolveBaseUrl().path("customer"));
    }

    @Test
    public void noAuthClaims() {
        get(noAuthClient, Response.Status.UNAUTHORIZED, resolveBaseUrl().path("claims"));
    }

    private static Client createAdminClient() {
        return createClient("admin", "adminpw");
    }

    private static Client createCustomerClient() {
        return createClient("customer", "customerpw");
    }

    private static Client createClient(final String username, final String password) {
        if (username == null || password == null) {
            return ClientBuilder.newClient();
        }
        return ClientBuilder.newBuilder()
                .register(new BearerTokenAuthorizationFilter(username, password))
                .build();
    }

    private static JsonObject get(final Client client, final Response.Status expectedStatus, final UriBuilder uriBuilder) {
        try (
                Response response = client.target(uriBuilder).request().get()
        ) {
            Assertions.assertEquals(expectedStatus, response.getStatusInfo(), () ->
                    String.format("Expected status %s for resource %s got \"%d: %s\": %s", expectedStatus, uriBuilder.build(),
                            response.getStatus(), response.getStatusInfo(), response.readEntity(String.class)));
            if (expectedStatus == Response.Status.OK) {
                return response.readEntity(JsonObject.class);
            }
        }
        return JsonObject.EMPTY_JSON_OBJECT;
    }

    private static UriBuilder resolveBaseUrl() {
        final String baseUrl = resolveServerHost();
        return UriBuilder.fromUri(baseUrl).path("rest");
    }

    private static String resolveServerHost() {
        final String result = System.getenv("SERVER_HOST");
        return result != null ? result : System.getProperty("server.host", DEFAULT_SERVER_HOST);
    }

    /**
     * This is an example filter and should not be used in production
     */
    private static class BearerTokenAuthorizationFilter implements ClientRequestFilter {

        private final Supplier<String> tokenSupplier;
        private final Lock lock;
        private volatile String cachedToken;

        private BearerTokenAuthorizationFilter(final String username, final String password) {
            lock = new ReentrantLock();
            this.tokenSupplier = () -> {
                final UriBuilder uriBuilder = resolveBaseUrl().path("token");
                final Form form = new Form();
                form.param("username", username);
                form.param("password", password);
                try (
                        Client client = ClientBuilder.newClient();
                        Response response = client
                                .target(uriBuilder)
                                .request()
                                .post(Entity.form(form))
                ) {
                    Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () ->
                            String.format("Failed to get token: %d: %s", response.getStatus(), response.readEntity(String.class)));
                    final JsonObject token = response.readEntity(JsonObject.class);
                    Assertions.assertNotNull(token);
                    return token.getString("token");
                }
            };
        }

        @Override
        public void filter(final ClientRequestContext requestContext) {
            if (cachedToken == null) {
                try {
                    lock.lock();
                    if (cachedToken == null) {
                        cachedToken = tokenSupplier.get();
                    }
                } finally {
                    lock.unlock();
                }
            }
            requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, "Bearer " + cachedToken);
        }
    }
}
