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

package org.jboss.as.quickstarts.loggingToolsQS.loggers;

import java.text.ParseException;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.Message;

@org.jboss.logging.MessageLogger(projectCode = "GTRDATES")
public interface DateLogger extends BasicLogger {
    DateLogger LOGGER = Logger.getMessageLogger(DateLogger.class, DateLogger.class.getPackage().getName());

    @LogMessage(level = Level.ERROR)
    @Message(id = 3, value = "Invalid date passed as string: %s")
    void logStringCouldntParseAsDate(String datestring, @Cause ParseException exception);

    @LogMessage
    @Message(id = 4, value = "Requested number of days until '%s'")
    void logDaysUntilRequest(String dateString);

}