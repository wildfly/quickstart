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
package org.jboss.as.quickstarts.servlet.async;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.servlet.AsyncContext;

/**
 * A simple service to simulate the execution of a long running task.
 *
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Stateless
public class LongRunningService {

    private final Logger logger = Logger.getLogger(LongRunningService.class.getName());

    /**
     * The use of {@link Asynchronous} causes this EJB method to be executed asynchronously, by a different thread from a
     * dedicated, container managed thread pool.
     *
     * @param asyncContext the context for a suspended Servlet request that this EJB will complete later.
     */
    @Asynchronous
    public void readData(AsyncContext asyncContext) {
        try {
            // This is just to simulate a long running operation for demonstration purposes.
            Thread.sleep(5000);

            PrintWriter writer = asyncContext.getResponse().getWriter();
            writer.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            writer.close();

            asyncContext.complete();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
