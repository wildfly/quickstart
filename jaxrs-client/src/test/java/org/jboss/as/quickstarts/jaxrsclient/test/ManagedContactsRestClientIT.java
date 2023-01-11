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
package org.jboss.as.quickstarts.jaxrsclient.test;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.jaxrsclient.model.Contact;
import org.jboss.as.quickstarts.jaxrsclient.rest.JaxRsActivator;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class ManagedContactsRestClientIT extends AbstractContactsRestClient {

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "managed-executor-service.war")
                .addPackage(JaxRsActivator.class.getPackage())
                .addPackage(Contact.class.getPackage())
                .addClass(AbstractContactsRestClient.class)
                .addAsWebInfResource(new StringAsset("<beans xmlns=\"https://jakarta.ee/xml/ns/jakartaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                        + "xsi:schemaLocation=\"https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/beans_3_0.xsd\"\n"
                        + "bean-discovery-mode=\"all\">\n"
                        + "</beans>"), "beans.xml");
        return war;
    }

    @Override
    protected String getRequestUrl() {
        return new StringBuilder(deploymentUrl.toString())
                .append("rest/contacts")
                .toString();
    }


    // This test shows basic operations
    @Test
    @Override
    public void cruedTest() {
        super.cruedTest();
    }

    // This test shows some basic operations using ASYNC invocations and java.util.concurrent.Future
    @Test
    @Override
    public void asyncCrudTest() throws Exception {
        super.asyncCrudTest();
    }

    // This test shows how to use jakarta.ws.rs.client.InvocationCallback
    @Test
    @Override
    public void invocationCallBackTest() throws Exception {
        super.invocationCallBackTest();
    }

    // Shows how to use a delayed REST invocation
    @Test
    @Override
    public void delayedInvocationTest() throws Exception {
        super.delayedInvocationTest();
    }

    // Shows how to use Request and Response filters
    @Test
    @Override
    public void requestResponseFiltersTest() {
        super.requestResponseFiltersTest();
    }
}
