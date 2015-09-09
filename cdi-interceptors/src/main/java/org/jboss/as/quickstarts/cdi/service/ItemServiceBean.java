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
package org.jboss.as.quickstarts.cdi.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.cdi.interceptor.Audit;
import org.jboss.as.quickstarts.cdi.interceptor.Logging;

/**
 * Methods of ItemServiceBean marked with annotations, which specify that an annotation type is an interceptor binding type.
 * CDI-interceptors are disabled by default, so must be added to WEB-INF/beans.xml, also the order of execution can be specified
 * in descriptor.
 *
 * @author Ievgen Shulga
 */
@Stateless
public class ItemServiceBean {

    @Inject
    private EntityManager em;

    @Audit
    @Logging
    public void create(Item item) {
        em.persist(item);
    }

    @Audit
    @Logging
    public List<Item> getList() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
