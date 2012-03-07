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

import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.as.quickstarts.loggingToolsQS.exceptions.GreeterExceptionBundle;
import org.jboss.as.quickstarts.loggingToolsQS.loggers.GreeterLogger;
import org.jboss.as.quickstarts.loggingToolsQS.messages.GreetingMessagesBundle;
import org.jboss.logging.Messages;

/**
 * A simple REST service which says hello in different languages and logs the activity using localised loggers created using
 * JBoss Logging Tools.
 *
 * @author dmison@me.com
 *
 */

@Path("greetings")
public class GreeterService {
    // ======================================================================
    // Hello "name"!
    @GET
    @Path("{name}")
    public String getHelloName(@PathParam("name") String name) {
        GreeterLogger.LOGGER.logHelloMessageSent();
        return GreetingMessagesBundle.MESSAGES.helloToYou(name);
    }

    // ======================================================================
    // Hello "name" in language
    @GET
    @Path("{locale}/{name}")
    public String getHelloNameForLocale(@PathParam("name") String name, @PathParam("locale") String locale) {
        String[] locale_parts = locale.split("-");
        Locale newLocale = null;

        switch (locale_parts.length) {
            case 1:
                newLocale = new Locale(locale_parts[0]);
                break;
            case 2:
                newLocale = new Locale(locale_parts[0], locale_parts[1]);
                break;
            case 3:
                newLocale = new Locale(locale_parts[0], locale_parts[1], locale_parts[2]);
                break;
            default:
                throw GreeterExceptionBundle.EXCEPTIONS.localeNotValid(locale);
        }

        GreetingMessagesBundle messages = Messages.getBundle(GreetingMessagesBundle.class, newLocale);
        GreeterLogger.LOGGER.logHelloMessageSentForLocale(locale);
        return messages.helloToYou(name);
    }

    // ======================================================================
    // this is a particularly contrived example.  It is here merely to demonstrate
    // the throwing of a localized exception with another exception as the cause. 
    @GET
    @Path("crashme")
    public String crashMe() throws Exception {
        int value = 0;

        try {
            value = 50 / 0;
        } catch (Exception ex) {
            throw GreeterExceptionBundle.EXCEPTIONS.thrownOnPurpose(ex);
        }

        return "value=" + value + " (if you are reading this, then something went wrong!)";
    }

}
