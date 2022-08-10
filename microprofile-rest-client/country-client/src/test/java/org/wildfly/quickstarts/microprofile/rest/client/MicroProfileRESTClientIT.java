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
package org.wildfly.quickstarts.microprofile.rest.client;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.quickstarts.microprofile.rest.client.api.CountryProviderResource;
import org.wildfly.quickstarts.microprofile.rest.client.model.Country;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import java.net.URL;

/**
 * Simple tests for MicroProfile REST Client quickstart. Arquillian deploys an JAR archive to the application server,
 * which contains several REST endpoints and verifies that they are correctly invoking the external service through
 * the MicroProfile REST Client.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MicroProfileRESTClientIT {

    private static final String COUNTRY_SERVER = "country-server";
    private static final String COUNTRY_CLIENT = "country-client";

    @ArquillianResource
    @OperateOnDeployment(COUNTRY_CLIENT)
    private URL deploymentURL;

    private Client client;

    @Before
    public void before() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Constructs a server deployment archive
     *
     * @return the server deployment archive
     */
    @Deployment(name = COUNTRY_SERVER, order = 1)
    public static WebArchive createServerDeployment() {
        return ShrinkWrap.create(WebArchive.class, COUNTRY_SERVER + ".war")
            .addClasses(JaxRsApplication.class, CountryProviderResource.class, Country.class)
            // enable CDI
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment(name = COUNTRY_CLIENT, order = 2)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, COUNTRY_CLIENT+ ".war")
            .addPackages(true, JaxRsApplication.class.getPackage())
            .addAsResource("META-INF/microprofile-config.properties")
            // enable CDI
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Tests that CDI injected MP REST Client is responding correctly.
     */
    @Test
    public void testCDIInvocation() {
        Response response = invoke("/country/cdi/France");

        Assert.assertEquals("Invalid response code returned from REST client invocation", 200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        assertFranceJSON(json);

        response.close();
    }

    /**
     * Tests that programmatically created MP REST Client is responding correctly.
     */
    @Test
    public void testProgrammaticInvocation() {
        Response response = invoke("/country/programmatic/France");

        Assert.assertEquals("Invalid response code returned from REST client invocation", 200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        assertFranceJSON(json);

        response.close();
    }

    /**
     * Tests that async invocation of MP REST Client is responding correctly.
     */
    @Test
    public void testAsyncInvocation() {
        Response response = invoke("/country/name-async/France");

        Assert.assertEquals("Invalid response code returned from REST client invocation", 200, response.getStatus());
        ModelNode json = ModelNode.fromJSONString(response.readEntity(String.class));

        assertFranceJSON(json);

        response.close();
    }

    /**
     * Tests that MP REST Client response exception mapper is responding correctly.
     */
    @Test
    public void testExceptionMapper() {
        Response response = invoke("/country/cdi/doesNotExist");

        Assert.assertEquals("Exception mapper not invoked correctly.",
            Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        response.close();
    }

    private Response invoke(String path) {
        return client
            .target(deploymentURL.toString())
            .path(path)
            .request()
            .get();
    }

    private void assertFranceJSON(ModelNode json) {
        Assert.assertEquals("Returned invalid country object", json.get("name").asString(), "France");
        Assert.assertEquals("Returned invalid country object", json.get("capital").asString(), "Paris");
        Assert.assertEquals("Returned invalid country object", json.get("currency").asString(), "EUR");
    }
}
