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

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;

/**
 * <p>
 * This exception mapper is registered with JAX-RS by the provider annotation. It
 * </p>
 * <p>
 * You can inject other CDI beans into it.
 * </p>
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Provider
public class DeltaSpikeExceptionMapper implements ExceptionMapper<Exception> {

    // Inject CDI Event
    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    @Inject
    private Instance<Response> response;

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
     */
    @Override
    public Response toResponse(Exception exception) {
        catchEvent.fire(new ExceptionToCatchEvent(exception, RestRequestLiteral.INSTANCE));
        return response.get();
    }

}
