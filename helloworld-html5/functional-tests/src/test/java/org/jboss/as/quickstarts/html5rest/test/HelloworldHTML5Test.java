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
package org.jboss.as.quickstarts.html5rest.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.junit.Assert.assertEquals;

/**
 * Tests HTML5 Hello World quickstart
 *
 * @author Emil Cervenan
 */
@RunAsClient
@RunWith(Arquillian.class)
public class HelloworldHTML5Test {

    /**
     * Locator for name input field
     */
    @FindBy(id = "name")
    WebElement input;

    /**
     * Locator for submit button
     */
    @FindBy(id = "sayHello")
    WebElement button;

    /**
     * Locator for hello message
     */
    @FindBy(id = "result")
    WebElement message;

    /**
     * Name to be entered
     */
    private static final String NAME = "John Doe";

    /**
     * Specifies relative path to the war of built application in the main project.
     */
    private static final String DEPLOYMENT_WAR = "../target/helloworld-html5.war";

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

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive helloWorld() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT_WAR));
    }

    @Test
    public void sayHelloTest() {
        browser.get(contextPath.toString());
        input.clear();
        input.sendKeys(NAME);

        guardAjax(button).click();

        assertEquals("Entered name does not match.", "Hello " + NAME + "!", message.getText());
    }

    @Test
    public void sayHelloEmptyFieldTest() {
        browser.get(contextPath.toString());
        input.clear();

        guardNoRequest(button).click();

        assertEquals("Can't say hello if name is not provided.", "A name is required!", message.getText());
    }

}
