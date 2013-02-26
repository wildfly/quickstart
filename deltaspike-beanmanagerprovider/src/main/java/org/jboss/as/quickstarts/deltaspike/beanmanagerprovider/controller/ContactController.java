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

package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.AuditContact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.Contact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence.AuditRepository;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence.ContactRepository;

/**
 * 
 * This class uses {@link ConversationScoped} and {@link Named} instead of {@link Model} because it holds the managed
 * {@link Contact} entity instance accross the requests.
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Named
@ConversationScoped
public class ContactController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FacesContext facesContext;

    @Inject
    private ContactRepository contactRepository;

    @Inject
    private AuditRepository auditRepository;

    @Inject
    private Conversation conversation;

    private Contact contact;

    private List<Contact> allContacts;

    // return the managed entity instance
    public Contact getContact() {
        return contact;
    }

    public void save() {
        // Define the faces message based on the entity state
        String msg = isContactManaged() ? "Contact updated" : "New Contact added";
        try {
            contactRepository.persist(contact);
        } catch (Exception e) {
            // discard the conversation (and the entity manager) on any exception
            conversation.end();
            // reset the entity Id
            contact.setId(null);
            msg = "Can't create contact: " + e.getMessage();
        }
        // add the message to be showed on the jsf page
        facesContext.addMessage(null, new FacesMessage(msg));
    }

    public void remove(Contact contact) {
        contactRepository.remove(contact);
        facesContext.addMessage(null, new FacesMessage("Contact Removed"));
        this.contact = new Contact();
    }

    // select an instance from the table to edition
    public void selectForEdit(Contact contact) {
        this.contact = contact;
    }

    /*
     * This method will promote the conversation when this component is constructed through the {@link PostConstruct} annotation
     * This will also create a new entity to be managed/edited and get the list of all contacts previously persisted.
     */
    @PostConstruct
    public void newContact() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
        contact = new Contact();
        allContacts = contactRepository.getAllContacts();
    }

    /*
     * Updates the Contact list when a event on Contact was fired. The Events are produced by {@link ContactRepository}
     */
    public void readAllContacts(@Observes Contact contact) {
        allContacts = contactRepository.getAllContacts();
    }

    // Return the conversation number to be used on the page footer
    public String getConversationNumber() {
        return "Conversation Id: " + (conversation.getId() == null ? "conversation transient" : conversation.getId());
    }

    // Return the information to be used on *All Contacts* table.
    public List<Contact> getAllContacts() {
        return allContacts;
    }

    // Return the information to be used on *Audit Records* table
    public List<AuditContact> getAuditRecords() {
        return auditRepository.getAllAuditRecords();
    }

    public boolean isContactManaged() {
        return contact.getId() != null;
    }

}