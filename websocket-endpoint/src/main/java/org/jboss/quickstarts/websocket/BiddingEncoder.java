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

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder.Text;
import javax.websocket.EndpointConfig;

import org.jboss.quickstarts.websocket.model.Bid;
import org.jboss.quickstarts.websocket.model.Bidding;

// This class is responsible to encode the Bidding object in a String
// that will be sent to clients
public class BiddingEncoder implements Text<Bidding> {

    @Override
    public void destroy() {

    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public String encode(Bidding bidding) throws EncodeException {
        // It uses the JSON-P API to create a JSON representation
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
            .add("item", Json.createObjectBuilder()
                .add("buyNowPrice", bidding.getItem().getBuyNowPrice())
                .add("description", bidding.getItem().getDescription())
                .add("imagePath", bidding.getItem().getImagePath())
                .add("title", bidding.getItem().getTitle())
                .build())
            .add("bidStatus", bidding.getBidStatus().toString())
            .add("currentPrice", bidding.getCurrentPrice())
            .add("secondsLeft", 0);
        if (bidding.getDueDate() != null) {
            jsonBuilder.add("dueDate", bidding.getDueDate().getTime());
        }
        if (bidding.getSecondsLeft() != null) {
            jsonBuilder.add("secondsLeft", bidding.getSecondsLeft());
        }
        JsonArrayBuilder jsonBidArray = Json.createArrayBuilder();
        for (Bid bid : bidding.getBids()) {
            jsonBidArray.add(Json.createObjectBuilder()
                .add("dateTime", bid.getDateTime().getTime())
                .add("value", bid.getValue())
                .add("id", bid.getId())
                .build());
        }
        jsonBuilder.add("bids", jsonBidArray);
        StringWriter stWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stWriter);
        jsonWriter.writeObject(jsonBuilder.build());
        jsonWriter.close();
        return stWriter.toString();
    }
}
