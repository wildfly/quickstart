/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.ha.singleton.service;

import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;
import org.wildfly.clustering.singleton.SingletonDefaultRequirement;
import org.wildfly.clustering.singleton.SingletonPolicy;

/**
 * {@link org.jboss.msc.service.ServiceActivator} loaded by service loader mechanism (see {@code META-INF/services})
 * starts the singleton service and a service which will regularly query the singleton service for the duration of the
 * deployment of the archive.
 *
 * @author Radoslav Husar
 */
public class ServiceActivator implements org.jboss.msc.service.ServiceActivator {

    private final Logger LOG = Logger.getLogger(ServiceActivator.class);

    static final ServiceName SINGLETON_SERVICE_NAME =
            ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service");
    private static final ServiceName QUERYING_SERVICE_NAME =
            ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service.querying");

    @Override
    public void activate(ServiceActivatorContext serviceActivatorContext) {
        try {
            SingletonPolicy policy = (SingletonPolicy) serviceActivatorContext
                    .getServiceRegistry()
                    .getRequiredService(ServiceName.parse(SingletonDefaultRequirement.SINGLETON_POLICY.getName()))
                    .awaitValue();

            InjectedValue<Group> group = new InjectedValue<>();

            Service<Node> service = new SingletonService(group);

            policy.createSingletonServiceBuilder(SINGLETON_SERVICE_NAME, service)
                    .build(serviceActivatorContext.getServiceTarget())
                    .addDependency(ServiceName.parse("org.wildfly.clustering.default-group"), Group.class, group)
                    .install();

            serviceActivatorContext.getServiceTarget()
                    .addService(QUERYING_SERVICE_NAME, new QueryingService())
                    .setInitialMode(ServiceController.Mode.ACTIVE)
                    .install();

            LOG.info("Singleton and querying services activated.");
        } catch (InterruptedException e) {
            throw new ServiceRegistryException(e);
        }
    }
}
