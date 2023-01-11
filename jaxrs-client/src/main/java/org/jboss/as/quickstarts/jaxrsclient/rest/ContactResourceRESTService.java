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
package org.jboss.as.quickstarts.jaxrsclient.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.as.quickstarts.jaxrsclient.model.Contact;

@Path("/contacts")
public class ContactResourceRESTService {

    private static Map<Long, Contact> contactsRepository = new HashMap<>();

    /**
     * Creates a new contact from the values provided and will return a JAX-RS response with either 200 ok, or 400 (BAD REQUEST)
     * in case of errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createContact(Contact contact) {

        Response.ResponseBuilder builder = null;
        Long nextId = contactsRepository.keySet().size() + 1L;
        try {
            // Store the contact
            contact.setId(nextId);
            contactsRepository.put(nextId, contact);

            // Create an "ok" response with the persisted contact
            builder = Response.ok(contact);
        } catch (Exception e) {
            // Handle generic exceptions
            builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }

        return builder.build();
    }

    // delete all contacts
    @DELETE
    public Response removeAllContacts() {
        contactsRepository.clear();
        return Response.ok().build();
    }

    // delete a specific contact
    @DELETE
    @Path("/{id}")
    public Response removeContact(final @PathParam("id") Long id) {
        contactsRepository.remove(id);
        return Response.ok().build();
    }

    // Fetch all contacts
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Collection<Contact> allcontacts = contactsRepository.values();
        return Response.ok(allcontacts).build();
    }

    // Fetch a specific contact
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(final @PathParam("id") Long id) {
        Contact contact = contactsRepository.get(id);
        return Response.ok(contact).build();
    }
}
