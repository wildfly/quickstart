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

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class RootResource {

    @Inject
    DatabaseBean dbBean;

    @GET
    @Path("/db")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDatabaseEntries() {
        List<TimedEntry> entries = dbBean.loadAllTimedEntries();
        StringBuffer sb = new StringBuffer();
        for (TimedEntry t : entries) {
            sb.append(t);
            sb.append("\n");
        }
        return sb.toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRootResponse() {
        return "MicroProfile Reactive Messaging with Kafka quickstart deployed successfully. You can find the available operations in the included README file.";
    }
}
