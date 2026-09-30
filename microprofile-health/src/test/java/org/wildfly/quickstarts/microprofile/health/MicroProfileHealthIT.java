/*
 * JBoss, Home of Professional Open Source
 * Copyright 2019, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.quickstarts.microprofile.health;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.jboss.dmr.ModelNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Simple tests for MicroProfile Health quickstart.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
public class MicroProfileHealthIT {

    private URL managementURL;
    private Client client;

    @BeforeEach
    public void before() throws MalformedURLException {
        String managementHost = getConfigValue("server.management.host").orElse("http://localhost:9990");
        managementURL = URI.create(managementHost).toURL();
        client = ClientBuilder.newClient();
    }

    private Optional<String> getConfigValue(String key) {
        String value = System.getenv(key.toUpperCase().replaceAll("\\.", "_"));
        if (value == null) {
            value = System.getProperty(key);
        }
        return Optional.ofNullable(value);
    }

    @AfterEach
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Tests that liveness contents (/health/live) contain correct data about two defined @Liveness procedures.
     */
    @Test
    public void testLivenessContents() {
        Response response =  client
            .target(managementURL.toString())
            .path("/health/live")
            .request()
            .get();

        Assertions.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assertions.assertEquals("UP", json.get("status").asString());

        List<ModelNode> checks = json.get("checks").asList();
        Assertions.assertEquals(2, checks.size());

        for (ModelNode check : checks) {
            String name = check.get("name").asString();
            Assertions.assertTrue(name.equals("Simple health check") || name.equals("Health check with data"));

            Assertions.assertEquals("UP", check.get("status").asString());

            if (name.equals("Health check with data")) {
                ModelNode data = check.get("data");

                Assertions.assertTrue(data.get("bar") != null && data.get("bar").asString().equals("barValue"));
                Assertions.assertTrue(data.get("foo") != null && data.get("foo").asString().equals("fooValue"));
            }
        }
    }

    /**
     * Tests that readiness contents (/health/ready) contain correct data about the single defined @Readiness
     * procedure.
     */
    @Test
    public void testReadinessContents() {
        Response response =  client
            .target(managementURL.toString())
            .path("/health/ready")
            .request()
            .get();

        Assertions.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assertions.assertEquals("UP", json.get("status").asString());

        List<ModelNode> checks = json.get("checks").asList();

        boolean checkIncluded = false;

        for (int i = 0; i < checks.size(); i++) {
            ModelNode check = checks.get(i);
            if (check.get("name").asString().equals("Database connection health check")) {
                Assertions.assertEquals("UP", check.get("status").asString());

                checkIncluded = true;
            }
        }

        Assertions.assertTrue(checkIncluded, "The user defined check is not included in the readiness response");
    }
}
