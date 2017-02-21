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
package org.jboss.as.quickstarts.cdi.interceptors.test;

import static org.junit.Assert.assertEquals;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.cdi.service.History;
import org.jboss.as.quickstarts.cdi.service.Item;
import org.jboss.as.quickstarts.cdi.service.ItemServiceBean;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Simple tests for cdi interceptors. Arquillian deploys an WAR archive to the application server and run the test.
 * ItemServiceBean is injected into the test so user can verify the interceptors are working.
 *
 */
@RunWith(Arquillian.class)
public class InterceptorsEnabledIT {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addPackages(true, "org.jboss.as.quickstarts.cdi")
            // enable JPA
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            // enable CDI interceptors
            .addAsWebInfResource("beans.xml", "beans.xml")
            // Deploy test datasource
            .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @EJB
    private ItemServiceBean itemService;

    @Test
    public void testAuditInterceptor() {
        Item item = new Item();
        item.setName("testItem");
        itemService.create(item);
        item = itemService.getList().get(0);
        // assert that 2 entries were added to history.
        assertEquals(2, History.getItemHistory().size());
    }

}
