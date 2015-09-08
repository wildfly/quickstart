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

import org.jboss.as.quickstarts.cdi.interceptor.ServiceStereotype;

/**
 * This is the alternative implementation of ItemService interface. @ServiceStereotype says that this bean is @Alternative and @Logging
 * and @Audit interceptor bindings is applied. Interceptors are applied to all business methods, since @ServiceStereotype
 * specified at class level. This implementation with interceptors is used only if alternative stereotype and interceptors are
 * specified in beans.xml descriptor.
 *
 * @author Ievgen Shulga
 */
@Stateless
@ServiceStereotype
public class ItemAlternativeServiceBean implements ItemService {

    @Inject
    private EntityManager em;

    public void create(Item item) {
        em.persist(item);
    }

    public List<Item> getList() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
