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
import org.jboss.arquillian.graphene.angular.findby.FindByNg;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.kitchensink.test.Member;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
    @FindByNg(model = "newMember.name")
    private WebElement nameField;

    /**
     * Locator for email field
     */
    @FindByNg(model = "newMember.email")
    private WebElement emailField;

    /**
     * Locator for phone number field
     */
    @FindByNg(model = "newMember.phoneNumber")
    private WebElement phoneField;

    /**
     * Locator for registration button
     */
    @FindBy(id = "register")
    private WebElement registerButton;

    /**
     * Locator for name field validation message
     */
    @FindByJQuery("span[ng-show='errors.name']")
    private WebElement nameErrorMessage;

    /**
     * Locator for phone field validation message
     */
    @FindByJQuery(".error[ng-show*=phoneNumber]:visible")
    private WebElement phoneErrorMessage;

    /**
     * Locator for registration success message
     */
    @FindByJQuery("ul.success li:first")
    private WebElement registeredMessageSuccess;


    public void register(Member member, boolean successExpected) {
        nameField.clear();
        nameField.sendKeys(member.getName());
        emailField.clear();
        emailField.sendKeys(member.getEmail());
        phoneField.clear();
        phoneField.sendKeys(member.getPhoneNumber());
        registerButton.click();

        if (successExpected) waitModel().until().element(registeredMessageSuccess).is().visible();
    }

    public String waitForNameValidation() {
        waitModel().until("Name validation message should be present").element(nameErrorMessage).is().visible();
        return nameErrorMessage.getText();
    }

    public boolean nameValidity() {
        return isValid(nameField);
    }

    public boolean emailValidity() {
        return isValid(emailField);
    }

    public boolean phoneValidity() {
        return new WebElementConditionFactory(phoneErrorMessage).not().isPresent().apply(browser);
    }

    public boolean formValidity() {
        return isValid(registrationForm);
    }

    public void waitUntilPresent() {
        waitModel().until().element(registrationForm).is().present();
    }

    /**
     * Helper method for executing checkValidity() javascript method from HTML5 form validation API
     *
     * @param element Element to be checked
     * @return Element validity
     */
    private boolean isValid(WebElement element) {
        return (Boolean) javascript.executeScript("return arguments[0].checkValidity()", element);
    }

}
