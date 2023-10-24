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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * Simple tests for MicroProfile Health quickstart. Arquillian deploys an JAR archive to the application server, which
 * contains several health checks and verifies that they are correctly invoked.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
public class MicroProfileHealthIT {

    private URL managementURL;
    private Client client;

    @Before
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

    @After
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

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals("UP", json.get("status").asString());

        List<ModelNode> checks = json.get("checks").asList();
        Assert.assertEquals(2, checks.size());

        for (ModelNode check : checks) {
            String name = check.get("name").asString();
            Assert.assertTrue(name.equals("Simple health check") || name.equals("Health check with data"));

            Assert.assertEquals("UP", check.get("status").asString());

            if (name.equals("Health check with data")) {
                ModelNode data = check.get("data");

                Assert.assertTrue(data.get("bar") != null && data.get("bar").asString().equals("barValue"));
                Assert.assertTrue(data.get("foo") != null && data.get("foo").asString().equals("fooValue"));
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

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals("UP", json.get("status").asString());

        List<ModelNode> checks = json.get("checks").asList();
        Assert.assertEquals(5, checks.size());

        boolean checkIncluded = false;

        for (int i = 0; i < 5; i++) {
            ModelNode check = checks.get(i);
            if (check.get("name").asString().equals("Database connection health check")) {
                Assert.assertEquals("UP", check.get("status").asString());

                checkIncluded = true;
            }
        }

        Assert.assertTrue("The user defined check is not included in the readiness response", checkIncluded);
    }
}
