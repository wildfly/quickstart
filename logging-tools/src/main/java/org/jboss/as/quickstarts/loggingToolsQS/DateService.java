/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.DateExceptionsBundle;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.DateLogger;

/**
 * A simple REST service which returns the number of days until a date and provides localised logging of the activity
 * 
 * @author dmison@me.com
 * 
 */

@Path("dates")
public class DateService {

    @GET
    @Path("daysuntil/{targetdate}")
    public int showDaysUntil(@PathParam("targetdate") String targetdate) {
        DateLogger.LOGGER.logDaysUntilRequest(targetdate);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date target = null;
        Date now = new Date();

        float days = 0;

        try {
            df.setLenient(false);               //make sure no invalid dates sneak through
            target = df.parse(targetdate);
            days = (float) target.getTime() - now.getTime();
            days = days / (1000 * 60 * 60 * 24); // turn milliseconds into days
        } catch (ParseException ex) {
            // ** DISCLAIMER **
            // This example is contrived and overly verbose for the purposes of showing the
            // different logging methods. It's generally not recommended to recreate exceptions
            // or log exceptions that are being thrown.

            // create localized ParseException using method from bundle with details from ex
            ParseException nex = DateExceptionsBundle.EXCEPTIONS.targetDateStringDidntParse(targetdate, ex.getErrorOffset());

            // log a message using nex as the cause
            DateLogger.LOGGER.logStringCouldntParseAsDate(targetdate, nex);

            // throw a WebApplicationException (400) with the localized message from nex
            throw new WebApplicationException(Response.status(400).entity(nex.getLocalizedMessage()).type(MediaType.TEXT_PLAIN)
                    .build());
        }

        return Math.round(days);
    }

}
