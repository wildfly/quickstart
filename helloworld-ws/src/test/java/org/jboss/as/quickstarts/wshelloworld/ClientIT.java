/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.wshelloworld;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Simple set of tests for the HelloWorld Web Service to demonstrate accessing the web service using a client
 *
 * @author lnewson@redhat.com
 */
public class ClientIT {
    /**
     * The path of the WSDL endpoint in relation to the deployed web application.
     */
    private static final String WSDL_PATH = "HelloWorldService?wsdl";

    protected static URL getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080";
        }
        try {
            return new URI(host + "/" + WSDL_PATH).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }
    private static URL deploymentUrl;

    private HelloWorldService client;

    @BeforeClass
    public static void beforeClass() throws MalformedURLException {
        URL deploymentUrl = getHTTPEndpoint();

        System.out.println("WSDL Deployment URL: " + deploymentUrl);

        // Set the deployment url
        ClientIT.deploymentUrl = deploymentUrl;
    }

    @Before
    public void setup() {
        try {
            client = new Client(new URL(deploymentUrl, WSDL_PATH));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHello() {
        System.out.println("[Client] Requesting the WebService to say Hello.");

        // Get a response from the WebService
        final String response = client.sayHello();
        assertEquals(response, "Hello World!");

        System.out.println("[WebService] " + response);

    }

    @Test
    public void testHelloName() {
        System.out.println("[Client] Requesting the WebService to say Hello to John.");

        // Get a response from the WebService
        final String response = client.sayHelloToName("John");
        assertEquals(response, "Hello John!");

        System.out.println("[WebService] " + response);
    }

    @Test
    public void testHelloNames() {
        System.out.println("[Client] Requesting the WebService to say Hello to John, Mary and Mark.");

        // Create the array of names for the WebService to say hello to.
        final List<String> names = new ArrayList<>();
        names.add("John");
        names.add("Mary");
        names.add("Mark");

        // Get a response from the WebService
        final String response = client.sayHelloToNames(names);
        assertEquals(response, "Hello John, Mary & Mark!");

        System.out.println("[WebService] " + response);
    }
}
