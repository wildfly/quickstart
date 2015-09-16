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
package org.jboss.as.quickstarts.threadracing.stage.batch;

import org.jboss.as.quickstarts.threadracing.Race;
import org.jboss.as.quickstarts.threadracing.stage.RaceStage;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import java.util.Properties;

/**
 * The Batch 1.0 race stage, which starts a job execution and waits for it to complete.
 *
 * Apps deploy Batch jobs by packaging the related XML descriptors in META-INF/batch-jobs. The name of these Batch jobs, which is needed to start their execution, is the name of the XML file, without the ".xml".
 * This app deploys a Batch job named "race", so the XML descriptor file is named "race.xml" and put in "src/main/resources/META-INF/batch-jobs" directory of the project.
 *
 *
 * @author Eduardo Martins
 */
public class BatchRaceStage implements RaceStage {

    @Override
    public void run(Race.Registration registration) throws Exception {
        // retrieve the job operator
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        // start the race.xml job
        Long executionId = jobOperator.start("race", new Properties());
        // would be nice if the job start would provide a Future object wrt the job execution, perhaps something for next spec revision
        // just check every 1ms if the job status is completed
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        while (jobExecution.getBatchStatus() != BatchStatus.COMPLETED) {
            Thread.sleep(1);
        }
    }
}
