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
package org.jboss.as.quickstarts.contacts.test.page.fragment;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.as.quickstarts.contacts.test.Contact;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

/**
 * Page fragment for list items on contact list page
 *
 * @author Oliver Kiss
 */
public class ContactListItemPageFragment {

    /**
     * Injects browser to our test.
     */
    @Drone
    private WebDriver browser;

    @FindByJQuery("a[href='#contacts-edit-page']")
    private WebElement name;

    @FindByJQuery(".detialedList p:nth-child(1)")
    private WebElement email;

    @FindByJQuery(".detialedList p:nth-child(2)")
    private WebElement phoneNumber;

    @FindByJQuery(".detialedList p:nth-child(3)")
    private WebElement birthDate;

    public Contact getContact() {
        return new Contact(name.getText(), phoneNumber.getText().replaceAll(" |-", ""), email.getText(), birthDate.getText());
    }

    public String getContactName() {
        return name.getText();
    }

    public void editContact() {
        guardAjax(name).click();
    }
}
