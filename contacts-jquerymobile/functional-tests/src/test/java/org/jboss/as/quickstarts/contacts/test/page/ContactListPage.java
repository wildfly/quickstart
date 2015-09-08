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
package org.jboss.as.quickstarts.contacts.test.page;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.as.quickstarts.contacts.test.Contact;
import org.jboss.as.quickstarts.contacts.test.page.fragment.ContactListItemPageFragment;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

/**
 * Manipulation with Contact List page
 *
 * @author Oliver Kiss
 */
public class ContactListPage {

    @FindByJQuery("ul[data-role='listview']:visible li[id*='contact']")
    private List<ContactListItemPageFragment> contacts;

    @FindBy(id = "contacts-detail-list-page-button")
    private WebElement showDetailsButton;

    @FindBy(id = "contacts-list-page-button")
    private WebElement showListButton;

    @FindByJQuery(".ui-filterable:eq(0)")
    private WebElement filterInput;

    public void editContact(String name) {
        for (ContactListItemPageFragment contactPF : contacts) {
            if (contactPF.getContactName().contains(name)) {
                contactPF.editContact();
                return;
            }
        }
    }

    public List<Contact> getContacts() {
        List<Contact> ret = new ArrayList<>();
        for (ContactListItemPageFragment contactPF : contacts) {
            ret.add(contactPF.getContact());
        }
        return ret;
    }

    public void showDetails() {
        showDetailsButton.click();
        waitAjax().until().element(showListButton).is().present();
    }

    public void showList() {
        showListButton.click();
        waitAjax().until().element(showDetailsButton).is().present();
    }

    public void waitForPage() {
        waitModel().until().element(filterInput).is().visible();
    }
}
