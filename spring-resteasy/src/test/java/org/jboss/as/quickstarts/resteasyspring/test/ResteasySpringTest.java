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

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic unit tests for resteasy spring integration
 */
public class ResteasySpringTest {
    @Test
    public void testHelloSpringResource() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPath("/spring-resteasy/hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/basic");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut("http://localhost:8080/spring-resteasy/basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPath("/spring-resteasy/queryParam")
                        .setParameter("param", "hello world")
                        .build();
                HttpGet method = new HttpGet(uri);
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/matrixParam;param=matrix");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("matrix"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/uriParam/1234");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("1234"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
        } finally {
            client.close();
        }
    }

    @Test
    public void testLocatingResource() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPath("/spring-resteasy/locating/hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/locating/basic");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut("http://localhost:8080/spring-resteasy/locating/basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("localhost:8080")
                        .setPath("/spring-resteasy/locating/queryParam")
                        .setParameter("param", "hello world")
                        .build();
                HttpGet method = new HttpGet(uri);
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/locating/matrixParam;param=matrix");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("matrix"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet("http://localhost:8080/spring-resteasy/locating/uriParam/1234");
                CloseableHttpResponse response = client.execute(method);
                try {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("1234"));
                } finally {
                    response.close();
                    method.releaseConnection();
                }
            }
        } finally {
            client.close();
        }
    }

}
