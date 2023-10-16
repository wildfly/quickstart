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
package org.jboss.as.quickstarts.cmt;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.logging.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;


public class CustomerIT {

    private static final Logger log = Logger.getLogger(CustomerIT.class.getName());

    protected URI getHTTPEndpoint(String path) {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/cmt";
        }
        try {
            return new URI(host + path);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testAddCustomer() throws Exception {
        addCustomer("BartS");
    }

    public void addCustomer(String name) throws IOException, InterruptedException {
        CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .cookieHandler(manager)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse response = client.send(HttpRequest.newBuilder(getHTTPEndpoint("/addCustomer.jsf")).GET().build(), HttpResponse.BodyHandlers.ofString());
        String body = response.body().toString();
        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(body, body.contains("<input type=\"hidden\" name=\"jakarta.faces.ViewState\" id=\"j_id1:jakarta.faces.ViewState:0\" value=\""));
        int startIndex = body.indexOf("<input type=\"hidden\" name=\"jakarta.faces.ViewState\" id=\"j_id1:jakarta.faces.ViewState:0\" value=\"") + 96;
        int endIndex= body.indexOf('"', startIndex);
        String viewState = body.substring(startIndex, endIndex);
        HttpRequest request = HttpRequest.newBuilder(getHTTPEndpoint("/addCustomer.jsf"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(
                        Map.of(
                            "addCustomerForm", "addCustomerForm",
                            "addCustomerForm:name", name,
                            "addCustomerForm:add", "Add",
                            "jakarta.faces.ViewState", viewState)
                            )
                        )
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
        body = response.body().toString();
        Assert.assertTrue(body, body.contains(name));
    }

    public static BodyPublisher ofFormData(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return BodyPublishers.ofString(builder.toString());
    }

}
