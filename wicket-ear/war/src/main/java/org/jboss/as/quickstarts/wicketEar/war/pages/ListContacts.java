/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.wicketEar.war.pages;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.ContactDao;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Contact;

/**
 * Dynamic behavior for the ListContact page
 * 
 * @author Filippo Diotalevi
 */
@SuppressWarnings("serial")
public class ListContacts extends WebPage {

    // Inject the ContactDao using @Inject
    @Inject
    private ContactDao contactDao;

    @Resource(name = "welcomeMessage")
    private String welcome;

    // Set up the dynamic behavior for the page, widgets bound by id
    public ListContacts() {

        // Add the dynamic welcome message, specified in web.xml
        add(new Label("welcomeMessage", welcome));
        
        // Populate the table of contacts
        add(new ListView<Contact>("contacts", contactDao.getContacts()) {

            @Override
            protected void populateItem(final ListItem<Contact> item) {
                Contact contact = item.getModelObject();
                item.add(new Label("name", contact.getName()));
                item.add(new Label("email", contact.getEmail()));
                item.add(new Link<Contact>("delete", item.getModel()) {

                    // Add a click handler
                    @Override
                    public void onClick() {
                        contactDao.remove(item.getModelObject());
                        setResponsePage(ListContacts.class);
                    }
                });
            }
        });
    }

}
