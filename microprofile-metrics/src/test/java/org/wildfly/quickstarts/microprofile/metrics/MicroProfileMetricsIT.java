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
package org.wildfly.quickstarts.microprofile.metrics;

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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Simple tests for MicroProfile Metrics quickstart. Arquillian deploys an JAR archive to the application server, which
 * contains several REST endpoints and verifies that the metrics are correctly exposed.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MicroProfileMetricsIT {

    private URL managementURL;

    @ArquillianResource
    private ManagementClient managementClient;

    @ArquillianResource
    private URL deploymentURL;

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
            .addClasses(JaxRsApplication.class, PrimeNumberChecker.class)
            // enable CDI
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Tests that /prime/{number} metrics are correctly collected.
     */
    @Test
    public void testPrimeMetrics() {
        restAppGetInvoke(client, "/prime/350");
        restAppGetInvoke(client, "/prime/7");
        restAppGetInvoke(client, "/prime/29");

        Response response = client.
            target(managementURL.toString())
            .path("/metrics/application")
            .request()
            .header("Accept", MediaType.APPLICATION_JSON)
            .get();

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals(3, json.get("org.wildfly.quickstarts.microprofile.metrics.PrimeNumberChecker.performedChecks").asInt());
        Assert.assertEquals(3, json.get("checksTimer").get("count").asInt());
        Assert.assertEquals(3, json.get("checkIfPrimeFrequency").get("count").asInt());
        Assert.assertEquals(29, json.get("org.wildfly.quickstarts.microprofile.metrics.PrimeNumberChecker.highestPrimeNumberSoFar").asInt());
    }

    /**
     * Tests that /parallel metrics are correctly collected.
     */
    @Test
    public void testParallelMetrics() throws Exception {
        int n = 3;
        WebTarget webTarget = client.target(deploymentURL.toString())
            .path("/parallel");

        List<Future<Response>> responses = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            responses.add(webTarget.request().async().get());
        }

        // 3 parallel requests are in flight, give metrics a while to be collected
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check the application metrics
        Response response = client.
            target(managementURL.toString())
            .path("/metrics/application")
            .request()
            .header("Accept", MediaType.APPLICATION_JSON)
            .get();

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals("Invalid number of current parallel accesses",
            3, json.get("org.wildfly.quickstarts.microprofile.metrics.PrimeNumberChecker.parallelAccess").get("current").asInt());

        // finish the started requests
        restAppGetInvoke(client, "/parallel-finish");

        for (int i = 0; i < n; i++) {
            responses.get(i).get().close();
        }
    }

    /**
     * Tests that /injected-metric metrics are correctly collected.
     */
    @Test
    public void testInjectedMetrics() {
        for (int i = 0; i < 3; i++) {
            restAppGetInvoke(client, "/injected-metric");
        }

        Response response = client.
            target(managementURL.toString())
            .path("/metrics/application")
            .request()
            .header("Accept", MediaType.APPLICATION_JSON)
            .get();

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals(3, json.get("injectedCounter").asInt());
    }

    /**
     * Tests that /registry metrics are correctly collected.
     */
    @Test
    public void testRegistryMetrics() {
        restAppGetInvoke(client, "/registry");

        Response response = client.
            target(managementURL.toString())
            .path("/metrics/application")
            .request()
            .header("Accept", MediaType.APPLICATION_JSON)
            .get();

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals(42, json.get("programmaticCounter").asInt());
    }

    /**
     * Tests that tagged duplicated metrics are correctly collected.
     */
    @Test
    public void testDuplicatedMetrics() {
        restAppGetInvoke(client, "/duplicates");
        restAppGetInvoke(client, "/duplicates2");

        Response response = client.
            target(managementURL.toString())
            .path("/metrics/application")
            .request()
            .header("Accept", MediaType.APPLICATION_JSON)
            .get();

        Assert.assertEquals(200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        Assert.assertEquals(1, json.get("duplicatedCounter;type=original").asInt());
        Assert.assertEquals(1, json.get("duplicatedCounter;type=copy").asInt());
    }


    private void restAppGetInvoke(Client client, String path) {
        Response response = client
            .target(deploymentURL.toString())
            .path(path)
            .request()
            .get();

        response.close();
    }
}
