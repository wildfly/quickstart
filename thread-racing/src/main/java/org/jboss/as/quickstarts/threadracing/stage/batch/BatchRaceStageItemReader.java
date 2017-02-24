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

import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The race batch job component that reads items for chunk processing.
 *
 * @author Eduardo Martins
 */
@Named
public class BatchRaceStageItemReader extends AbstractItemReader {

    /**
     * random temperature simulator
     */
    private static final Random random = new Random();

    /**
     * the items to read iterator
     */
    private Iterator<Object> items;

    /**
     * This method is implemented when the ItemReader requires any open time processing.
     * @param checkpoint
     * @throws Exception
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
        // retrieve the items to read, let's just simulate something, a list containing a random number of objects
        final List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < random.nextInt(50); i++) {
            list.add(new Object());
        }
        items = list.iterator();
    }

    /**
     * The method that reads 1 item to be processed.
     * @return
     */
    @Override
    public Object readItem() {
        return items.hasNext() ? items.next() : null;
    }
}
