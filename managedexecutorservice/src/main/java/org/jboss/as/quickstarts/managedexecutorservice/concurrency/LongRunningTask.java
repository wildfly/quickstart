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
package org.jboss.as.quickstarts.managedexecutorservice.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.managedexecutorservice.model.Product;

//This class simulates a long running task that could take many seconds to be executed.
public class LongRunningTask implements Callable<Integer> {

    @Inject
    private Logger log;

    @Inject
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public Integer call() throws Exception {
        // here we simulate an access to Products listing
        List<Product> products = (List<Product>) entityManager.createQuery("SELECT p FROM Product p").getResultList();
        log.info("Starting a long running task");
        for (Product product : products) {
            log.info("Analysing " + product.getName());
            // We simulate now a long running task
            Thread.sleep(3000);
        }
        // after executing a long running task we return a result
        return (int) (Math.random() * 100);
    }

}
