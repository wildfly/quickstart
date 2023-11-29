/*
 * JBoss, Home of Professional Open Source
 * Copyright 2022, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.jaxrsclient.test;

import java.util.List;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractContactsRestClientIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/jaxrs-client";

    private static final String CONTACT_NAME = "New Contact";
    private static final String CONTACT_PHONE = "+55-61-5555-1234";

    static Client CLIENT;

    private final boolean checkSavedBy;

    protected AbstractContactsRestClientIT() {
        this(false);
    }

    protected AbstractContactsRestClientIT(final boolean checkSavedBy) {
        this.checkSavedBy = checkSavedBy;
    }

    @BeforeAll
    public static void createClient() {
        CLIENT = ClientBuilder.newClient();
    }

    @AfterAll
    public static void closeClient() {
        if (CLIENT != null) CLIENT.close();
    }

    @Test
    @Order(1)
    public void testAddContact() throws Exception {
        Contact contact = addContact();
        Assertions.assertEquals((Long) 1L, contact.getId(), "A contact should be persisted with Id=1!");
        if (checkSavedBy) {
            Assertions.assertEquals(SavedByClientRequestFilter.USERNAME, contact.getSavedBy());
        }
    }

    @Test
    @Order(2)
    public void testGetContact() throws Exception {
        final Contact contact = getContact(1L);
        Assertions.assertEquals((Long) 1L, contact.getId(), "Fetched contact with Id=1!");
        Assertions.assertEquals(CONTACT_NAME, contact.getName(), "Fetched contact with equal name");
        Assertions.assertEquals(CONTACT_PHONE, contact.getPhoneNumber(), "Fetched contact with equal phone");
        if (checkSavedBy) {
            Assertions.assertEquals(SavedByClientRequestFilter.USERNAME, contact.getSavedBy());
        }
    }

    @Test
    @Order(3)
    public void testDeleteContact() throws Exception {
        try (Response response = deleteContact(1L)) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), "Contact 1 should be dropped");
        }
        // There should be no more contacts left
        final List<Contact> contacts = getContacts();
        Assertions.assertTrue(contacts.isEmpty(), () -> String.format("Expected no contacts, but found: %s", contacts));
    }

    @Test
    @Order(4)
    public void testGetAll() throws Exception {
        // Add two contacts and we should end up with two entries
        addContact();
        addContact();
        final List<Contact> contacts = getContacts();
        Assertions.assertFalse(contacts.isEmpty(), "Expected contacts, not none were found");
        final Contact contact = contacts.get(0);
        Assertions.assertEquals((Long) 1L, contact.getId(), "Fetched contact with Id=1!");
        Assertions.assertEquals(CONTACT_NAME, contact.getName(), "Fetched contact with equal name");
        Assertions.assertEquals(CONTACT_PHONE, contact.getPhoneNumber(), "Fetched contact with equal phone");
        if (checkSavedBy) {
            Assertions.assertEquals(SavedByClientRequestFilter.USERNAME, contact.getSavedBy());
        }
    }

    @Test
    @Order(5)
    public void testDeleteAll() throws Exception {
        // Get the current contacts just to ensure we have something to delete
        Assertions.assertFalse(getContacts().isEmpty(), "Expected contacts, not none were found. There is nothing to verify deletion.");

        try (Response response = deleteContacts()) {
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), "All contacts should be dropped");
        }
        // There should be no more contacts left
        final List<Contact> contacts = getContacts();
        Assertions.assertTrue(contacts.isEmpty(), () -> String.format("Expected no contacts, but found: %s", contacts));
    }

    protected abstract Contact addContact() throws Exception;

    protected abstract Contact getContact(final long id) throws Exception;

    protected abstract List<Contact> getContacts() throws Exception;

    protected abstract Response deleteContact(final long id) throws Exception;

    protected abstract Response deleteContacts() throws Exception;

    final Contact createContact() {
        final Contact contact = new Contact();
        contact.setName(CONTACT_NAME);
        contact.setPhoneNumber(CONTACT_PHONE);
        return contact;
    }

    static UriBuilder resolveBaseUrl() {
        final String baseUrl = resolveServerHost();
        return UriBuilder.fromUri(baseUrl).path("rest/contacts");
    }

    static String resolveServerHost() {
        final String result = System.getenv("SERVER_HOST");
        return result != null ? result : System.getProperty("server.host", DEFAULT_SERVER_HOST);
    }
}
