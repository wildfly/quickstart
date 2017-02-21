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

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import java.util.List;

/**
 * The race batch job component that writes a chunk of processed items.
 * @author Eduardo Martins
 */
@Named
public class BatchRaceStageItemWriter extends AbstractItemWriter {

    @Override
    public void writeItems(List<Object> list) {
        // simulate a item write op, sleep 10ms for each item to write
        try {
            Thread.sleep(10 * list.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
