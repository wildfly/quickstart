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
package org.jboss.as.quickstarts.cluster.hasingleton.service.ejb;

import java.util.logging.Logger;

import org.jboss.msc.service.DelegatingServiceContainer;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;
import org.wildfly.clustering.singleton.SingletonServiceName;
import org.wildfly.clustering.singleton.election.NamePreference;
import org.wildfly.clustering.singleton.election.PreferredSingletonElectionPolicy;
import org.wildfly.clustering.singleton.election.SimpleSingletonElectionPolicy;

/**
 * Service activator that installs the HATimerService as a clustered singleton service
 * during deployment.
 *
 * @author Paul Ferraro
 * @author Wolf-Dieter Fink
 */
public class HATimerServiceActivator implements ServiceActivator {
    private final Logger log = Logger.getLogger(this.getClass().toString());

    @Override
    public void activate(ServiceActivatorContext context) {
        log.info("HATimerService will be installed!");

        HATimerService service = new HATimerService();
        ServiceName factoryServiceName = SingletonServiceName.BUILDER.getServiceName("server", "default");
        ServiceController<?> factoryService = context.getServiceRegistry().getRequiredService(factoryServiceName);
        SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) factoryService.getValue();
        ServiceName ejbComponentService = ServiceName.of("jboss", "deployment", "unit", "cluster-ha-singleton-service.jar", "component", "SchedulerBean", "START");
        factory.createSingletonServiceBuilder(HATimerService.SINGLETON_SERVICE_NAME, service)
            /*
             * The NamePreference is a combination of the node name (-Djboss.node.name) and the name of
             * the configured cache "singleton". If there is more than 1 node, it is possible to add more than
             * one name and the election will use the first available node in that list.
             *   -  To pass a chain of election policies to the singleton and tell JGroups to run the
             * singleton on a node with a particular name, uncomment the first line  and
             * comment the second line below.
             *   - To pass a list of more than one node, comment the first line and uncomment the
             * second line below.
             */

            .electionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(), new NamePreference("node1/singleton")))
            //singleton.setElectionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(), new NamePreference("node1/singleton"), new NamePreference("node2/singleton")));

            .build(new DelegatingServiceContainer(context.getServiceTarget(), context.getServiceRegistry()))
            .setInitialMode(ServiceController.Mode.ACTIVE)
            .addDependency(ejbComponentService)
            .install();
    }
}
