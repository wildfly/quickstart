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
package org.jboss.as.quickstarts.ejb.asynchronous;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 * <p>
 * A simple SLSB to demonstrate the asynchronous EJB call.
 * </p>
 * <p>
 * Here the methods are annotated but it is also possible to add the annotation at class level, in that case all methods of this
 * bean are asynchrounous.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class AsynchronousAccessBean implements AsynchronousAccess, AnotherAsynchronousAccess {
    private static final Logger LOGGER = Logger.getLogger(AsynchronousAccessBean.class.getName());

    @Asynchronous
    @Override
    public void fireAndForget(long sleepTime) {
        LOGGER.info("'fireAndForget' Will wait for " + sleepTime + "ms");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        LOGGER.info("action 'fireAndForget' finished");
    }

    @Asynchronous
    @Override
    public Future<String> longerRunning(long sleepTime) {
        LOGGER.info("Will wait for " + sleepTime + "ms");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        LOGGER.info("returning the result");
        return new AsyncResult<>("returning at " + new Date() + ", duration was " + sleepTime + "ms");
    }

    @Asynchronous
    @Override
    public Future<String> failure() throws IllegalAccessException {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
        }
        throw new IllegalAccessException("Asynchrounous fail demonstration");
    }

    @Override
    public Future<String> interfaceAsync(long sleepTime) {
        LOGGER.info("Will wait for " + sleepTime + "ms");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
        LOGGER.info("returning the result");
        return new AsyncResult<>("returning at " + new Date() + ", duration was " + sleepTime + "ms");
    }
}
