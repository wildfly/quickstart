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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Simple tests for MicroProfile Health quickstart. Arquillian deploys an JAR archive to the application server, which
 * contains several health checks and verifies that they are correctly invoked.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MicroProfileHealthIT {

    private URL managementURL;

    @ArquillianResource
    private ManagementClient managementClient;

    private Client client;

    @Before
    public void before() throws MalformedURLException {
        managementURL = new URL(String.format("http://%s:%d",
            managementClient.getMgmtAddress(), managementClient.getMgmtPort()));
        client = ClientBuilder.newClient();
    }

    @After
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(SimpleHealthCheck.class, DatabaseConnectionHealthCheck.class, DataHealthCheck.class)
            // enable CDI
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
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

        Assert.assertEquals(503, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals("DOWN", json.get("status").asString());

        List<ModelNode> checks = json.get("checks").asList();
        Assert.assertEquals(4, checks.size());

        boolean checkIncluded = false;

        for (int i = 0; i < 4; i++) {
            ModelNode check = checks.get(i);
            if (check.get("name").asString().equals("Database connection health check")) {
                Assert.assertEquals("DOWN", check.get("status").asString());

                ModelNode data = check.get("data");
                Assert.assertTrue(data.get("error") != null &&
                    data.get("error").asString().equals("Cannot contact database"));

                checkIncluded = true;
            }
        }

        Assert.assertTrue("The user defined check is not included in the readiness response", checkIncluded);
    }
}
