/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.helloworld.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
/**
 * A simple REST service which is able to say "Hello World!"
 *
 * @author Ashwin Mehendale
 * @author emartins
 */

@Path("/")
public class HelloWorld {

    @GET
    @Path("/HelloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello World!";
    }
}
