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
package org.jboss.as.quickstarts.loggingToolsQS.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 *  Wrapper class for WebApplicationException.  WebApplicationException doesn't
 *  use the `String message` parameter from Throwable which is required for the passing
 *  of the translated message.
 *
 *    This class simply provides a constructor with String message which is passed along
 *    to the super class constructor as part of it's Response object.
 *
 *    http://docs.oracle.com/javaee/6/api/javax/ws/rs/WebApplicationException.html
 *
 */

@SuppressWarnings("serial")
public class LocaleInvalidException extends WebApplicationException {
    public LocaleInvalidException(String message) {
        super(Response.status(404).entity(message).type(MediaType.TEXT_PLAIN).build());

    }
}
