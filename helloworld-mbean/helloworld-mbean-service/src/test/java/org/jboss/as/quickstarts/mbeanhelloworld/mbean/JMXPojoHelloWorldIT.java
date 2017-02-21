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
package org.jboss.as.quickstarts.mbeanhelloworld.mbean;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing pojo mbean with mxbean interface.
 *
 * @author Jeremie Lagarde
 *
 */
@RunWith(Arquillian.class)
public class JMXPojoHelloWorldIT {

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "myservice.war")
            .addClasses(MXPojoHelloWorld.class).addClasses(IHelloWorldMXBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("META-INF/jboss-service.xml", "jboss-service.xml");
    }

    @Test
    public void testHello() throws Exception {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("quickstarts", "type", "SarMXPojoHelloWorld");
        MBeanInfo mbeanInfo = mbeanServer.getMBeanInfo(objectName);
        Assert.assertNotNull(mbeanInfo);
        Assert.assertEquals(0L, mbeanServer.getAttribute(objectName, "Count"));
        Assert.assertEquals("Welcome", mbeanServer.getAttribute(objectName, "WelcomeMessage"));
        Assert.assertEquals("Welcome jer!",
            mbeanServer.invoke(objectName, "sayHello", new Object[] { "jer" }, new String[] { "java.lang.String" }));
        Assert.assertEquals(1L, mbeanServer.getAttribute(objectName, "Count"));
        mbeanServer.setAttribute(objectName, new Attribute("WelcomeMessage", "Hi"));
        Assert.assertEquals("Hi jer!",
            mbeanServer.invoke(objectName, "sayHello", new Object[] { "jer" }, new String[] { "java.lang.String" }));
        Assert.assertEquals(2L, mbeanServer.getAttribute(objectName, "Count"));
    }

}
