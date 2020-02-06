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
package org.jboss.as.quickstarts.batch.controller;

import java.util.Date;
import java.util.Properties;

import javax.batch.runtime.BatchStatus;

//This class is used to show the latest job execution from each job instance
public class JobData {

    private Long jobInstanceId;

    private Long executionId;

    private String jobName;

    private Date startTime;

    private Date endTime;

    private BatchStatus batchStatus;

    private Properties jobParameters;

    private String exitStatus;

    public JobData(Long jobInstanceId, Long executionId, String jobName,
        Date startTime, Date endTime, BatchStatus batchStatus,
        Properties parameters, String exitStatus) {
        this.jobInstanceId = jobInstanceId;
        this.executionId = executionId;
        this.jobName = jobName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.batchStatus = batchStatus;
        this.jobParameters = parameters;
        this.exitStatus = exitStatus;
    }

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public Long getExecutionId() {
        return executionId;
    }

    public String getJobName() {
        return jobName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public Properties getJobParameters() {
        return jobParameters;
    }

    public String getExitStatus() {
        return exitStatus;
    }

}
