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

import javax.ejb.Stateless;

import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;

/**
 * A simple SLSB to access the internal SingletonService.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class ServiceAccessBean implements ServiceAccess {
    private static final Logger LOGGER = Logger.getLogger(ServiceAccessBean.class);

    public String getNodeNameOfTimerService() {
        LOGGER.info("Method getNodeNameOfTimerService() is invoked");
        ServiceController<?> service = CurrentServiceContainer.getServiceContainer().getService(
                HATimerService.DEFAULT_SERVICE_NAME);

        // Example how to leverage JBoss Logging to do expensive String concatenation only when needed:
        LOGGER.debugf("Service: %s", service);

        if (service != null) {
            return ((Environment)service.getValue()).getNodeName();
        } else {
            throw new IllegalStateException("Service '" + HATimerService.DEFAULT_SERVICE_NAME + "' not found!");
        }
    }
}
