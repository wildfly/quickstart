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
package org.jboss.as.quickstarts.helloworld.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HelloWorldJaxRsClient {

    private static final String REST_TARGET_URL = "http://localhost:8080/helloworld-jaxrs/rest/";

    /**
     * Responses of the RESTful web service
     */
    private static final String XML_RESPONSE = "<xml><result>Hello World!</result></xml>";
    private static final String JSON_RESPONSE = "{\"result\":\"Hello World!\"}";
    private static final String XML_RESPONSE_GREETING = "<xml><result>%s World!</result></xml>";
    private static final String JSON_RESPONSE_GREETING = "{\"result\":\"%s World!\"}";

    public static void main(String[] args) throws Exception {
        HelloWorldJaxRsClient client = new HelloWorldJaxRsClient();
        client.testGet();
        client.testPost();
    }

    String getRequestUrl(String verb, String format) {
        return REST_TARGET_URL + verb + "/" + format;
    }

    /**
     * Test method that invokes GET resource methods on HelloWorldJaxRsServer.
     */
    public void testGet() {
        String response = runRequestGet(getRequestUrl("get", "xml"), MediaType.APPLICATION_XML_TYPE);
        if (!XML_RESPONSE.equals(response)) {
            throw new RuntimeException("Response is wrong:\nXML Response:" + response + "\nshould be: " + XML_RESPONSE);
        }

        response = runRequestGet(getRequestUrl("get", "json"), MediaType.APPLICATION_JSON_TYPE);
        if (!JSON_RESPONSE.equals(response)) {
            throw new RuntimeException("Response is wrong:\nJSON Response:" + response + "\nshould be: " + JSON_RESPONSE);
        }
    }

    /**
     * Test method that invokes POST resource methods on HelloWorldJaxRsServer.
     */
    public void testPost() {
        String response = runRequestPost(getRequestUrl("post", "xml"), MediaType.APPLICATION_XML_TYPE, "Hola");
        if (!String.format(XML_RESPONSE_GREETING, "Hola").equals(response)) {
            throw new RuntimeException("Response is wrong:\nXML Response:" + response + "\nshould be: " + String.format(XML_RESPONSE_GREETING, "Hola"));
        }

        response = runRequestPost(getRequestUrl("post", "json"), MediaType.APPLICATION_JSON_TYPE, "Hola");
        if (!String.format(JSON_RESPONSE_GREETING, "Hola").equals(response)) {
            throw new RuntimeException("Response is wrong:\nJSON Response:" + response + "\nshould be: " + String.format(JSON_RESPONSE_GREETING, "Hola"));
        }
    }

    /**
     * The purpose of this method is to run the external REST request on GET resource methods.
     *
     * @param url       The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private String runRequestGet(String url, MediaType mediaType) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());


        // Using the RESTEasy libraries, initiate a client request
        Client client = ClientBuilder.newClient();

        // Set url as target
        WebTarget target = client.target(url);

        // Be sure to set the mediatype of the request
        target.request(mediaType);

        // Request has been made, now let's get the response
        Response response = target.request().get();
        result = response.readEntity(String.class);
        response.close();

        // Check the HTTP status of the request
        // HTTP 200 indicates the request is OK
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
        }

        // We have a good response, let's now read it
        System.out.println("\n*** Response from Server ***\n");
        System.out.println(result);
        System.out.println("\n===============================================");

        return result;
    }

    /**
     * The purpose of this method is to run the external REST request on POST resource methods.
     *
     * @param url       The url of the RESTful service
     * @param mediaType The mediatype of the RESTful service
     */
    private String runRequestPost(String url, MediaType mediaType, String greeting) {
        String result = null;

        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());


        // Using the RESTEasy libraries, initiate a client request
        Client client = ClientBuilder.newClient();

        // Set url as target
        WebTarget target = client.target(url);

        // Be sure to set the mediatype of the request
        target.request(mediaType);

        // Create the entity parameter for the POST resource method and make the invocation
        Response response = target.request().post(Entity.entity(greeting, MediaType.TEXT_PLAIN_TYPE));

        // Request has been made, now let's get the response
        result = response.readEntity(String.class);
        response.close();

        // Check the HTTP status of the request
        // HTTP 200 indicates the request is OK
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed request with HTTP status: " + response.getStatus());
        }

        // We have a good response, let's now read it
        System.out.println("\n*** Response from Server ***\n");
        System.out.println(result);
        System.out.println("\n===============================================");

        return result;
    }
}
