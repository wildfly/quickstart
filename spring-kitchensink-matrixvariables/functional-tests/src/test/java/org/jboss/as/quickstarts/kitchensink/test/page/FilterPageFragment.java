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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

public class FilterPageFragment {

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "filt")
    private WebElement filterButton;

    @FindBy(id = "clear")
    private WebElement clearButton;

    public void filter(String name, String email) {
        nameInput.clear();
        emailInput.clear();
        nameInput.sendKeys(name);
        emailInput.sendKeys(email);
        guardAjax(filterButton).click();

    }

    public void filterName(String name) {
        filter(name, "");
    }

    public void filterEmail(String email) {
        filter("", email);
    }

    public void clear() {
        guardAjax(clearButton).click();
    }
}
