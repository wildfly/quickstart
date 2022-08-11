/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */

@ApplicationScoped
public class DatabaseBean {

    @PersistenceContext(unitName = "test")
    EntityManager em;

    @Transactional
    public void store(Object entry) {
        em.persist(entry);
    }

    public List<TimedEntry> loadAllTimedEntries() {
        TypedQuery<TimedEntry> query = em.createQuery("SELECT t from TimedEntry t", TimedEntry.class);
        List<TimedEntry> result = query.getResultList();
        return result;
    }
}