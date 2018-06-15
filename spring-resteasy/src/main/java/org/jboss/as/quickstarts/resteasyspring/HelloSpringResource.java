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
package org.jboss.as.quickstarts.resteasyspring;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Joshua Wilson
 *
 */
@Path("/")
public class HelloSpringResource {

    @Autowired
    GreetingBean greetingBean;

    /**
     * Create a default REST endpoint that directs the user to use the demonstration endpoints.
     *
     * @return html
     */
    @GET
    @Produces("text/html")
    public Response getDefault(@Context UriInfo uriInfo) {
        String baseURI = uriInfo.getBaseUri().toString();
        if (!baseURI.endsWith("/")) baseURI += '/';
        String msg = "Hello. <br> Please try <a href='"+baseURI+"hello?name=yourname'>spring-resteasy/hello?name=yourname</a>"
            + "<br> Or try <a href='"+baseURI+"basic'>spring-resteasy/basic</a>"
            + "<br> Or try <a href='"+baseURI+"queryParam?param=query'>spring-resteasy/queryParam?param=query</a>"
            + "<br> Or try <a href='"+baseURI+"matrixParam;param=matrix'>spring-resteasy/matrixParam;param=matrix</a>"
            + "<br> Or try <a href='"+baseURI+"uriParam/789'>spring-resteasy/uriParam/789</a>"
            + "<br> Or try <a href='"+baseURI+"locating/hello?name=yourname'>spring-resteasy/locating/hello?name=yourname</a>"
            + "<br> Or try <a href='"+baseURI+"locating/basic'>spring-resteasy/locating/basic</a>"
            + "<br> Or try <a href='"+baseURI+"locating/queryParam?param=query'>spring-resteasy/locating/queryParam?param=query</a>"
            + "<br> Or try <a href='"+baseURI+"locating/matrixParam;param=matrix'>spring-resteasy/locating/matrixParam;param=matrix</a>"
            + "<br> Or try <a href='"+baseURI+"locating/uriParam/789'>spring-resteasy/locating/uriParam/789</a>";
        System.out.println("getDefault()");
        return Response.ok(msg).build();
    }

    @GET
    @Path("hello")
    @Produces("text/plain")
    public Response sayHello(@QueryParam("name") String name) {
        String greetingMsg = greetingBean.greet(name);
        System.out.println("Sending greeing: " + greetingMsg);
        return Response.ok(greetingMsg).build();
    }

    @GET
    @Path("basic")
    @Produces("text/plain")
    public String getBasic() {
        System.out.println("getBasic()");
        return "basic";
    }

    @PUT
    @Path("basic")
    @Consumes("text/plain")
    public void putBasic(String body) {
        System.out.println(body);
    }

    @GET
    @Path("queryParam")
    @Produces("text/plain")
    public String getQueryParam(@QueryParam("param") String param) {
        return param;
    }

    @GET
    @Path("matrixParam")
    @Produces("text/plain")
    public String getMatrixParam(@MatrixParam("param") String param) {
        return param;
    }

    @GET
    @Path("uriParam/{param}")
    @Produces("text/plain")
    public int getUriParam(@PathParam("param") int param) {
        return param;
    }

}
