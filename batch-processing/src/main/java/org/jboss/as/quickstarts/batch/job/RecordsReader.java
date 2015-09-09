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
package org.jboss.as.quickstarts.batch.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.batch.model.Contact;

//This class will read the file and return an instance of Contact to be imported
@Named("recordsReader")
public class RecordsReader implements ItemReader {

    // Here you can inject a property defined in import-file.xml
    @Inject
    @BatchProperty
    private String separatorRegex;

    // Here you can inject a property defined in import-file.xml
    @Inject
    @BatchProperty
    private String fileName;

    @Inject
    private Logger log;

    private BufferedReader br;

    private ChunkCheckpoint checkpoint;

    @Override
    public void close() throws Exception {
        br.close();
    }

    @Override
    public void open(Serializable previousCheckpoint) throws Exception {
        // Verify if we have an previous checkpoint
        if (previousCheckpoint == null) {
            this.checkpoint = new ChunkCheckpoint();
        }
        else {
            this.checkpoint = (ChunkCheckpoint) previousCheckpoint;
        }
        br = new BufferedReader(new FileReader(new File(System.getProperty("java.io.tmpdir"), fileName)));
        long lineNumber = checkpoint.getLineNumber();
        if (lineNumber > 0) {
            log.info("Skipping to line " + lineNumber + " as marked by previous checkpoint");
        }
        for (long i = 0; i < lineNumber; i++) {
            br.readLine();
        }
    }

    @Override
    public Contact readItem() throws Exception {
        String line = br.readLine();
        if (line != null) {
            String[] fields = line.split(separatorRegex);
            Contact contact = new Contact();
            contact.setId(Integer.parseInt(fields[0]));
            contact.setName(fields[1]);
            contact.setPhone(fields[2]);
            // update the checkpoint
            checkpoint.increase();
            return contact;
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return checkpoint;
    }

}
