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

package org.jboss.as.quickstarts.ejb.client;

import org.jboss.as.quickstarts.ejb.entity.CallerUser;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST endpoints that serves as a simple API to invoke the remote EJBs.
 */
@Path("/")
public class RestEndpoints {

    @EJB
    private RemoteBeanCaller remoteCaller;

    @EJB
    private UsersManagement usersMgmt;

    @GET
    @Path("/remote-outbound-stateless")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> toStatelessRemoteOutbound() throws Exception {
        return remoteCaller.remoteOutboundStatelessBeanCall();
    }

    @GET
    @Path("/remote-outbound-notx-stateless")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> toStatelessRemoteOutboundNoTx() throws Exception {
        return remoteCaller.remoteOutboundStatelessNoTxBeanCall();
    }

    @GET
    @Path("/direct-stateless")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> toStatelessDirect() throws Exception {
        return remoteCaller.directLookupStatelessBeanOverEjbRemotingCall();
    }

    @GET
    @Path("/direct-stateless-http")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> toStatelessDirectHttp() throws Exception {
        return remoteCaller.directLookupStatelessBeanOverHttpCall();
    }

    @GET
    @Path("/remote-outbound-notx-stateful")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> toStatefulRemoteOutboundNoTx() throws Exception {
        return remoteCaller.remoteOutboundStatefulNoTxBeanCall();
    }

    @GET
    @Path("/remote-outbound-fail-stateless")
    @Produces(MediaType.APPLICATION_JSON)
    public String toFailStatelessRemoteOutbound() throws Exception {
        return remoteCaller.remoteOutboundStatelessBeanFail();
    }


    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CallerUser> listUsers() {
        return usersMgmt.getUsers();
    }
}
