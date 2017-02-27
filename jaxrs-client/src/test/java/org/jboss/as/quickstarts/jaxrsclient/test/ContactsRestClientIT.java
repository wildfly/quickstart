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
package org.jboss.as.quickstarts.jaxrsclient.test;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;
import org.jboss.as.quickstarts.jaxrsclient.rest.JaxRsActivator;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class ContactsRestClientIT {

    //private static final String getRequestUrl() = "http://localhost:8080/jaxrs-client/rest/contacts";
    private static final String CONTACT_NAME = "New Contact";
    private static final String CONTACT_PHONE = "+55-61-5555-1234";

    private Logger log = Logger.getLogger(ContactsRestClientIT.class.getName());

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "managed-executor-service.war")
                .addPackage(JaxRsActivator.class.getPackage())
                .addPackage(Contact.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }

    private String getRequestUrl() {
        return new StringBuilder(deploymentUrl.toString())
                .append("rest/contacts")
                .toString();
    }


    // This test shows basic operations
    @Test
    public void cruedTest() {
        log.info("### CRUD tests ###");
        // 1 - drop all contacts
        log.info("dropping all contacts");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All contacts should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new contact
        log.info("creating a new contact");
        Contact c = new Contact();
        c.setName(CONTACT_NAME);
        c.setPhoneNumber(CONTACT_PHONE);
        Contact persistedContact = ClientBuilder.newClient().target(getRequestUrl()).request().post(Entity.entity(c, MediaType.APPLICATION_JSON), Contact.class);
        Assert.assertEquals("A book should be persisted with Id=1!", (Long) 1L, (Long) persistedContact.getId());

        // 3 - Fetch Contact by Id
        log.info("fetching a contact by id");
        Contact fetchContctById =
                ClientBuilder.newClient().target(getRequestUrl()).path("/{contactId}").resolveTemplate("contactId", persistedContact.getId()).request().get(Contact.class);
        Assert.assertEquals("Fetched book with Id=1!", (Long) 1L, (Long) fetchContctById.getId());
        Assert.assertEquals("Fetched book with equal name", CONTACT_NAME, fetchContctById.getName());
        Assert.assertEquals("Fetched book with equal phone", CONTACT_PHONE, fetchContctById.getPhoneNumber());

        // 4 - Fetch all Contacts
        log.info("fetching all contacts");
        GenericType<List<Contact>> contactsListType = new GenericType<List<Contact>>() {
        };
        List<Contact> allContacts = ClientBuilder.newClient().target(getRequestUrl()).request().get(contactsListType);
        Assert.assertEquals("Should have a single contact", 1, allContacts.size());

        // 5 - Delete a Contact
        log.info("delete a contact by id");
        response = ClientBuilder.newClient().target(getRequestUrl()).path("/{contactId}").resolveTemplate("contactId", persistedContact.getId()).request().delete();
        Assert.assertEquals("Contact 1 should be dropped", Response.ok().build().getStatus(), response.getStatus());
    }

    // This test shows some basic operations using ASYNC invocations and java.util.concurrent.Future
    @Test
    public void asyncCrudTest() throws Exception {
        log.info("### CRUD tests  ASYNC ###");

        // 1 - drop all contacts ASYNC
        log.info("dropping all contacts ASYNC");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().async().delete().get();
        Assert.assertEquals("All contacts should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Contact ASYNC
        log.info("creating a new contact ASYNC");
        Contact c = new Contact();
        c.setName(CONTACT_NAME);
        c.setPhoneNumber(CONTACT_PHONE);

        Future<Contact> futureContact = ClientBuilder.newClient().target(getRequestUrl()).request().async().post(Entity.entity(c, MediaType.APPLICATION_JSON), Contact.class);

        Contact persistedContact = futureContact.get();
        Assert.assertEquals("A contact should be persisted with Id=1!", (Long) 1L, (Long) persistedContact.getId());

        // 3 - Delete a contact ASYNC
        log.info("delete a contact by id ASYNC");
        ClientBuilder.newClient().target(getRequestUrl()).path("{contactId}").resolveTemplate("contactId", persistedContact.getId()).request().async().delete().get();

        // 4 - Fetch All Contacts ASYNC
        log.info("fetching all contacts ASYNC");
        Future<List<Contact>> futureContacts = ClientBuilder.newClient().target(getRequestUrl()).request().async().get(new GenericType<List<Contact>>() {
        });
        List<Contact> allContacts = futureContacts.get();
        Assert.assertEquals("Should have no contacts", 0, allContacts.size());
    }

    // This test shows how to use javax.ws.rs.client.InvocationCallback
    @Test
    public void invocationCallBackTest() throws Exception {
        log.info("### Testing invocation callback ###");

        // 1 - drop all contacts
        log.info("dropping all contacts");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All contacts should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a InvocationCallback
        log.info("Creating a InvocationCallback");
        InvocationCallback<List<Contact>> invocationCallback = new InvocationCallback<List<Contact>>() {

            @Override
            public void completed(List<Contact> allContacts) {
                // Completed the invocation with no contact
                Assert.assertEquals("Should have no contacts", 0, allContacts.size());
            }

            @Override
            public void failed(Throwable throwable) {
                // It should fail
                Assert.fail(throwable.getMessage());

            }
        };
        // 3 - Invoke the service
        log.info("Invoking a service using the InvocationCallback");
        ClientBuilder.newClient().target(getRequestUrl()).request().async().get(invocationCallback).get();
    }

    // Shows how to use a delayed REST invocation
    @Test
    public void delayedInvocationTest() throws Exception {
        log.info("### Testing Delayed invocaton ###");

        // 1 - Drop all contacts
        log.info("dropping all contacts");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All contacts should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Contact Invocation
        log.info("Creating a new contact invocation");
        Contact c = new Contact();
        c.setName(CONTACT_NAME);
        c.setPhoneNumber(CONTACT_PHONE);
        Invocation saveContactInvocation = ClientBuilder.newClient().target(getRequestUrl()).request().buildPost(Entity.entity(c, MediaType.APPLICATION_JSON));

        // 3 - Create a new list Contacts Invocation
        log.info("Creating list all contacts invocation");
        Invocation listContactsInvocation = ClientBuilder.newClient().target(getRequestUrl()).request().buildGet();

        // 4 - Synch Save contact
        log.info("invoking the new contact");
        Contact persistedContact = saveContactInvocation.invoke(Contact.class);
        Assert.assertEquals("A contacts should be persisted with Id=1!", (Long) 1L, (Long) persistedContact.getId());

        // 5 - Async List contacts
        log.info("invoking list all contacts ASYNC");
        GenericType<List<Contact>> contactsListType = new GenericType<List<Contact>>() {
        };
        Future<List<Contact>> futureAllContacts = listContactsInvocation.submit(contactsListType);
        List<Contact> allContacts = futureAllContacts.get();
        Assert.assertEquals("Should have a single contact", 1, allContacts.size());
    }

    // Shows how to use Request and Response filters
    @Test
    public void requestResponseFiltersTest() {
        log.info("### Testing Request and Response Filters ###");

        // 1 - Drop all contacts
        log.info("dropping all contacts");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All contacts should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Contact Invocation
        log.info("Invoking create new contact using a ClientRequestFilter");
        Contact c = new Contact();
        c.setName(CONTACT_NAME);
        c.setPhoneNumber(CONTACT_PHONE);
        Contact persistedContact =
                ClientBuilder.newClient().register(SavedByClientRequestFilter.class).target(getRequestUrl()).request()
                        .post(Entity.entity(c, MediaType.APPLICATION_JSON), Contact.class);
        Assert.assertEquals("A contact should be persisted with savedBy", SavedByClientRequestFilter.USERNAME, persistedContact.getSavedBy());

        // 3 - Fetch all Contacts
        log.info("Invoking list all contacts using a ClientResponseFilter");
        GenericType<List<Contact>> contactsListType = new GenericType<List<Contact>>() {
        };
        List<Contact> allContacts = ClientBuilder.newClient().register(LogResponseFilter.class).target(getRequestUrl()).request().get(contactsListType);
        Assert.assertEquals("Should have a single contact", 1, allContacts.size());
    }
}
