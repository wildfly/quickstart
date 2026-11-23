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

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.wildfly.clustering.server.Group;
import org.wildfly.clustering.server.GroupMember;
import org.wildfly.clustering.singleton.Singleton;
import org.wildfly.clustering.singleton.service.SingletonServiceActivatorContext;
import org.wildfly.clustering.singleton.service.SingletonServiceBuilder;

/**
 * {@link org.jboss.msc.service.ServiceActivator} loaded by service loader mechanism (see {@code META-INF/services}) starts
 * the singleton service and a service which will regularly query the singleton service for the duration of the deployment
 * of the archive.
 * <p>
 * The {@link org.wildfly.clustering.singleton.service.SingletonServiceActivator} interface declares two {@code activate}
 * methods:
 * <ul>
 * <li>{@link #activate(SingletonServiceActivatorContext)} is the method to implement for installing singleton services.</li>
 * <li>{@link #activate(ServiceActivatorContext)} is the standard {@link org.jboss.msc.service.ServiceActivator} entry point
 * invoked by the server, which by default sets up singleton service installation. It only needs to be overridden to also
 * install regular (non-singleton) services, as this quickstart does.</li>
 * </ul>
 *
 * @author Radoslav Husar
 */
public class SingletonServiceActivator implements org.wildfly.clustering.singleton.service.SingletonServiceActivator {

    private static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service");
    private static final ServiceName QUERYING_SERVICE_NAME = ServiceName.parse("org.jboss.as.quickstarts.ha.singleton.service.querying");
    private static final ServiceName DEFAULT_GROUP_SERVICE_NAME = ServiceName.parse("org.wildfly.clustering.server.default-group");

    private final Logger log = Logger.getLogger(this.getClass());

    // Reference to the installed singleton service, which exposes the state of the singleton (e.g. its primary provider)
    private final AtomicReference<Singleton> singleton = new AtomicReference<>();

    /**
     * Entry point invoked by the server when the deployment is activated.
     * Applications that only install singleton services do not need to override this method.
     * <p>
     * This quickstart overrides it because it also installs the querying service, which must run on all nodes and therefore
     * must <em>not</em> be installed as a singleton service.
     */
    @Override
    public void activate(ServiceActivatorContext context) {
        // Install singleton services via #activate(SingletonServiceActivatorContext) using the default singleton policy
        org.wildfly.clustering.singleton.service.SingletonServiceActivator.super.activate(context);

        // Install querying service, which is a regular (i.e. non-singleton) service running on all nodes
        ServiceBuilder<?> queryingBuilder = context.getServiceTarget().addService();
        queryingBuilder.provides(QUERYING_SERVICE_NAME);
        queryingBuilder.setInstance(new QueryingService(this.singleton::get)).install();

        this.log.info("Singleton and querying services activated.");
    }

    /**
     * Installs the singleton services. This is the only method applications typically implement.
     */
    @Override
    public void activate(SingletonServiceActivatorContext context) {
        // Any service installed into this target will be instrumented as a singleton service
        SingletonServiceBuilder<?> builder = context.getServiceTarget().addService();
        builder.provides(SINGLETON_SERVICE_NAME);
        Supplier<Group<GroupMember>> group = builder.requires(DEFAULT_GROUP_SERVICE_NAME);
        // The installed service controller also exposes the singleton state
        this.singleton.set(builder.setInstance(new SingletonService(group)).install());
    }
}
