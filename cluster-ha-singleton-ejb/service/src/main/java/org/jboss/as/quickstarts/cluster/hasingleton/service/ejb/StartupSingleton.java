/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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

import java.util.Collection;
import java.util.EnumSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jboss.as.clustering.singleton.SingletonService;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Transition;
import org.jboss.msc.service.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A Singleton EJB to create the SingletonService during startup.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Singleton
@Startup
public class StartupSingleton {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupSingleton.class);

	/**
	 * Create the Service and wait until it is started.<br/>
	 * Will log a message if the service will not start in 10sec. 
	 */
	@PostConstruct
	protected void startup() {
		LOGGER.info("StartupSingleton will be initialized!");

		HATimerService service = new HATimerService();
		SingletonService<String> singleton = new SingletonService<String>(service, HATimerService.SINGLETON_SERVICE_NAME);
		// if there is a node where the Singleton should deployed the election policy might set,
		// otherwise the JGroups coordinator will start it
		//singleton.setElectionPolicy(new PreferredSingletonElectionPolicy(new NamePreference("node2/cluster"), new SimpleSingletonElectionPolicy()));
		ServiceController<String> controller = singleton.build(CurrentServiceContainer.getServiceContainer())
				.addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, service.env)
				.install();

		controller.setMode(ServiceController.Mode.ACTIVE);
		try {
			wait(controller, EnumSet.of(ServiceController.State.DOWN, ServiceController.State.STARTING), ServiceController.State.UP);
			LOGGER.info("StartupSingleton has started the Service");
		} catch (IllegalStateException e) {
			LOGGER.warn("Singleton Service {} not started, are you sure to start in a cluster (HA) environment?",HATimerService.SINGLETON_SERVICE_NAME);
		}
	}

	/**
	 * Remove the service during undeploy or shutdown
	 */
	@PreDestroy
	protected void destroy() {
		LOGGER.info("StartupSingleton will be removed!");
		ServiceController<?> controller = CurrentServiceContainer.getServiceContainer().getRequiredService(HATimerService.SINGLETON_SERVICE_NAME);
		controller.setMode(ServiceController.Mode.REMOVE);
		try {
			wait(controller, EnumSet.of(ServiceController.State.UP, ServiceController.State.STOPPING, ServiceController.State.DOWN), ServiceController.State.REMOVED);
		} catch (IllegalStateException e) {
			LOGGER.warn("Singleton Service {} has not be stopped correctly!",HATimerService.SINGLETON_SERVICE_NAME);
		}
	}

	private static <T> void wait(ServiceController<T> controller, Collection<ServiceController.State> expectedStates, ServiceController.State targetState) {
		if (controller.getState() != targetState) {
			ServiceListener<T> listener = new NotifyingServiceListener<T>();
			controller.addListener(listener);
			try {
				synchronized (controller) {
					int maxRetry = 2;
					while (expectedStates.contains(controller.getState()) && maxRetry > 0) {
						LOGGER.info("Service controller state is {}, waiting for transition to {}", new Object[] {controller.getState(), targetState});
						controller.wait(5000);
						maxRetry--;
					}
				}
			} catch (InterruptedException e) {
				LOGGER.warn("Wait on startup is interrupted!");
				Thread.currentThread().interrupt();
			}
			controller.removeListener(listener);
			ServiceController.State state = controller.getState();
			LOGGER.info("Service controller state is now {}",state);
			if (state != targetState) {
				throw new IllegalStateException(String.format("Failed to wait for state to transition to %s.  Current state is %s", targetState, state), controller.getStartException());
			}
		}
	}

	private static class NotifyingServiceListener<T> extends AbstractServiceListener<T> {
		@Override
		public void transition(ServiceController<? extends T> controller, Transition transition) {
			synchronized (controller) {
				controller.notify();
			}
		}
	}
}
