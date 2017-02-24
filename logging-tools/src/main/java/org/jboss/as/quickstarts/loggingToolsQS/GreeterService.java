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

import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

    @SuppressWarnings("unused")
    private int value;    // used to demonstrate exceptions

    // ======================================================================
    // Hello "name"!
    @GET
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHelloName(@PathParam("name") String name) throws Exception {
        // ======================================================================
        // The name "crashme" is a contrived example to demonstrate how to throw
        // a localized exception with another exception as the cause.
        if ("crashme".equals(name)) {
            try {
                value = 50 / 0;
            } catch (Exception ex) {
                throw GreeterExceptionBundle.EXCEPTIONS.thrownOnPurpose(ex);
            }
        }

        GreeterLogger.LOGGER.logHelloMessageSent();
        return GreetingMessagesBundle.MESSAGES.helloToYou(name);

    }

    // ======================================================================
    // Hello "name" in language
    @GET
    @Path("{locale}/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHelloNameForLocale(@PathParam("name") String name, @PathParam("locale") String locale) throws Exception {
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

        // ======================================================================
        // The localized name "crashme" is a alos contrived example to demonstrate how to throw
        // a localized exception with another exception as the cause.
        if ("crashme".equals(name)) {
            try {
                value = 50 / 0;
            } catch (Exception ex) {
                throw GreeterExceptionBundle.EXCEPTIONS.thrownOnPurpose(ex);
            }
        }
        GreetingMessagesBundle messages = Messages.getBundle(GreetingMessagesBundle.class, newLocale);
        GreeterLogger.LOGGER.logHelloMessageSentForLocale(locale);
        return messages.helloToYou(name);
    }

}
