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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.spi.LoggableFailure;

import javax.ws.rs.WebApplicationException;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the contacts table.
 *
 * @author Joshua Wilson
 *
 */
/*
 * The Path annotation defines this as a REST Web Service using JAX-RS.
 *
 * By placing the Consumes and Produces annotations at the class level the methods all default to JSON.  However, they
 * can be overriden by adding the Consumes or Produces annotations to the individual method.
 *
 * It is Stateless to "inform the container that this RESTful web service should also be treated as an EJB and allow
 * transaction demarcation when accessing the database." - Antonio Goncalves
 *
 */
@Path("/contacts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class ContactRESTService {
    @Inject
    private Logger log;

    @Inject
    private ContactService service;

    @Context
    private UriInfo uriInfo;

    /**
     * Search for and return all the Contacts.  They are sorted alphabetically by name.
     *
     * @return List of Contacts
     */
    @GET
    public Response retrieveAllContacts() {
        List<Contact> contacts = service.findAllOrderedByName();
        return Response.ok(contacts).build();
    }

    /**
     * Search for and return all the Contacts.  They are sorted alphabetically by name.
     *
     * @return List of Contacts
     */
    @GET
    @Path("/{email}")
    public Response retrieveContactsByEmail(@PathParam("email") String email) {
        Contact contact = service.findByEmail(email);
        if (contact == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(contact).build();
    }

    /**
     * Search for just one Contact by it's ID.
     *
     * @param ID of the Contact
     * @return Response
     */
    @GET
    @Path("/{id:[0-9][0-9]*}")
    public Response retrieveContactById(@PathParam("id") long id) {
        Contact contact = service.findById(id);
        if (contact == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
            + contact.getBirthDate() + " " + contact.getId());

        return Response.ok(contact).build();
    }

    /**
     * Creates a new contact from the values provided. Performs validation and will return a JAX-RS response with either 200 (ok)
     * or with a map of fields, and related errors.
     *
     * @param Contact
     * @return Response
     */
    @SuppressWarnings("unused")
    @POST
    public Response createContact(Contact contact) {
        log.info("createContact started. Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
            + contact.getBirthDate() + " " + contact.getId());
        if (contact == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder = null;

        try {
            // Go add the new Contact.
            Contact created = service.create(contact);

            // Construct a location of created contact.
            URI location = null;
            try {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                uriBuilder.path(Long.toString(created.getId()));
                location = uriBuilder.build();
            } catch (LoggableFailure lf) {
                // IGNORED: UriInfo methods throw this if called outside the scope of request,
                // that happens in ContactRegistrationTest.testRegister method.
            }

            // Create an CREATED Response and pass the URI location back in case it is needed.
            builder = Response.created(location);

            log.info("createContact completed. Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
                + contact.getBirthDate() + " " + contact.getId());
        } catch (ConstraintViolationException ce) {
            log.info("ConstraintViolationException - " + ce.toString());
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            log.info("ValidationException - " + e.toString());
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Updates a contact with the ID provided in the Contact. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     *
     * @param Contact
     * @return Response
     */
    @PUT
    @Path("/{id:[0-9][0-9]*}")
    public Response updateContact(@PathParam("id") long id, Contact contact) {
        if (contact == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        log.info("updateContact started. Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
            + contact.getBirthDate() + " " + contact.getId());

        if (contact.getId() != id) {
            // The client attempted to update the read-only Id. This is not permitted.
            Response response = Response.status(Response.Status.CONFLICT).entity("The contact ID cannot be modified").build();
            throw new WebApplicationException(response);
        }
        if (service.findById(contact.getId()) == null) {
            // Verify if the contact exists. Return 404, if not present.
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder = null;

        try {
            // Apply the changes the Contact.
            service.update(contact);

            // Create an OK Response and pass the contact back in case it is needed.
            builder = Response.ok(contact);

            log.info("updateContact completed. Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
                + contact.getBirthDate() + " " + contact.getId());
        } catch (ConstraintViolationException ce) {
            log.info("ConstraintViolationException - " + ce.toString());
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            log.info("ValidationException - " + e.toString());
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            responseObj.put("error", "This is where errors are displayed that are not related to a specific field");
            responseObj.put("anotherError", "You can find this error message in /src/main/java/org/jboss/quickstarts/contact/ContactRESTService.java line 242.");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Deletes a contact using the ID provided. If the ID is not present then nothing can be deleted, and will return a
     * JAX-RS response with either 200 OK or with a map of fields, and related errors.
     *
     * @param Contact
     * @return Response
     */
    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteContact(@PathParam("id") Long id) {
        log.info("deleteContact started. Contact ID = " + id);
        Response.ResponseBuilder builder = null;

        try {
            Contact contact = service.findById(id);
            if (contact != null) {
                service.delete(contact);
            } else {
                log.info("ContactRESTService - deleteContact - No contact with matching ID was found so can't Delete.");
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }

            builder = Response.noContent();
            log.info("deleteContact completed. Contact = " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail() + " " + contact.getPhoneNumber() + " "
                + contact.getBirthDate() + " " + contact.getId());
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

}
