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
package org.jboss.as.quickstarts.threadracing.stage.json;

import org.jboss.as.quickstarts.threadracing.Race;
import org.jboss.as.quickstarts.threadracing.stage.RaceStage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * The JSON 1.0 race stage shows how work with the new API for common use cases: building of a JSON object, writing a JSON object to a string/stream, and reading a JSON object from a string/stream.
 *
 * @author Eduardo Martins
 */
public class JSONRaceStage implements RaceStage {

    @Override
    public void run(Race.Registration registration) throws Exception {
        // 1. build an object with nested structure
        JsonObject jsonObject = Json.createObjectBuilder()
            // simple pairs
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            // nested object
            .add("address", Json.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021"))
            // nested object array
            .add("phoneNumber", Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567")))
            .build();
        // 2. write the object to a string
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = Json.createWriter(stringWriter)) {
            writer.write(jsonObject);
        }
        String toString = stringWriter.toString();
        // 3. read object from the string
        JsonObject fromString = null;
        try (JsonReader jsonReader = Json.createReader(new StringReader(toString))) {
            fromString = jsonReader.readObject();
        }
        // 4. sanity check :)
        if (!jsonObject.equals(fromString)) {
            throw new IllegalStateException("json object read from string does not equal the one built");
        }
    }
}
