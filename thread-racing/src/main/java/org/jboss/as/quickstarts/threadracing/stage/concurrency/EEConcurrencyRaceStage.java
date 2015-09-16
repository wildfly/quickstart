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
package org.jboss.as.quickstarts.threadracing.stage.concurrency;

import org.jboss.as.quickstarts.threadracing.Race;
import org.jboss.as.quickstarts.threadracing.stage.RaceStage;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.naming.InitialContext;
import java.util.concurrent.TimeUnit;

/**
 * The EE Concurrency 1.0 race shows how to retrieve and use the EE Concurrency 1.0 {@link javax.enterprise.concurrent.ManagedExecutorService} and {@link javax.enterprise.concurrent.ManagedScheduledExecutorService} default instances.
 *
 * @author Eduardo Martins
 */
public class EEConcurrencyRaceStage implements RaceStage {

    /**
     * Injecting the managed executor service's default instance is that simple.
     */
    @Resource
    private ManagedExecutorService executorService;

    @Override
    public void run(Race.Registration registration) throws Exception {
        // the task to submit (to the managed executor service)
        final Runnable runnableToSubmit = new Runnable() {
            @Override
            public void run() {
                // this task will run in a different thread, but it will be executed with the app invocation context available when the task was submitted, which means class loading, JNDI lookups, or even security roles, will work as expected
                try {
                    // as an example, let's lookup the managed scheduled executor service's default instance, from the standard JNDI name, scoped to the web app
                    final ManagedScheduledExecutorService scheduledExecutorService = InitialContext.doLookup("java:comp/DefaultManagedScheduledExecutorService");
                    // schedule a task and wait for its execution
                    final Runnable runnableToSchedule = new Runnable() {
                        @Override
                        public void run() {
                            // TODO all apps have at least one
                        }
                    };
                    scheduledExecutorService.schedule(runnableToSchedule, 100, TimeUnit.MILLISECONDS).get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        // submit the task and wait for its execution
        executorService.submit(runnableToSubmit).get();
    }
}
