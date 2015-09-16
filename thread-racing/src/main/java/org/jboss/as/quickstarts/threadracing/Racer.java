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
package org.jboss.as.quickstarts.threadracing;

import org.jboss.as.quickstarts.threadracing.stage.concurrency.EEConcurrencyRaceStage;
import org.jboss.as.quickstarts.threadracing.stage.batch.BatchRaceStage;
import org.jboss.as.quickstarts.threadracing.stage.jaxrs.JAXRSRaceStage;
import org.jboss.as.quickstarts.threadracing.stage.jms.JMSRaceStage;
import org.jboss.as.quickstarts.threadracing.stage.json.JSONRaceStage;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import java.util.UUID;

/**
 * A racer, a CDI bean injected with an instance of each race stage, which are also CDI beans.
 *
 * The race stages are run sequentially in a managed thread, provided by the container's EE Concurrency 1.0 {@link javax.enterprise.concurrent.ManagedThreadFactory} default instance, also injected, but through @Resource.
 *
 * The race's {@link org.jboss.as.quickstarts.threadracing.Race.Registration} is used to control the racer's start and finish/abort.
 *
 * @author Eduardo Martins
 */
public class Racer {

    /**
     * JNDI injection of the default managed thread factory instance, introduced by EE Concurrency 1.0 (JSR 236),
     * which apps may use to create threads running tasks with the invocation context present on the thread creation.
     */
    @Resource
    private ManagedThreadFactory managedThreadFactory;

    /**
     * cdi injection of the Batch 1.0 race stage
     */
    @Inject
    private BatchRaceStage batchRaceStage;

    /**
     * cdi injection of the EE Concurrency 1.0 race stage
     */
    @Inject
    private EEConcurrencyRaceStage eeConcurrencyRaceStage;

    /**
     * cdi injection of the JAX-RS 2.0 race stage
     */
    @Inject
    private JAXRSRaceStage jaxrsRaceStageRen;

    /**
     * cdi injection of the JMS 2.0 race stage
     */
    @Inject
    private JMSRaceStage jmsRaceStage;

    /**
     * cdi injection of the JSON 1.0 race stage
     */
    @Inject
    private JSONRaceStage jsonRaceStageRen;

    /**
     * the racer's registration , which the racer uses to "interact" with a race
     */
    private Race.Registration registration;

    /**
     * the racer's name
     */
    private final String name;

    /**
     * Mandatory bean's no args constructor, which creates a random racer's name.
     */
    public Racer() {
        this("Racer" + UUID.randomUUID());
    }

    /**
     * Creates a racer with the specified name.
     * @param name
     */
    public Racer(String name) {
        this.name = name;
    }

    /**
     * Retrieves the racer's name.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the race's registration, used by the racer to report race progress.
     * @param registration
     */
    public void setRegistration(Race.Registration registration) {
        this.registration = registration;
    }

    /**
     * Starts the racer's engine a.k.a. as thread. The default {@link javax.enterprise.concurrent.ManagedThreadFactory} instance, provided by EE Concurrency 1.0, is used to create the racer's thread.
     */
    public void startEngine() {
        final Runnable raceTask = new Runnable() {
            @Override
            public void run() {
                try {
                    // the racer is ready
                    registration.ready();
                    // race on, run baby run
                    runRaceStages();
                    // game over
                    registration.done();
                } catch (Throwable t) {
                    registration.aborted(t);
                }
            }
        };
        managedThreadFactory.newThread(raceTask).start();
    }

    /**
     * Execution of the race stages/tasks.
     * @throws Exception
     */
    private void runRaceStages() throws Exception {
        batchRaceStage.run(registration);
        registration.broadcast("completed the Batch 1.0 stage.");
        eeConcurrencyRaceStage.run(registration);
        registration.broadcast("completed the EE Concurrency 1.0 stage.");
        jaxrsRaceStageRen.run(registration);
        registration.broadcast("completed the JAX-RS 2.0 stage.");
        jmsRaceStage.run(registration);
        registration.broadcast("completed the JMS 2.0 stage.");
        jsonRaceStageRen.run(registration);
        registration.broadcast("completed the JSON 1.0 stage.");
    }
}
