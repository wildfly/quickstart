/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
 * This example demonstrates the use an external JAX-RS RestEasy client
 * which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS 
 * in JBoss AS 7.  Specifically, this client "calls" the HelloWorld JAX-RS
 * Web Service created in quickstart helloworld-rs.  Please refer to the helloworld-rs
 * README.md for instructions on how to build and deploy helloworld-rs.
 */
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.ws.rs.core.MediaType;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit4 Test class which makes a request to the RESTful helloworld-rs web service.
 * 
 * @author bmincey (Blaine Mincey)
 * 
 */
public class JaxRsClientTest {
    /**
     * Request URLs pulled from system properties in pom.xml
     */
    private static String XML_URL;
    private static String JSON_URL;

    /**
     * Property names used to pull values from system properties in pom.xml
     */
    private static final String XML_PROPERTY = "xmlUrl";
    private static final String JSON_PROPERTY = "jsonUrl";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "<xml><result>Hello World!</result></xml>";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";

    /**
     * Method executes BEFORE the test method. Values are read from system properties that can be modified in the pom.xml.
     */
    @BeforeClass
    public static void beforeClass() {
        JaxRsClientTest.XML_URL = System.getProperty(JaxRsClientTest.XML_PROPERTY);
        JaxRsClientTest.JSON_URL = System.getProperty(JaxRsClientTest.JSON_PROPERTY);
    }

    /**
     * Test method which executes the runRequest method that calls the RESTful helloworld-rs web service.
     */
    @Test
    public void test() {
        assertEquals("XML Response", JaxRsClientTest.XML_RESPONSE,
                this.runRequest(JaxRsClientTest.XML_URL, MediaType.APPLICATION_XML_TYPE));

        assertEquals("JSON Response", JaxRsClientTest.JSON_RESPONSE,
                this.runRequest(JaxRsClientTest.JSON_URL, MediaType.APPLICATION_JSON_TYPE));
    }

    /**
     * The purpose of this method is to run the external REST request.
     * 
     * @param url The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private String runRequest(String url, MediaType mediaType) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());

        try {
            // Using the RESTEasy libraries, initiate a client request
            // using the url as a parameter
            ClientRequest request = new ClientRequest(url);

            // Be sure to set the mediatype of the request
            request.accept(mediaType);

            // Request has been made, now let's get the response
            ClientResponse<String> response = request.get(String.class);

            // Check the HTTP status of the request
            // HTTP 200 indicates the request is OK
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
            }

            // We have a good response, let's now read it
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getEntity()
                    .getBytes())));

            // Loop over the br in order to print out the contents
            System.out.println("\n*** Response from Server ***\n");
            String output = null;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                result = output;
            }
        } catch (ClientProtocolException cpe) {
            System.err.println(cpe);
        } catch (IOException ioe) {
            System.err.println(ioe);
        } catch (Exception e) {
            System.err.println(e);
        }

        System.out.println("\n===============================================");

        return result;
    }

}
