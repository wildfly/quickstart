/*
 * JBoss, Home of Professional Open Source
 * Copyright 2019, Red Hat, Inc. and/or its affiliates, and individual
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

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.service.ChildTargetService;
import org.wildfly.clustering.singleton.Singleton;
import org.wildfly.clustering.singleton.SingletonDefaultRequirement;
import org.wildfly.clustering.singleton.service.SingletonPolicy;

/**
 * {@link ServiceActivator} loaded by service loader mechanism (see {@code META-INF/services}) starts the singleton service and
 * a service which will regularly query the singleton service for the duration of the deployment of the archive.
 *
 * @author Radoslav Husar
 */
public class SingletonServiceActivator implements ServiceActivator {

    private static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service");
    private static final ServiceName QUERYING_SERVICE_NAME = ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service.querying");
    private static final ServiceName INSTALLER_SERVICE_NAME = ServiceName.parse(SingletonServiceActivator.class.getName());
    private static final ServiceName GROUP_SERVICE_NAME = ServiceName.parse("org.wildfly.clustering.default-group");

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public void activate(ServiceActivatorContext context) {
        ServiceBuilder<?> installerBuilder = context.getServiceTarget().addService(INSTALLER_SERVICE_NAME);
        Supplier<SingletonPolicy> policy = installerBuilder.requires(ServiceName.parse(SingletonDefaultRequirement.POLICY.getName()));
        Consumer<ServiceTarget> installer = target -> {
            // Install singleton service
            ServiceBuilder<?> builder = policy.get().createSingletonServiceConfigurator(SINGLETON_SERVICE_NAME).build(target);
            Supplier<Group> group = builder.requires(GROUP_SERVICE_NAME);
            builder.setInstance(new SingletonService(group)).install();
        };
        installerBuilder.setInstance(new ChildTargetService(installer)).install();

        // Install querying service

        ServiceBuilder<?> queryingBuilder = context.getServiceTarget().addService(QUERYING_SERVICE_NAME);
        Supplier<Singleton> singleton = queryingBuilder.requires(SINGLETON_SERVICE_NAME.append("singleton"));
        queryingBuilder.setInstance(new QueryingService(singleton)).install();

        this.log.info("Singleton and querying services activated.");
    }
}
