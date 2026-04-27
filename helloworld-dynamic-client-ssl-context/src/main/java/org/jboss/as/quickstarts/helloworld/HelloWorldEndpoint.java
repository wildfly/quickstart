/*
 * JBoss, Home of Professional Open Source
 * Copyright 2026, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.helloworld;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * This class uses RESTEasy client that is configured with default SSL Context.
 * Server in this example is configured with dynamic ssl context, so it will be dynamically switched according to the peer's hostname and port.
 */
@Path("/")
public class HelloWorldEndpoint {

    private ResteasyClientBuilder builder = (ResteasyClientBuilder) ClientBuilder.newBuilder();
    private ResteasyClient client = builder.hostnameVerifier((s, sslSession) -> true).sslContext(SSLContext.getDefault()).build(); // resteasy client must set default ssl context otherwise it uses null

    public HelloWorldEndpoint() throws NoSuchAlgorithmException {
    }

    @GET
    @Path("/port9443request")
    @Produces(MediaType.TEXT_HTML)
    public String pingFirstServer() {
        Response response = client.target("https://127.0.0.1:9443/helloworld-dynamic-client-ssl-context/").request().get();
        return "HTTP status of result is " +  response.getStatus();
    }

    @GET
    @Path("/port10443request")
    @Produces(MediaType.TEXT_HTML)
    public String pingSecondServer() throws IOException {
        Response response = client.target("https://127.0.0.1:10443/helloworld-dynamic-client-ssl-context/").request().get();
        return "HTTP status of result is " +  response.getStatus();
    }
}
