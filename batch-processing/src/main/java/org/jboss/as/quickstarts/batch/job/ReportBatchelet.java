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
package org.jboss.as.quickstarts.batch.job;

import java.util.logging.Logger;

import javax.batch.api.Batchlet;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

//This batchlet just reports how many records was imported to database
@Named("reportBatchelet")
public class ReportBatchelet implements Batchlet {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger log;

    @Override
    public String process() throws Exception {
        long contacts = (long) entityManager.createQuery("SELECT COUNT(c) FROM Contact c").getSingleResult();
        log.info("Imported " + contacts + " contacts into the database.");
        return "END";
    }

    @Override
    public void stop() throws Exception {

    }

}
