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
package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.Contact;

/**
 * 
 * This class is responsible for dealing with the persistence of {@link Contact} entities
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
// This class is Stateful because we need to keep the EntityManager opened during the conversation scope.
@Stateful
@ConversationScoped
public class ContactRepository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Inject
    private Event<Contact> contactEvent;

    /**
     * Persit the {@link Contact}
     * 
     * @param contact
     */
    public void persist(Contact contact) {
        entityManager.persist(contact);
        entityManager.flush();
        contactEvent.fire(contact);
    }

    /**
     * Removes the {@link Contact}
     * 
     * @param contact
     */
    public void remove(Contact contact) {
        entityManager.remove(contact);
        entityManager.flush();
        contactEvent.fire(contact);
    }

    /**
     * Retrieve all {@link Contact}
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Contact> getAllContacts() {
        return entityManager.createQuery("SELECT m From Contact m").getResultList();
    }

}
