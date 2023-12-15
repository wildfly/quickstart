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

import java.util.List;
import java.util.ArrayList;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple integration test for Coffee resource that is guarded by MicroProfile Fault Tolerance annotations.
 *
 * @author Radoslav Husar
 * @author Eduardo Martins
 */

public class CoffeeResourceIT {

    private Client client;

    static {
        ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(instance);
        instance.registerProvider(ResteasyJackson2Provider.class);
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
        this.setFailRatio(0f);
        this.resetCounter();

        try (Response response = this.getResponse("/coffee")) {
            Assert.assertEquals(200, response.getStatus());

            List<Coffee> entity = response.readEntity(new GenericType<List<Coffee>>() {});
            Assert.assertNotNull(entity);
            Assert.assertEquals(3, entity.size());
        }
    }

    @Test
    public void testCoffeeListFailure() {
        this.setFailRatio(1f);
        this.resetCounter();

        try (Response response = this.getResponse("/coffee")) {
            Assert.assertEquals(500, response.getStatus());
        }

        Assert.assertEquals(5, this.getCounter());
    }

    @Test
    public void testCoffeeDetail() {
        this.setFailRatio(0f);

        try (Response response = this.getResponse("/coffee/1")) {
            Assert.assertEquals(200, response.getStatus());

            Coffee entity = response.readEntity(new GenericType<Coffee>() {});
            Assert.assertNotNull(entity);
            Assert.assertEquals("Colombia", entity.countryOfOrigin);
        }
    }

    @Test
    public void testCoffeeDetailFailure() {
        this.setFailRatio(1f);

        try (Response response = this.getResponse("/coffee/1")) {
            Assert.assertEquals(500, response.getStatus());
        }
    }

    private long getCounter() {
        try (Response response = this.getResponse("/coffee/getCounter")) {
            Assert.assertEquals(200, response.getStatus());
            return response.readEntity(Long.class);
        }
    }

    private void setFailRatio(float failRatio) {
        try (Response response = this.getResponse("/coffee/setFailRatio/" + failRatio)) {
            Assert.assertEquals(204, response.getStatus());
        }
    }

    private void resetCounter() {
        try (Response response = this.getResponse("/coffee/resetCounter")) {
            Assert.assertEquals(204, response.getStatus());
        }
    }

    /**
     * Testing whether customer has at his disposal all the suggestions for coffee.
     * <p>
     * In cases of small-time delay, the total number of recommendations will appear and fallbackMethod won't be called.
    */
    @Test
    public void testCoffeeRecommendations() {
        setMinDelay(0);
        setMaxDelay(250);

        try (Response response = this.getResponse("/coffee/1/recommendations")) {
            Assert.assertEquals(200, response.getStatus());
            ArrayList<Coffee> ordersList = response.readEntity(new GenericType<ArrayList<Coffee>>() {});
            Assert.assertNotNull(ordersList);
            Assert.assertEquals(2, ordersList.size());
            Assert.assertNotEquals(1, ordersList.get(0).getId());
            Assert.assertNotEquals(1, ordersList.get(1).getId());
        }
    }

   /**
     * Testing if fallbackMethod will offer to the customer one recommendation.
     * <p>
     * In cases of big-time delay fallbackMethod will propose a safe recommendation choice.
    */
    @Test
    public void testCoffeeRecommendationsSafeChoice() {
        setMinDelay(251);
        setMaxDelay(500);

        try (Response response = this.getResponse("/coffee/1/recommendations")) {
            Assert.assertEquals(200, response.getStatus());

            ArrayList<Coffee> ordersList = response.readEntity(new GenericType<ArrayList<Coffee>>() {});
            Assert.assertNotNull(ordersList);
            Assert.assertEquals(1, ordersList.size());
            Assert.assertEquals(1, ordersList.get(0).getId());
        }
    }

    private void setMaxDelay(int maxDelay) {
        try (Response response = this.getResponse("/coffee/setMaxDelay/" + maxDelay)) {
            Assert.assertEquals(204, response.getStatus());
        }
    }

    private void setMinDelay(int minDelay) {
        try (Response response = this.getResponse("/coffee/setMinDelay/" + minDelay)) {
            Assert.assertEquals(204, response.getStatus());
        }
    }

    private Response getResponse(String path) {
        return client.target(TestUtils.getServerHost())
                .path(path)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
