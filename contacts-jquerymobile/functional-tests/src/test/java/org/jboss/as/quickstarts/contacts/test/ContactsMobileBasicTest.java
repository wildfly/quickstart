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
package org.jboss.as.quickstarts.contacts.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.contacts.test.page.ContactListPage;
import org.jboss.as.quickstarts.contacts.test.page.ContactPage;
import org.jboss.as.quickstarts.contacts.test.page.fragment.NavigationPageFragment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contacts Mobile Basic quickstart functional test
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class ContactsMobileBasicTest {

    /**
     * Locator for contact page
     */
    @FindByJQuery("[data-role='page']:visible")
    ContactPage contactPage;

    /**
     * Locator for contact list page
     */
    @FindByJQuery("[data-role='page']:visible")
    ContactListPage listPage;

    /**
     * Locator for navigation panel
     */
    @FindByJQuery("[data-role='page']:visible")
    NavigationPageFragment navigation;

    /**
     * Injects browser to our test.
     */
    @Drone
    WebDriver browser;

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    //Test fixtures
    private static String FIRST_NAME_INVALID = "John&";
    private static String FIRST_NAME_A = "John";
    private static String FIRST_NAME_B = "Jane";
    private static String LAST_NAME_INVALID = "Doe11";
    private static String LAST_NAME = "Doe";
    private static String PHONE_NUMBER_INVALID = "123a";
    private static String PHONE_NUMBER_A = "+12105551111";
    private static String PHONE_NUMBER_B = "+12105552222";
    private static String PHONE_NUMBER_C = "+12105553333";
    private static String EMAIL_INVALID = "doe@";
    private static String EMAIL_A = "johndoe@mail.com";
    private static String EMAIL_B = "janedoe@mail.com";
    private static String EMAIL_C = "doe.jane@newmail.com";
    private static String DATE = "1970-01-01";
    private static String DATE_INVALID = "19aaa";
    private static String DATE_TOO_OLD = "1700-02-04";
    private static String DATE_FUTURE = "2020-01-01";

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.contacts();
    }

    @Before
    public void loadPage() {
        browser.get(contextPath.toString());
    }

    @Test
    @InSequence(1)
    public void requiredFieldsValidationTest() {
        navigation.openAddPage();
        contactPage.fillContact(new Contact(" ", "", "", ""));
        contactPage.submit(false);
        assertFalse(contactPage.isFirstNameValid());
        assertFalse(contactPage.isLastNameValid());
        assertFalse(contactPage.isPhoneNumberValid());
        assertFalse(contactPage.isEmailValid());
        assertFalse(contactPage.isBirthDateValid());
    }

    @Test
    @InSequence(2)
    public void contactFormValidationTest() {
        navigation.openAddPage();
        contactPage.fillContact(new Contact(FIRST_NAME_INVALID + " " + LAST_NAME_INVALID, PHONE_NUMBER_INVALID, EMAIL_INVALID, DATE_INVALID));
        assertFalse(contactPage.isFirstNameValid());
        assertFalse(contactPage.isLastNameValid());
        assertFalse(contactPage.isPhoneNumberValid());
        assertFalse(contactPage.isEmailValid());
        assertFalse(contactPage.isBirthDateValid());
        contactPage.submit(false);

        contactPage.fillContact(new Contact(FIRST_NAME_A + " " + LAST_NAME, PHONE_NUMBER_A, EMAIL_A, DATE_TOO_OLD));
        assertTrue(contactPage.isFirstNameValid());
        assertTrue(contactPage.isLastNameValid());
        assertTrue(contactPage.isPhoneNumberValid());
        assertTrue(contactPage.isEmailValid());
        assertFalse(contactPage.isBirthDateValid());
        contactPage.submit(false);

        contactPage.fillContact(new Contact(FIRST_NAME_A + " " + LAST_NAME, PHONE_NUMBER_A, EMAIL_A, DATE_FUTURE));
        assertTrue(contactPage.isFirstNameValid());
        assertTrue(contactPage.isLastNameValid());
        assertTrue(contactPage.isPhoneNumberValid());
        assertTrue(contactPage.isEmailValid());
        assertFalse(contactPage.isBirthDateValid());
        contactPage.submit(false);
    }

    @Test
    @InSequence(3)
    public void addContactTest() {
        Contact newContact = new Contact(FIRST_NAME_A + " " + LAST_NAME, PHONE_NUMBER_A, EMAIL_A, DATE);

        navigation.openAddPage();
        contactPage.fillContact(newContact);
        assertTrue(contactPage.isFormValid());
        contactPage.submit(true);

        listPage.waitForPage();
        listPage.showDetails();
        assertTrue(listPage.getContacts().contains(newContact));
    }

    @Test
    @InSequence(4)
    public void duplicateEmailValidationTest() {
        listPage.showDetails();
        String existingEmail = listPage.getContacts().get(0).getEmail();
        Contact newContact = new Contact(FIRST_NAME_B + " " + LAST_NAME, PHONE_NUMBER_B, existingEmail, DATE);

        navigation.openAddPage();
        contactPage.fillContact(newContact);
        assertTrue(contactPage.isFormValid());
        contactPage.submit(true);
        contactPage.waitForPage();
        assertFalse(contactPage.isEmailValid());

        newContact.setEmail(EMAIL_B);
        contactPage.fillContact(newContact);
        contactPage.submit(true);
        listPage.waitForPage();
        listPage.showDetails();
        assertTrue(listPage.getContacts().contains(newContact));
    }

    @Test
    @InSequence(5)
    public void editContactTest() {
        listPage.editContact(FIRST_NAME_B);
        contactPage.waitForPage();
        Contact contact = contactPage.getContact();
        contact.setPhoneNumber(PHONE_NUMBER_C);
        contact.setEmail(EMAIL_C);
        contactPage.fillContact(contact);
        contactPage.submit(true);

        listPage.waitForPage();
        listPage.showDetails();
        assertTrue(listPage.getContacts().contains(contact));
    }

}
