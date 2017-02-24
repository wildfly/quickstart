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
package org.jboss.as.quickstarts.kitchensink.test.page;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.kitchensink.test.Member;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

public class RegistrationFormPageFragment {
    /**
     * Injects browser to our test.
     */
    @Drone
    private WebDriver browser;

    /**
     * Injects JavascriptExecutor for executing javascript on opened page
     */
    @ArquillianResource
    private JavascriptExecutor javascript;

    /**
     * Locator for the registration form
     */
    @Root
    private WebElement registrationForm;

    /**
     * Locator for name field
     */
    @FindBy(id = "name")
    private WebElement nameField;

    /**
     * Locator for email field
     */
    @FindBy(id = "email")
    private WebElement emailField;

    /**
     * Locator for phone number field
     */
    @FindBy(id = "phoneNumber")
    private WebElement phoneField;

    /**
     * Locator for registration button
     */
    @FindByJQuery("input.register")
    private WebElement registerButton;

    /**
     * Locator for name field validation message
     */
    @FindBy(id = "name.errors")
    private WebElement nameErrorMessage;

    /**
     * Locator for email field validation message
     */
    @FindBy(id = "email.errors")
    private WebElement emailErrorMessage;

    /**
     * Locator for phone number field validation message
     */
    @FindBy(id = "phoneNumber.errors")
    private WebElement phoneNumberErrorMessage;

    /**
     * Locator for date field validation message
     */
    @FindBy(id = "date.errors")
    private WebElement dateErrorMessage;

    /**
     * Locator for today's date in the date picker
     */
    @FindByJQuery("a.ui-state-highlight")
    private WebElement today;

    public void register(Member member, boolean fillDate) {
        if (fillDate) {
            registrationForm.click();
            javascript.executeScript("document.getElementById('datepicker').focus()");
            WebElement today = browser.findElement(ByJQuery.selector("a.ui-state-highlight"));
            waitModel().until().element(today).is().visible();
            today.click();
            javascript.executeScript("document.getElementById('datepicker').blur()");
        }
        nameField.click();
        nameField.clear();
        nameField.sendKeys(member.getName());
        emailField.clear();
        emailField.sendKeys(member.getEmail());
        phoneField.clear();
        phoneField.sendKeys(member.getPhoneNumber());

        guardHttp(registerButton).click();
    }

    public String getNameValidation() {
        return nameErrorMessage.getText();
    }

    public String getEmailValidation() {
        return emailErrorMessage.getText();
    }

    public String getPhoneValidation() {
        return phoneNumberErrorMessage.getText();
    }

    public String getDateValidation() {
        return dateErrorMessage.getText();
    }

    public void waitUntilPresent() {
        waitModel().until().element(registrationForm).is().present();
    }
}
