/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.cluster.hasingleton.service.ejb;


import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;
import org.wildfly.clustering.singleton.election.NamePreference;
import org.wildfly.clustering.singleton.election.PreferredSingletonElectionPolicy;
import org.wildfly.clustering.singleton.election.SimpleSingletonElectionPolicy;

/**
 * @author Paul Ferraro
 * @author <a href="mailto:ralf.battenfeld@bluewin.ch">Ralf Battenfeld</a>
 */
public class HATimerServiceActivator implements ServiceActivator {

    private static final String CONTAINER_NAME = "server";
    private static final String CACHE_NAME = "default";
    public static final String PREFERRED_NODE = HATimerService.NODE_2;

    @Override
    public void activate(ServiceActivatorContext context) {
        install(HATimerService.DEFAULT_SERVICE_NAME, 1, context);
        install(HATimerService.QUORUM_SERVICE_NAME, 2, context);
    }

    private static void install(ServiceName name, int quorum, ServiceActivatorContext context) {
        InjectedValue<ServerEnvironment> env = new InjectedValue<>();
        HATimerService service = new HATimerService(env);
        ServiceController<?> factoryService = context.getServiceRegistry().getRequiredService(SingletonServiceBuilderFactory.SERVICE_NAME.append(CONTAINER_NAME, CACHE_NAME));
        SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) factoryService.getValue();
        factory.createSingletonServiceBuilder(name, service)
            .electionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(), new NamePreference(PREFERRED_NODE + "/" + CONTAINER_NAME)))
            .requireQuorum(quorum)
            .build(context.getServiceTarget())
                .addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, env)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install()
        ;
    }
}
