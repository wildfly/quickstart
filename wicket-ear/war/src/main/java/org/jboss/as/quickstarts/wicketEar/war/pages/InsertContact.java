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

import javax.ejb.EJB;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.jboss.as.quickstarts.wicketEar.ejbjar.dao.ContactDaoLocal;
import org.jboss.as.quickstarts.wicketEar.ejbjar.model.Contact;

/**
 *
 * @author Filippo Diotalevi
 */
public class InsertContact extends WebPage {

    private static final long serialVersionUID = 1L;
    private Form<Contact> insertForm;
    private String name;
    private String email;

    @EJB(name = "ContactDaoBean")
    private ContactDaoLocal contactDao;


    public InsertContact() {
        add(new FeedbackPanel("feedback"));

        insertForm = new Form<Contact>("insertForm") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                contactDao.addContact(name, email);
                setResponsePage(ListContacts.class);
            }
        };

        insertForm.add(new RequiredTextField<String>("name",
                new PropertyModel<String>(this, "name")));
        insertForm.add(new RequiredTextField<String>("email", new PropertyModel<String>(this,
                "email")));
        add(insertForm);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
