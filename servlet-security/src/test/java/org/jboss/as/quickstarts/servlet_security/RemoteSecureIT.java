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
package org.jboss.as.quickstarts.servlet_security;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */

public class RemoteSecureIT {

    protected URI getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/servlet-security";
        }
        try {
            return new URI(host + "/SecuredServlet");
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
    public void testConnectOk() throws IOException, InterruptedException {
        System.out.println("Endpoint " + getHTTPEndpoint());
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().authenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("quickstartUser", "quickstartPwd1!".toCharArray());
            }
        }).build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        String[] lines = response.body().toString().split(System.lineSeparator());
        Assert.assertEquals("<h1>Successfully called Secured Servlet </h1>", lines[1].trim());
        Assert.assertEquals("<p>Principal  : quickstartUser</p>", lines[2].trim());
        Assert.assertEquals("<p>Remote User : quickstartUser</p>", lines[3].trim());
        Assert.assertEquals("<p>Authentication Type : BASIC</p>", lines[4].trim());
    }

    @Test
    public void testConnectNotOk() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint())
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().authenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("guest", "guestPwd1!".toCharArray());
            }
        }).build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(403, response.statusCode());
    }
}
