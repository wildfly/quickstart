/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.jaxrsclient;

/**
 * This example demonstrates the use JAX-RS 2.0  client
 * which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS 
 * in JBoss WildFly.  Specifically, this client request resources served by the
 * HelloWorld JAX-RS Web Service created in quickstart helloworld-rs.
 * Please refer to the helloworld-rs README.md for instructions on how to
 * build and deploy helloworld-rs.
 */

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.jsonp.JsonObjectProvider;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit4 Test class which makes a request to the RESTful helloworld-rs web service.
 * 
 * @author bmincey (Blaine Mincey)
 * 
 */
public class JaxRsClientTest {

    private static String DEFAULT_RESOURCE_URL = "http://localhost:8080/wildfly-helloworld-rs/rest/";

    /**
     * Property names used to pull value from system properties in pom.xml
     */
    private static final String XML_PROPERTY = "resourceUrl";

    /**
     * Expected responses of the RESTful web service for the XML media type
     */
    private static final String XML_RESPONSE = "<xml><result>Hello World!</result></xml>";

    private static String resourceURL;

    @BeforeClass
    public static void beforeClass() {
        resourceURL = System.getProperty(XML_PROPERTY, DEFAULT_RESOURCE_URL);
    }

    /**
     * Test method which executes the getResource method that calls the RESTful helloworld-rs web service.
     */
    @Test
    public void testXML() {
        Response response = getResource(resourceURL, APPLICATION_XML_TYPE);

        String content = response.readEntity(String.class);
        System.out.println(content);

        assertEquals(XML_RESPONSE, content);
    }


    @Test
    public void testJSON() {
        Response response = getResource(resourceURL, APPLICATION_JSON_TYPE);

        JsonObject content = response.readEntity(JsonObject.class);
        System.out.println(content);

        assertTrue(content.containsKey("result"));
        assertEquals("Hello World!", content.getString("result"));
    }

    /**
     * The purpose of this method is to run the external REST request.
     * 
     * @param url The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private Response getResource(String url, MediaType mediaType) {
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        // Using the JAX-RS client, initiate a request
        // using the url as the target
        Client client = ClientBuilder.newClient();
        Response response = client.target(url)
                .register(JsonObjectProvider.class)
                .request(mediaType)
                .get();

        // Check the HTTP status of the request
        // HTTP 200 indicates the request is OK
        assertEquals(200, response.getStatus());

        return response;
    }

}
