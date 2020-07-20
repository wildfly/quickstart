/*
 * JBoss, Home of Professional Open Source
 * Copyright 2020, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.microprofile.faulttolerance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;

@RunWith(Arquillian.class)
public class MicroProfileFaultToleranceIT {

    private static final String APP_NAME = "microprofile-fault-tolerance";

    @ArquillianResource
    private URL deploymentUrl;

    @Inject
    private CoffeeResource coffeeResource;

    private Client client;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, APP_NAME + ".war")
                .addPackage(CoffeeApplication.class.getPackage());
    }

    @Before
    public void before() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void testCoffeeList() {
        coffeeResource.setFailRatio(0f);
        coffeeResource.resetCounter();

        try (Response response = this.getResponse("/coffee")) {
            Assert.assertEquals(200, response.getStatus());

            List<Coffee> entity = response.readEntity(new GenericType<List<Coffee>>() {});
            Assert.assertNotNull(entity);
            Assert.assertEquals(3, entity.size());
        }
    }

    @Test
    public void testCoffeeListFailure() {
        coffeeResource.setFailRatio(1f);
        coffeeResource.resetCounter();

        try (Response response = this.getResponse("/coffee")) {
            Assert.assertEquals(500, response.getStatus());
            Assert.assertEquals(5, coffeeResource.getCounter().longValue());
        }
    }

    @Test
    public void testCoffeeDetail() {
        coffeeResource.setFailRatio(0f);

        try (Response response = this.getResponse("/coffee/1")) {
            Assert.assertEquals(200, response.getStatus());

            Coffee entity = response.readEntity(new GenericType<Coffee>() {});
            Assert.assertNotNull(entity);
            Assert.assertEquals("Colombia", entity.countryOfOrigin);
        }
    }

    @Test
    public void testCoffeeDetailFailure() {
        coffeeResource.setFailRatio(1f);

        try (Response response = this.getResponse("/coffee/1")) {
            Assert.assertEquals(500, response.getStatus());
        }
    }

    private Response getResponse(String path) {
        return client.target(deploymentUrl.toString())
                .path(path)
                .request()
                .get();
    }
}
