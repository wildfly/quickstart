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
package org.jboss.as.quickstarts.threadracing.results;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * A stateless EJB used to manage the storage of race results, through JPA.
 *
 * The JPA PU deployed with the app (see src/main/resources/META-INF/persistence.xml) is used to persist race's results, and has the particularity of not declaring the datasource it targets, and in such case the Jakarta EE 7 introduced default datasource is used.
 * @author Eduardo Martins
 */
@Stateless
public class RaceResults {

    /**
     * the injected JPA entity manager, which may be used to interact with race result's PU
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Adds a race result.
     * @param e
     */
    public void add(RaceResult e) {
        em.persist(e);
    }

    /**
     * Finds all race results.
     * @return
     */
    public List<RaceResult> findAll() {
        return em.createNamedQuery("RaceResult.findAll", RaceResult.class).getResultList();
    }
}
