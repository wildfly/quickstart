/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
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

    /**
     * Persit the {@link Contact}
     * 
     * @param contact
     */
    public void persist(Contact contact) {
        entityManager.persist(contact);
        entityManager.flush();
    }

    /**
     * Removes the {@link Contact}
     * 
     * @param contact
     */
    public void remove(Contact contact) {
        entityManager.remove(contact);
        entityManager.flush();
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
