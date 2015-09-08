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
package org.jboss.quickstarts.websocket;

import java.io.StringReader;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

// This class is responsible to "decode" the received String
// from clients in a Message object
public class MessageDecoder implements Decoder.Text<Message> {

    private Logger logger = Logger.getLogger(getClass().getName());

    // create a Message object from JSON
    @Override
    public Message decode(String msg) throws DecodeException {
        logger.info("Decoding: " + msg);
        // It uses the JSON-P API to parse JSON content
        JsonReader reader = Json.createReader(new StringReader(msg));
        JsonObject jsonObject = reader.readObject();
        String command = jsonObject.getString("command");
        Integer bidValue = null;
        if (jsonObject.containsKey("bidValue")) {
            bidValue = jsonObject.getInt("bidValue");
        }
        return new Message(command, bidValue);
    }

    @Override
    public boolean willDecode(String msg) {
        return true;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }
}
