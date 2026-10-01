/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.helloworld.rest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The very basic runtime integration testing for a rest endpoint.
 * @author Ashwin Mehendale
 * @author emartins
 */
public class RESTEndPointIT {
    private Client client;
    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private static String getServerHost() {
    String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        return serverHost;
    }

    @BeforeEach
    public void before() {
        client = ClientBuilder.newClient();
    }

    @AfterEach
    public void after() {
        client.close();
    }

    @Test
    public void testRestEndPoint() {
        String responseMessage = client.target(getServerHost())
                .path("helloworld-rs/rest/HelloWorld")
                .request(MediaType.TEXT_PLAIN).get(String.class);
        assertEquals("Hello World!", responseMessage);
    }
}