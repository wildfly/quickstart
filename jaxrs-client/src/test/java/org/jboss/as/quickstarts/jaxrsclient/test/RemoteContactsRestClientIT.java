/*
 * JBoss, Home of Professional Open Source
 * Copyright 2022, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.jaxrsclient.test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

public class RemoteContactsRestClientIT extends AbstractContactsRestClient {

    private static final String REST_TARGET_URL = "http://localhost:8080/jaxrs-client";

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
