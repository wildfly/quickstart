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

import java.util.function.Supplier;

import org.jboss.logging.Logger;
import org.jboss.msc.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StopContext;
import org.wildfly.clustering.group.Group;

/**
 * Implementation of the singleton service. The container will ensure that at most one instance of this service is running at
 * any given time. The value this service returns is the logical name of the server this service is running on.
 *
 * @author Radoslav Husar
 */
class SingletonService implements Service {

    private final Logger log = Logger.getLogger(this.getClass());

    private final Supplier<Group> group;

    SingletonService(Supplier<Group> group) {
        this.group = group;
    }

    @Override
    public void start(StartContext context) {
        this.log.infof("Singleton service started on %s.", this.group.get().getLocalMember());
    }

    @Override
    public void stop(StopContext context) {
        this.log.infof("Singleton service stopped on %s.", this.group.get().getLocalMember());
    }

}
