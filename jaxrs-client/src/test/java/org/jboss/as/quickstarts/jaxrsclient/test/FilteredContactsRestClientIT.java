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

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;

/**
 * Tests client invocations using a client filter to add the {@link Contact#setSavedBy(String)}.
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class FilteredContactsRestClientIT extends AbstractContactsRestClientIT {

    protected FilteredContactsRestClientIT() {
        super(true);
    }

    @Override
    protected Contact addContact() {
        return CLIENT.target(resolveBaseUrl())
                .register(SavedByClientRequestFilter.class)
                .request()
                .post(Entity.json(createContact()), Contact.class);
    }

    @Override
    protected Contact getContact(final long id) {
        return CLIENT.target(resolveBaseUrl().path(Long.toString(id)))
                .register(SavedByClientRequestFilter.class)
                .request()
                .get(Contact.class);
    }

    @Override
    protected List<Contact> getContacts() {
        final GenericType<List<Contact>> contactsListType = new GenericType<>() {
        };
        return CLIENT.target(resolveBaseUrl()).request().get(contactsListType);
    }

    @Override
    protected Response deleteContact(final long id) {
        return CLIENT.target(resolveBaseUrl().path(Long.toString(id))).request().delete();
    }

    @Override
    protected Response deleteContacts() {
        return CLIENT.target(resolveBaseUrl()).request().delete();
    }
}
