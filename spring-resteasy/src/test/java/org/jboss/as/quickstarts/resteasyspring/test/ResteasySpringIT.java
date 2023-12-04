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
package org.jboss.as.quickstarts.resteasyspring.test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Basic unit tests for resteasy spring integration
 */

public class ResteasySpringIT {

    static URL url;

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/spring-resteasy";

    @BeforeClass
    public static void setupUrl() throws MalformedURLException {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        url = URI.create(serverHost).toURL();
    }

    @Test
    public void testHelloSpringResource() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "/hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/basic");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut(url.toString() + "/basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
                } finally {
                    method.releaseConnection();
                }
            }
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "queryParam")
                        .setParameter("param", "hello world")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/matrixParam;param=matrix");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertEquals("matrix", EntityUtils.toString(response.getEntity()));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/uriParam/1234");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertEquals("1234", EntityUtils.toString(response.getEntity()));
                } finally {
                    method.releaseConnection();
                }
            }
        }
    }

    @Test
    public void testLocatingResource() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "/locating/hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/locating/basic");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut(url.toString() + "/locating/basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
                } finally {
                    method.releaseConnection();
                }
            }
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "/locating/queryParam")
                        .setParameter("param", "hello world")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/locating/matrixParam;param=matrix");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertEquals("matrix", EntityUtils.toString(response.getEntity()));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "/locating/uriParam/1234");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertEquals("1234", EntityUtils.toString(response.getEntity()));
                } finally {
                    method.releaseConnection();
                }
            }
        }
    }

}
