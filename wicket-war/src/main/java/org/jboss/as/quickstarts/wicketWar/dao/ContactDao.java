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

import javax.ejb.Local;

import org.jboss.as.quickstarts.wicketWar.model.Contact;

/**
 *
 * @author Filippo Diotalevi
 */
@Local
public interface ContactDao {

    /**
     * Returns the currently available contacts.
     *
     * @return every contact in the database
     */
    public List<Contact> getContacts();

    /**
     * Returns a specific Contact from DB.
     *
     * @param id The Id for the Contact
     * @return The specified Contact object
     */
    public Contact getContact(Long id);

    /**
     * Persist a new Contact in the DB.
     *
     * @param name The name of the new Contact
     * @param email The e-mail address of the new Contact
     */
    public void addContact(String name, String email);

    /**
     * Removes a specific item from the DB.
     *
     * @param modelObject The specific Contact object, which we wants to remove
     */
    public void remove(Contact modelObject);
}
