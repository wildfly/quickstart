/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;

import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TimedEntryDeserializer implements Deserializer<TimedEntry> {

    @Override
    public TimedEntry deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))){
            Timestamp time = new Timestamp(in.readLong());
            String message = in.readUTF();
            return new TimedEntry(time, message);
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
