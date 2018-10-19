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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.batch.operations.JobOperator;
import javax.batch.operations.NoSuchJobException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

//The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
//EL name
//Read more about the @Model stereotype in this FAQ:
//http://www.cdi-spec.org/faq/#accordion6
@Model
public class BatchController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger log;

    private String fileName = "temp-file.txt";

    private boolean generateWithError;

    @Min(0)
    @Max(1000000)
    private Integer numRecords = 10;

    public void generate() throws IOException {
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        try (BufferedWriter bos = new BufferedWriter(new FileWriter(tempFile, false))) {
            log.info("Starting to generate " + numRecords + " records in file " + tempFile);
            String previousName = null;
            final Random random = new Random();
            for (int x = 0; x < numRecords; x++) {
                // generate random name
                String name = random.ints('a','z'+1)
                        .limit(10)
                        .collect(StringBuilder::new, (sb, i) -> sb.append((char) i), StringBuilder::append)
                        .toString();
                // generate random phone number
                String phone = random.ints('0','9'+1)
                        .limit(9)
                        .collect(StringBuilder::new, (sb, i) -> sb.append((char) i), StringBuilder::append)
                        .toString();
                // Generate a duplicate name;
                if (generateWithError && x == (numRecords / 2)) {
                    name = previousName;
                }
                String record = (x + 1) + "|" + name + "|" + phone;
                bos.write(record + "\n");
                previousName = name;
            }
            log.info("File generated at " + tempFile);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "File generated with " + numRecords + " records to be imported. File name: " + getFileName(), null));
            if (generateWithError) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attention: This file contains duplicate records for test purpose.", null));
            }
        }
    }

    public void generateFileAndStarJob() throws IOException {
        generate();
        startImport();
    }

    public void startImport() {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        jobParameters.setProperty("fileName", getFileName());
        long execID = jobOperator.start("import-file", jobParameters);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New job execution #" + execID + " running. Importing file:" + getFileName(), null));
    }

    public Set<JobData> getJobsExecution() {
        Set<JobData> jobsData = new TreeSet<JobData>(new Comparator<JobData>() {

            @Override
            public int compare(JobData o1, JobData o2) {
                return o2.getJobInstanceId().compareTo(o1.getJobInstanceId());
            }
        });
        Map<Long, JobExecution> jobIntances = new HashMap<>();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        try {
            List<JobInstance> instances = jobOperator.getJobInstances("import-file", 0, jobOperator.getJobInstanceCount("import-file"));
            for (JobInstance ji : instances) {
                List<JobExecution> executions = jobOperator.getJobExecutions(ji);
                for (JobExecution jobExecution : executions) {
                    // initialize the map if null
                    if (jobIntances.get(ji.getInstanceId()) == null) {
                        jobIntances.put(ji.getInstanceId(), jobExecution);
                    }
                    // Update the jobExecution if is newer
                    JobExecution existing = jobIntances.get(ji.getInstanceId());
                    if (jobExecution.getExecutionId() > existing.getExecutionId()) {
                        jobIntances.put(ji.getInstanceId(), jobExecution);
                    }
                }
            }
            for (Long instaceId : jobIntances.keySet()) {
                JobExecution jobExecution = jobIntances.get(instaceId);
                JobInstance ji = jobOperator.getJobInstance(jobExecution.getExecutionId());
                Properties parameters = jobOperator.getParameters(jobExecution.getExecutionId());
                jobsData.add(new JobData(ji.getInstanceId(), jobExecution.getExecutionId(), ji.getJobName(), jobExecution.getCreateTime(), jobExecution.getEndTime(), jobExecution
                    .getBatchStatus(), parameters, jobExecution.getExitStatus()));
            }
        } catch (NoSuchJobException e) {
            // It's ok if when doesn't have any jobs yet to show
        }

        return jobsData;
    }

    public void restartJob(int executionId) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = jobOperator.getParameters(executionId);
        jobParameters.setProperty("restartedOnce", "true");
        long newExecutionId = jobOperator.restart(executionId, jobParameters);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Execution " + executionId + " restarted! New execution id: " + newExecutionId, null));
    }

    public Integer getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(Integer numRecords) {
        this.numRecords = numRecords;
    }

    public boolean isGenerateWithError() {
        return generateWithError;
    }

    public void setGenerateWithError(boolean generateWithError) {
        this.generateWithError = generateWithError;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
