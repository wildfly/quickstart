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
package org.jboss.as.quickstarts.deltaspike.exceptionhandling.rest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * A request-scoped resource for customizing an REST error response from within an exception handler.
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 */
@RequestScoped
public class ResponseBuilderManager {

    private ResponseBuilder responseBuilder;

    private Response response;

    @Produces
    @RequestScoped
    public ResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }

    @Produces
    public Response buildResponse() {
        if (response == null) {
            // the builder is reset upon build()
            // therefore, we cache the response
            response = responseBuilder.build();
        }
        return response;
    }

    @PostConstruct
    public void initialize() {
        responseBuilder = Response.serverError();
    }
}