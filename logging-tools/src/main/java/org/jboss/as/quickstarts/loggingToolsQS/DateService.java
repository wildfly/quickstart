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
