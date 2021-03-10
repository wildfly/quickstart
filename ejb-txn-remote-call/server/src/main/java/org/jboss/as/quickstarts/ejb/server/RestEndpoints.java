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

package org.jboss.as.quickstarts.ejb.server;

import org.jboss.as.quickstarts.ejb.mock.MockXAResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * REST endpoints to get info on the server processing.
 */
@Path("/")
public class RestEndpoints {

    @GET
    @Path("/commits")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> numberOfMockXAResourceCommits() {
        return Arrays.asList(InfoUtils.getHostInfo(), String.valueOf(MockXAResource.getCommitCount()));
    }
}
