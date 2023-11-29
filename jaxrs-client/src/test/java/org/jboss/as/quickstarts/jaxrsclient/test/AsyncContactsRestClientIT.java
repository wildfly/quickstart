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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;

/**
 * Tests asynchronous client communications.
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public class AsyncContactsRestClientIT extends AbstractContactsRestClientIT {
    private static final long TIMEOUT = 5;

    @Override
    protected Contact addContact() throws Exception {
        final Future<Contact> future = CLIENT.target(resolveBaseUrl()).request()
                // Use an AsyncInvoker
                .async().post(Entity.json(createContact()), Contact.class);
        // Get the value from the Future
        return future.get(TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    protected Contact getContact(final long id) throws Exception {
        final Future<Contact> furture = CLIENT.target(resolveBaseUrl().path(Long.toString(id)))
                .request(MediaType.APPLICATION_JSON_TYPE)
                // Use an AsyncInvoker
                .async()
                .get(Contact.class);
        // Get the value from the Future
        return furture.get(TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    protected List<Contact> getContacts() throws Exception {
        final GenericType<List<Contact>> contactsListType = new GenericType<>() {
        };
        final Future<List<Contact>> future = CLIENT.target(resolveBaseUrl()).request()
                // Use an AsyncInvoker
                .async().get(contactsListType);
        // Get the value from the Future
        return future.get(TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    protected Response deleteContact(final long id) throws Exception {
        final Future<Response> future = CLIENT.target(resolveBaseUrl().path(Long.toString(id))).request()
                // Use an AsyncInvoker
                .async().delete();
        // Get the value from the Future
        return future.get(TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    protected Response deleteContacts() throws Exception {
        final Future<Response> future = CLIENT.target(resolveBaseUrl()).request()
                // Use an AsyncInvoker
                .async().delete();
        // Get the value from the Future
        return future.get(TIMEOUT, TimeUnit.SECONDS);
    }
}
