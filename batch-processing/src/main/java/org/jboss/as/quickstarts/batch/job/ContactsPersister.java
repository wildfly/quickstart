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

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.batch.model.Contact;

//This class will persist the contacts chunk at the database. It cleans the database if it's the first execution (no checkpoint)
@Named("contactsPersister")
public class ContactsPersister extends AbstractItemWriter {

    @Inject
    private EntityManager entityManager;

    @Inject
    private Logger log;

    private Boolean hasCheckPoint;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint == null) {
            log.info("No checkpoint detected. Cleaning the Database");
            entityManager.createQuery("DELETE FROM Contact c").executeUpdate();
            hasCheckPoint = true;
        }

    }

    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            Contact c = (Contact) items.get(i);
            entityManager.persist(c);
            entityManager.flush();
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return hasCheckPoint;
    }

}
