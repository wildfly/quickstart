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

import java.util.List;
import java.util.logging.Logger;

import javax.batch.api.chunk.listener.AbstractItemWriteListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

//This listener updates the exit status to show the exception
@Named("persistListener")
public class PersistListener extends AbstractItemWriteListener {

    @Inject
    private Logger log;

    @Inject
    private JobContext jobContext;

    @Override
    public void afterWrite(List<Object> items) throws Exception {
        log.info("Persisting " + items.size() + " contacts");
    }

    @Override
    public void beforeWrite(List<Object> items) throws Exception {
        log.info("Preparing to persist " + items.size() + " contacts");
    }

    // Write a custom Exit status
    @Override
    public void onWriteError(List<Object> items, Exception ex) throws Exception {
        log.info("Exception detected. Setting exit status");
        jobContext.setExitStatus("Error : " + ex.getMessage());
    }
}
