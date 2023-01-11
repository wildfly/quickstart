/*
 * Copyright 2022 Red Hat, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.as.quickstarts.jaxrsclient.test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ProvisionedManagedContactRestClientIT extends AbstractContactsRestClient {
    private static final String REST_TARGET_URL = "http://localhost:8080";

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Override
    String getRequestUrl() {
        String host = getServerHost();
        if (host == null) {
            host = REST_TARGET_URL;
        }
        try {
            return new URI(host + "/rest/contacts").toURL().toString();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }


    // This test shows basic operations
    @Test
    @Override
    public void cruedTest() {
        super.cruedTest();
    }

    // This test shows some basic operations using ASYNC invocations and java.util.concurrent.Future
    @Test
    @Override
    public void asyncCrudTest() throws Exception {
        super.asyncCrudTest();
    }

    // This test shows how to use jakarta.ws.rs.client.InvocationCallback
    @Test
    @Override
    public void invocationCallBackTest() throws Exception {
        super.invocationCallBackTest();
    }

    // Shows how to use a delayed REST invocation
    @Test
    @Override
    public void delayedInvocationTest() throws Exception {
        super.delayedInvocationTest();
    }

    // Shows how to use Request and Response filters
    @Test
    @Override
    public void requestResponseFiltersTest() {
        super.requestResponseFiltersTest();
    }
}
