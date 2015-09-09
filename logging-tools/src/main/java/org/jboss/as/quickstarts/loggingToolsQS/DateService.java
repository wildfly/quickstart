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
package org.jboss.as.quickstarts.loggingToolsQS;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.DateExceptionsBundle;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.DateLogger;

/**
 * A simple REST service which returns the number of days until a date and provides localised logging of the activity
 *
 * @author dmison@me.com
 */

@Path("dates")
public class DateService {

    @GET
    @Path("daysuntil/{targetdate}")
    @Produces(MediaType.TEXT_PLAIN)
    public long showDaysUntil(@PathParam("targetdate") String targetDate) {
        DateLogger.LOGGER.logDaysUntilRequest(targetDate);

        final long days;

        try {
            final LocalDate date = LocalDate.parse(targetDate, DateTimeFormatter.ISO_DATE);
            days = ChronoUnit.DAYS.between(LocalDate.now(), date);
        } catch (DateTimeParseException ex) {
            // ** DISCLAIMER **
            // This example is contrived and overly verbose for the purposes of showing the
            // different logging methods. It's generally not recommended to recreate exceptions
            // or log exceptions that are being thrown.

            // create localized ParseException using method from bundle with details from ex
            DateTimeParseException nex = DateExceptionsBundle.EXCEPTIONS.targetDateStringDidntParse(targetDate, ex.getParsedString(), ex.getErrorIndex());

            // log a message using nex as the cause
            DateLogger.LOGGER.logStringCouldntParseAsDate(targetDate, nex);

            // throw a WebApplicationException (400) with the localized message from nex
            throw new WebApplicationException(Response.status(400).entity(nex.getLocalizedMessage()).type(MediaType.TEXT_PLAIN)
                .build());
        }

        return days;
    }

}
