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
package org.jboss.as.quickstarts.wicketWar.pages;

import javax.annotation.Resource;
import javax.ejb.EJB;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.jboss.as.quickstarts.wicketWar.dao.ContactDaoLocal;
import org.jboss.as.quickstarts.wicketWar.model.Contact;

/**
 *
 * @author Filippo Diotalevi
 */
public class ListContacts extends WebPage {

    private static final long serialVersionUID = 1L;
    
    @EJB(name = "ContactDaoBean")
    private ContactDaoLocal contactDao;
    
    @Resource(name = "welcomeMessage")
    private String welcome;

    
    public ListContacts() {

        add(new Label("welcomeMessage", welcome));
        add(new ListView<Contact>("contacts", contactDao.getContacts()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Contact> item) {
                Contact contact = item.getModelObject();
                item.add(new Label("name", contact.getName()));
                item.add(new Label("email", contact.getEmail()));
                item.add(new Link<Contact>("delete", item.getModel()) {

                    private static final long serialVersionUID = 1L;

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
