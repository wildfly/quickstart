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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.as.quickstarts.contacts.test.Contact;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Manipulation with Add/Edit Contact pages
 *
 * @author Oliver Kiss
 */
public class ContactPage {

    /**
     * Injects browser to our test.
     */
    @Drone
    private WebDriver browser;

    @Root
    private WebElement page;

    @FindBy(name = "firstName")
    private WebElement firstName;

    @FindBy(name = "lastName")
    private WebElement lastName;

    @FindBy(name = "phoneNumber")
    private WebElement phoneNumber;

    @FindBy(name = "email")
    private WebElement email;

    @FindBy(name = "birthDate")
    private WebElement birthDate;

    @FindByJQuery("label[for*='input-firstName'].error")
    private WebElement firstNameValidationMessage;

    @FindByJQuery("label[for*='input-lastName'].error")
    private WebElement lastNameValidationMessage;

    @FindByJQuery("label[for*='input-tel'].error")
    private WebElement phoneNumberValidationMessage;

    @FindByJQuery("label[for*='input-email'].error")
    private WebElement emailValidationMessage;

    @FindByJQuery("label[for*='input-date'].error")
    private WebElement birthDateValidationMessage;

    @FindByJQuery("label[for*='input-'].error:visible")
    private List<WebElement> validationMessages;

    @FindByJQuery("[onclick*='#submit']:visible")
    private WebElement saveButton;

    public void fillContact(Contact contact) {
        String[] name = contact.getName().split(" ", 2);
        firstName.clear();
        firstName.sendKeys(name[0]);
        lastName.clear();
        lastName.sendKeys(name[1]);
        phoneNumber.clear();
        phoneNumber.sendKeys(contact.getPhoneNumber());
        email.clear();
        email.sendKeys(contact.getEmail());
        birthDate.clear();
        birthDate.sendKeys(contact.getBirthDate());
        page.click();
    }

    public Contact getContact() {
        Contact contact = new Contact();
        contact.setName(firstName.getAttribute("value") + " " + lastName.getAttribute("value"));
        contact.setPhoneNumber(phoneNumber.getAttribute("value"));
        contact.setEmail(email.getAttribute("value"));
        contact.setBirthDate(birthDate.getAttribute("value"));
        return contact;
    }

    public void submit(boolean requestExpected) {
        if (requestExpected) {
            guardAjax(saveButton).click();
        } else {
            guardNoRequest(saveButton).click();
        }
    }

    public boolean isFirstNameValid() {
        return !firstNameValidationMessage.isDisplayed();
    }

    public boolean isLastNameValid() {
        return !lastNameValidationMessage.isDisplayed();
    }

    public boolean isPhoneNumberValid() {
        return !phoneNumberValidationMessage.isDisplayed();
    }

    public boolean isEmailValid() {
        return !emailValidationMessage.isDisplayed();
    }

    public boolean isBirthDateValid() {
        return new WebElementConditionFactory(birthDateValidationMessage).not().isPresent().apply(browser);
    }

    public boolean isFormValid() {
        return validationMessages.size() == 0;
    }

    public void waitForPage() {
        waitModel().until().element(firstName).is().visible();
    }

}
