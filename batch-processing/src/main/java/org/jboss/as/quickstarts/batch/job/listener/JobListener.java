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
package org.jboss.as.quickstarts.batch.job.listener;

import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

//This listener modifies the Job to ABANDONED state if it was restarted once
@Named("jobListener")
public class JobListener extends AbstractJobListener {

    @Inject
    private Logger log;

    @Inject
    private JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        log.info(String.format("Job %s - Execution #%d starting.", jobContext.getJobName(), jobContext.getExecutionId()));
    }

    @Override
    public void afterJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = jobOperator.getParameters(jobContext.getExecutionId());
        //If the JOB was already restarted once, mark it as abandoned
        if (jobParameters.get("restartedOnce") != null && jobContext.getBatchStatus().equals(BatchStatus.FAILED)) {
            log.info("Job already restarted once! Abandoning it forever");
            jobOperator.abandon(jobContext.getExecutionId());
        }
        log.info(String.format("Job %s - Execution #%d finished. Status: %s", jobContext.getJobName(), jobContext.getExecutionId(), jobContext.getBatchStatus()));
    }

}
