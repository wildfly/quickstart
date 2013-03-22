/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.wicketWar.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.as.quickstarts.wicketWar.model.Contact;

/**
 * A bean which manages Contact entities.
 */
@Stateless
public class ContactDaoBean implements ContactDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    @SuppressWarnings("unchecked")
    public List<Contact> getContacts() {
        return em.createQuery("SELECT c FROM Contact c").getResultList();
    }

    /**
     * Get Contact by ID.
     */
    @Override
    public Contact getContact(Long id) {
        return em.find(Contact.class, id);
    }

    /**
     * Add a new Contact.
     */
    @Override
    public void addContact(String name, String email) {
        em.merge(new Contact(null, name, email));
    }

    /**
     * Remove a Contact.
     */
    @Override
    public void remove(Contact modelObject) {
        Contact managed = em.merge(modelObject);
        em.remove(managed);
        em.flush();
    }
    
}
