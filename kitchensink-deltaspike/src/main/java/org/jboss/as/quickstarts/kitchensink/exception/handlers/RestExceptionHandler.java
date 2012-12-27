/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstarts.kitchensink.exception.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.annotation.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.jboss.as.quickstarts.kitchensink.exception.annotation.RestRequest;

/**
 * This handler handles exceptions and builds an error message using {@link Status} and {@link Response} entities
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@ExceptionHandler
public class RestExceptionHandler {

    @Inject
    private Logger log;

    public void handleGenericException(@Handles @RestRequest ExceptionEvent<Throwable> evt, ResponseBuilder builder) {
        // Handle generic exceptions
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("error", evt.getException().getMessage());
        builder.status(Response.Status.BAD_REQUEST).entity(responseObj);
        // Mark as handled
        evt.handled();
    }

    public void handleValidationException(@Handles @RestRequest ExceptionEvent<ValidationException> evt, ResponseBuilder builder) {
        // Handle the unique constrain violation
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("email", "Email taken");
        builder.status(Response.Status.CONFLICT).entity(responseObj);
        // Mark as handled
        evt.handled();
    }

    public void handleWebApplicationException(@Handles @RestRequest ExceptionEvent<WebApplicationException> evt,
            ResponseBuilder builder) {
        // Handle Web Application exceptions
        builder.status(evt.getException().getResponse().getStatus());
        // Mark as handled
        evt.handled();
    }

    public void handleConstraintViolationException(@Handles @RestRequest ExceptionEvent<ConstraintViolationException> evt,
            ResponseBuilder builder) {
        // Handle bean validation issues
        builder.status(Status.BAD_REQUEST).entity(createViolationEntity(evt.getException().getConstraintViolations()));
        // Mark as handled
        evt.handled();
    }

    private Map<String, String> createViolationEntity(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return responseObj;
    }

}
