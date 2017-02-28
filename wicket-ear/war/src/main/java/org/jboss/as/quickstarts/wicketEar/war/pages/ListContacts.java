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
package org.jboss.as.quickstarts.wicketEar.war.pages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
        add(new RefreshingView<ContactDto>("contacts") {

            @Override
            protected Iterator<IModel<ContactDto>> getItemModels() {
                List<IModel<ContactDto>> models = new ArrayList<>();
                for (Contact contact : contactDao.getContacts()) {
                    models.add(Model.of(new ContactDto(contact)));
                }
                return models.iterator();
            }

            @Override
            protected void populateItem(final Item<ContactDto> item) {
                ContactDto contact = item.getModelObject();
                item.add(new Label("name", contact.getName()));
                item.add(new Label("email", contact.getEmail()));
                item.add(new Link<ContactDto>("delete", item.getModel()) {

                    // Add a click handler
                    @Override
                    public void onClick() {
                        contactDao.remove(item.getModelObject().getId());
                        setResponsePage(ListContacts.class);
                    }
                });
            }
        });
    }

    /**
     * This class is detached version of {@link Contact} and it's purpose is to
     * avoid detachable model in order not to complicate this example.
     * <p>
     * For more information please see
     * https://ci.apache.org/projects/wicket/guide/7.x/guide/modelsforms.html#modelsforms_6
     */
    private static class ContactDto implements Serializable {
        private final Long id;
        private final String name;
        private final String email;

        ContactDto(Contact contact) {
            this.id = contact.getId();
            this.name = contact.getName();
            this.email = contact.getEmail();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
