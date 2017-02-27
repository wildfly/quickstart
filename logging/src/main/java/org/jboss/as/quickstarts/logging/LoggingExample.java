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
package org.jboss.as.quickstarts.logging;

import org.jboss.logging.Logger;

public class LoggingExample {

    // a JBoss Logging 3 style level (FATAL, ERROR, WARN, INFO, DEBUG, TRACE), or a special level (OFF, ALL).
    private static Logger log = Logger.getLogger(LoggingExample.class.getName());

    static {
        logFatal();
        logError();
        logWarn();
        logInfo();
        logDebug();
        logTrace();
    }

    public static void logFatal() {
        if (log.isEnabled(Logger.Level.FATAL)) {
            log.fatal("THIS IS A FATAL MESSAGE");
        }
    }

    public static void logError() {
        if (log.isEnabled(Logger.Level.ERROR)) {
            log.error("THIS IS AN ERROR MESSAGE");
        }
    }

    public static void logWarn() {
        if (log.isEnabled(Logger.Level.WARN)) {
            log.warn("THIS IS A WARNING MESSAGE");
        }
    }

    public static void logInfo() {
        if (log.isEnabled(Logger.Level.INFO)) {
            log.info("THIS IS AN INFO MESSAGE");
        }
    }

    public static void logDebug() {
        if (log.isEnabled(Logger.Level.DEBUG)) {
            log.debug("THIS IS A DEBUG MESSAGE");
        }
    }

    public static void logTrace() {
        if (log.isEnabled(Logger.Level.TRACE)) {
            log.trace("THIS IS A TRACE MESSAGE");
        }
    }
}
