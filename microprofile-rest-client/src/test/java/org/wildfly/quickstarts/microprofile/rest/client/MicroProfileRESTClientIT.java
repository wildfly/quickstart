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
package org.wildfly.quickstarts.microprofile.rest.client;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.json.JsonObject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Simple tests for MicroProfile REST Client quickstart. Starts a {@link SeBootstrap Jakarta REST SE server} which
 * invokes calls to the remote container.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class MicroProfileRESTClientIT {

    private static SeBootstrap.Instance TEST_RUNNER;

    private static Client CLIENT;

    @BeforeAll
    public static void startTestContainer() throws Exception {
        TEST_RUNNER = SeBootstrap.start(new TestApplication()).toCompletableFuture().get(5, TimeUnit.SECONDS);
        CLIENT = ClientBuilder.newClient();
    }

    @AfterAll
    public static void shutdownTestContainer() throws Exception {
        if (TEST_RUNNER != null) {
            TEST_RUNNER.stop().toCompletableFuture().get(5, TimeUnit.SECONDS);
        }
        if (CLIENT != null) CLIENT.close();
    }

    /**
     * Tests that CDI injected MP REST Client is responding correctly.
     */
    @Test
    public void testCDIInvocation() {
        try (Response response = invoke("/country/cdi/France")) {
            Assertions.assertEquals(200, response.getStatus(), () -> "Invalid response code returned from REST client invocation: " + response.readEntity(String.class));
            final JsonObject json = response.readEntity(JsonObject.class);

            assertFranceJSON(json);
        }
    }

    /**
     * Tests that programmatically created MP REST Client is responding correctly.
     */
    @Test
    public void testProgrammaticInvocation() {
        try (Response response = invoke("/country/programmatic/France")) {
            Assertions.assertEquals(200, response.getStatus(), () -> "Invalid response code returned from REST client invocation: " + response.readEntity(String.class));
            final JsonObject json = response.readEntity(JsonObject.class);

            assertFranceJSON(json);
        }
    }

    /**
     * Tests that async invocation of MP REST Client is responding correctly.
     */
    @Test
    public void testAsyncInvocation() {
        try (Response response = invoke("/country/name-async/France")) {
            Assertions.assertEquals(200, response.getStatus(), () -> "Invalid response code returned from REST client invocation: " + response.readEntity(String.class));
            final JsonObject json = response.readEntity(JsonObject.class);

            assertFranceJSON(json);
        }
    }

    /**
     * Tests that MP REST Client response exception mapper is responding correctly.
     */
    @Test
    public void testExceptionMapper() {
        try (Response response = invoke("/country/cdi/doesNotExist")) {
            Assertions.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus(), "Exception mapper not invoked correctly.");
        }
    }

    private Response invoke(String path) {
        return CLIENT.target(TEST_RUNNER.configuration().baseUriBuilder()).path(path).request().get();
    }

    private void assertFranceJSON(final JsonObject json) {
        Assertions.assertEquals(json.getString("name"), "France", "Returned invalid country object");
        Assertions.assertEquals(json.getString("capital"), "Paris", "Returned invalid country object");
        Assertions.assertEquals(json.getString("currency"), "EUR", "Returned invalid country object");
    }

    @ApplicationPath("/")
    public static class TestApplication extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            return Set.of(CountriesResource.class);
        }
    }
}
