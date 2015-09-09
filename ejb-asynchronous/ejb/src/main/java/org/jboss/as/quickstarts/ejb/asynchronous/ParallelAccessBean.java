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

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Stateless session bean implementation to demonstrate how to invoke asynchronous methods to parallelize different actions to
 * minimize the duration time of client invocation.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class ParallelAccessBean implements ParallelAccess {
    private static final Logger LOGGER = Logger.getLogger(ParallelAccessBean.class.getName());

    @EJB
    private AsynchronousAccess asyncBean;
    @EJB
    private AnotherAsynchronousAccess anotherBean;

    @Override
    public Collection<String> invokeAsyncParallel() {
        List<String> results = new ArrayList<>();
        Future<String> call1 = asyncBean.longerRunning(5000);
        Future<String> call2 = asyncBean.longerRunning(3000);

        try {
            results.add(call1.get());
            results.add(call2.get());
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Asynchronous call was interrupted!", e);
            throw new RuntimeException("Asynchronous call was interrupted!", e);
        } catch (ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Asynchronous call failed!", e);
            throw new RuntimeException("Asynchronous call failed!", e);
        }
        return results;
    }

    @Override
    public void callInterfaceAnnotatedMethod() {
        // call the example method with interface annotation
        final Future<String> call1 = anotherBean.interfaceAsync(500);

        try {
            // wait for the result and log it
            LOGGER.info("CALL result " + call1.get());
        } catch (Exception e) {
            throw new RuntimeException("Asynchronous (interface annotated) method can not be called!", e);
        }
    }
}
