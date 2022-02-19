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
package org.jboss.as.quickstarts.helloworld.server;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * A simple JAX-RS resource class
 *
 */
@Path("")
public class HelloWorldJaxRsServer {

    @Inject
    HelloService helloService;

    @GET
    @Path("/get/json")
    @Produces({ "application/json" })
    public String getHelloWorldJSON() {
        return "{\"result\":\"" + helloService.createHelloMessage("World") + "\"}";
    }

    @GET
    @Path("/get/xml")
    @Produces({ "application/xml" })
    public String getHelloWorldXML() {
        return "<xml><result>" + helloService.createHelloMessage("World") + "</result></xml>";
    }

    @POST
    @Path("/post/json")
    @Produces({ "application/json" })
    public String postHelloWorldJSON(String greeting) {
        return "{\"result\":\"" + greeting + " World!" + "\"}";
    }

    @POST
    @Path("/post/xml")
    @Produces({ "application/xml" })
    public String postHelloWorldXML(String greeting) {
        return "<xml><result>" + greeting + " World!</result></xml>";
    }
}
