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
package org.jboss.as.quickstarts.jsonp;

import java.io.StringReader;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

@Model
public class JsonController {

    @Inject
    private Person person;

    private String jsonString;

    private String parsedResult;

    public void generateJson() {
        jsonString = Json.createObjectBuilder()
            .add("name", person.getName())
            .add("age", person.getAge())
            .add("enabled", person.getEnabled())
            .add("phones", Json.createArrayBuilder()
                .add(person.getPhone1())
                .add(person.getPhone2())
            )
            .add("addrees", Json.createObjectBuilder()
                .add("street", person.getAddressStreet())
                .add("apt", person.getAddressApt())
                .add("city", person.getAddressCity())
                .add("zip", person.getAddressZip())
            )
            .build().toString();
    }

    public void parseJsonStream() {
        StringBuilder sb = new StringBuilder();
        String json = getJsonString();
        try {
            JsonParser parser = Json.createParser(new StringReader(json));
            while (parser.hasNext()) {
                Event event = parser.next();
                if (event.equals(Event.KEY_NAME)) {
                    sb.append(" - - - -  >  Key: " + parser.getString() + "  < - - - - - \n");
                }
                if (event.equals(Event.VALUE_STRING)) {
                    sb.append("Value as String: " + parser.getString() + "\n");
                }
                if (event.equals(Event.VALUE_NUMBER)) {
                    sb.append("Value as Number: " + parser.getInt() + "\n");
                }
                if (event.equals(Event.VALUE_TRUE)) {
                    sb.append("Value as Boolean: true\n");
                }
                if (event.equals(Event.VALUE_FALSE)) {
                    sb.append("Value as Boolean: false \n");
                }
            }
        } catch (JsonException e) {
            FacesContext.getCurrentInstance().addMessage("form:parsed", new FacesMessage(e.getMessage()));
        }
        parsedResult = sb.toString();
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

    public String getParsedResult() {
        return parsedResult;
    }

}
