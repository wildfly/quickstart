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
package org.jboss.as.quickstarts.loggingToolsQS.loggers;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "GTRDATES")
public interface DateLogger extends BasicLogger {
    DateLogger LOGGER = Logger.getMessageLogger(DateLogger.class, DateLogger.class.getPackage().getName());

    @LogMessage(level = Level.ERROR)
    @Message(id = 3, value = "Invalid date passed as string: %s")
    void logStringCouldntParseAsDate(String dateString, @Cause Throwable exception);

    @LogMessage
    @Message(id = 4, value = "Requested number of days until '%s'")
    void logDaysUntilRequest(String dateString);

}
