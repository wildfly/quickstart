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
package org.jboss.as.quickstarts.ee_security;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
public class BasicRuntimeIT {
    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/ee-security";

    protected URI getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = DEFAULT_SERVER_HOST;
        }
        try {
            return new URI(host + "/secured");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(401, response.statusCode());
        request = HttpRequest.newBuilder(getHTTPEndpoint())
                .header("X-Username", "quickstartUser")
                .header("X-Password", "quickstartPwd1!")
                .GET()
                .build();
        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String[] lines = response.body().toString().split(System.lineSeparator());
        assertEquals("SecuredServlet - doGet()", lines[0].trim());
        assertEquals("Identity as available from SecurityContext 'quickstartUser'", lines[1].trim());
        assertEquals("Identity as available from injection 'quickstartUser'", lines[2].trim());
    }
}