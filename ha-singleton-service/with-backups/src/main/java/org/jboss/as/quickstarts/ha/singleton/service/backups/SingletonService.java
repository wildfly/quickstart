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

package org.jboss.as.quickstarts.ha.singleton.service.backups;

import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.group.Group;
import org.wildfly.clustering.group.Node;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of service to demonstrate singleton service. After the service is started, it logs a message every
 * 5 seconds. This way we can illustrate which node is the primary and which nodes are the backups.
 *
 * @author Radoslav Husar
 */
class SingletonService implements Service<Node> {

    private Logger LOG = Logger.getLogger(this.getClass());

    private ScheduledExecutorService executor;
    private InjectedValue<Group> group;
    private boolean primary;

    SingletonService(boolean primary, InjectedValue<Group> group) {
        this.primary = primary;
        this.group = group;
    }

    @Override
    public void start(StartContext context) throws StartException {
        LOG.infof("%s singleton service is starting.", this.serviceType());

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(
                () -> LOG.infof("%s singleton service is running on node '%s'.", this.serviceType(), this.getValue()),
                5, 5, TimeUnit.SECONDS);
    }

    @Override
    public void stop(StopContext context) {
        LOG.infof("%s singleton service is stopping.", this.serviceType());

        executor.shutdown();
    }

    @Override
    public Node getValue() throws IllegalStateException, IllegalArgumentException {
        return this.group.getValue().getLocalNode();
    }

    private String serviceType() {
        return primary ? "Primary" : "Backup";
    }
}
