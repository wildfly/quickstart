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