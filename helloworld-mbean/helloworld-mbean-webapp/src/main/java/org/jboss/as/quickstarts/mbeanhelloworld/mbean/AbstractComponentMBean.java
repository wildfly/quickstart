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
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Abstract component class to register it as mbean.
 *
 * @author Jeremie Lagarde
 *
 */
public abstract class AbstractComponentMBean {

    private static final Logger log = Logger.getLogger(AbstractComponentMBean.class.getName());

    private final String domain;
    private String name;
    private MBeanServer mbeanServer;
    private ObjectName objectName = null;

    public AbstractComponentMBean(String domain) {
        super();
        this.domain = domain;
    }

    @PostConstruct
    protected void startup() {
        this.name = this.getClass().getSimpleName();
        try {
            objectName = new ObjectName(domain, "type", name);
            mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.registerMBean(this, objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Error during registration of "
                + name + " into JMX:" + e, e);
        }
    }

    @PreDestroy
    protected void destroy() {
        log.info("# << -- Destroy : " + this.name);
        try {
            mbeanServer.unregisterMBean(this.objectName);
        } catch (Exception e) {
            throw new IllegalStateException("Error during unregistration of "
                + name + " into JMX:" + e, e);
        }
    }

}
