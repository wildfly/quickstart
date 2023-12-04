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
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;

/**
 * Tests client invocation using the {@link Invocation} API.
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class InvocationContactsRestClientIT extends AbstractContactsRestClientIT {

    @Override
    protected Contact addContact() {
        final Invocation invocation = CLIENT.target(resolveBaseUrl()).request()
                .buildPost(Entity.json(createContact()));
        return invocation.invoke(Contact.class);
    }

    @Override
    protected Contact getContact(final long id) {
        final Invocation invocation = CLIENT.target(resolveBaseUrl().path(Long.toString(id)))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet();
        return invocation.invoke(Contact.class);
    }

    @Override
    protected List<Contact> getContacts() {
        final GenericType<List<Contact>> contactsListType = new GenericType<>() {
        };
        final Invocation invocation = CLIENT.target(resolveBaseUrl()).request()
                .buildGet();
        return invocation.invoke(contactsListType);
    }

    @Override
    protected Response deleteContact(final long id) {
        final Invocation invocation = CLIENT.target(resolveBaseUrl().path(Long.toString(id))).request()
                .buildDelete();
        return invocation.invoke();
    }

    @Override
    protected Response deleteContacts() {
        final Invocation invocation = CLIENT.target(resolveBaseUrl()).request()
                .buildDelete();
        return invocation.invoke();
    }
}
