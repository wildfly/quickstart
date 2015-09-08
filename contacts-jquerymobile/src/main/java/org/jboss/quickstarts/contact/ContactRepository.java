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
package org.jboss.quickstarts.contact;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import java.util.List;
import java.util.logging.Logger;

/**
 * This is a Repository and connects the Service/Control layer with the Domain/Entity Object.  There are no access modifiers
 * on the methods making them 'package' scope.  They should only be accessed by a Service/Control object.
 *
 * @author Joshua Wilson
 *
 */
public class ContactRepository {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    /**
     * Find all the Contacts and sort them alphabetically by last name.
     *
     * @return List of Contacts
     */
    List<Contact> findAllOrderedByName() {
        TypedQuery<Contact> query = em.createNamedQuery(Contact.FIND_ALL, Contact.class);
        List<Contact> contacts = query.getResultList();
        return contacts;
    }

    /**
     * Find just one Contact by it's ID.
     *
     * @param id
     * @return Contact
     */
    Contact findById(Long id) {
        return em.find(Contact.class, id);
    }

    /**
     * Find just one Contact by the email that is passed in. If there is more then one, only the first will be returned.
     *
     * @param email
     * @return Contact
     */
    Contact findByEmail(String email) {
        TypedQuery<Contact> query = em.createNamedQuery(Contact.FIND_BY_EMAIL, Contact.class).setParameter("email", email);
        Contact contact = query.getSingleResult();
        return contact;
    }

    /**
     * Find just one Contact by the first name that is passed in. If there is more then one, only the first will be returned.
     *
     * @param firstName
     * @return Contact
     */
    Contact findByFirstName(String firstName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteria = cb.createQuery(Contact.class);
        Root<Contact> contact = criteria.from(Contact.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.firstName), firstName));
        criteria.select(contact).where(cb.equal(contact.get("firstName"), firstName));
        return em.createQuery(criteria).getSingleResult();
    }

    /**
     * Find just one Contact by the last name that is passed in. If there is more then one, only the first will be returned.
     *
     * @param lastName
     * @return Contact
     */
    Contact findByLastName(String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteria = cb.createQuery(Contact.class);
        Root<Contact> contact = criteria.from(Contact.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.lastName), lastName));
        criteria.select(contact).where(cb.equal(contact.get("lastName"), lastName));
        return em.createQuery(criteria).getSingleResult();
    }

    /**
     * Create a Contact and store it in the database.
     *
     * Persist takes an entity instance, adds it to the context and makes that instance managed (ie future updates
     * to the entity will be tracked)
     *
     * persist() will set the @GeneratedValue @Id for an object.
     *
     * @param Contact
     * @return Contact
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Contact create(Contact contact) throws ConstraintViolationException, ValidationException, Exception {
        log.info("ContactRepository.create() - Creating " + contact.getFirstName() + " " + contact.getLastName());

        // Write the contact to the database.
        em.persist(contact);

        return contact;
    }

    /**
     * Update a Contact in the database.
     *
     * Merge creates a new instance of your entity, copies the state from the supplied entity, and makes the new
     * copy managed. The instance you pass in will not be managed (any changes you make will not be part of the
     * transaction - unless you call merge again).
     *
     * merge() however must have an object with the @Id already generated.
     *
     * @param Contact
     * @return Contact
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    //    Map<String, Object> update(Contact contact) throws Exception {
    Contact update(Contact contact) throws ConstraintViolationException, ValidationException, Exception {
        log.info("ContactRepository.update() - Updating " + contact.getFirstName() + " " + contact.getLastName());

        // Either update the contact or add it if it can't be found.
        em.merge(contact);

        return contact;
    }

    /**
     * Delete a Contact in the database.
     *
     * @param Contact
     * @return Contact
     * @throws Exception
     */
    //    Map<String, Object> delete(Contact contact) throws Exception {
    Contact delete(Contact contact) throws Exception {
        log.info("ContactRepository.delete() - Deleting " + contact.getFirstName() + " " + contact.getLastName());

        if (contact.getId() != null) {
            /*
             * The Hibernate session (aka EntityManager's persistent context) is closed and invalidated after the commit(),
             * because it is bound to a transaction. The object goes into a detached status. If you open a new persistent
             * context, the object isn't known as in a persistent state in this new context, so you have to merge it.
             *
             * Merge sees that the object has a primary key (id), so it knows it is not new and must hit the database
             * to reattach it.
             *
             * Note, there is NO remove method which would just take a primary key (id) and a entity class as argument.
             * You first need an object in a persistent state to be able to delete it.
             *
             * Therefore we merge first and then we can remove it.
             */
            em.remove(em.merge(contact));

        } else {
            log.info("ContactRepository.delete() - No ID was found so can't Delete.");
        }

        return contact;
    }

}
