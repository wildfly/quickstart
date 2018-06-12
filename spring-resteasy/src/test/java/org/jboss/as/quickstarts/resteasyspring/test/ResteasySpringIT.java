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
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.resteasyspring.GreetingBean;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Basic unit tests for resteasy spring integration
 */

@RunWith(Arquillian.class)
@RunAsClient
public class ResteasySpringIT {
    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class, "spring-resteasy.war")
                .addPackages(true, GreetingBean.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web.xml")
                .addAsWebInfResource("applicationContext.xml")
                .addAsLibraries(Maven.configureResolver()
                        .resolve("org.springframework:spring-web:4.3.9.RELEASE")
                        .withTransitivity().asFile());
    }

    @ArquillianResource
    URL url;

    @Test
    public void testHelloSpringResource() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "basic");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut(url.toString() + "basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
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
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "matrixParam;param=matrix");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("matrix"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "uriParam/1234");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("1234"));
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
                        .setPath(url.getPath() + "locating/hello")
                        .setParameter("name", "JBoss Developer")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("JBoss Developer"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "locating/basic");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("basic"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpPut method = new HttpPut(url.toString() + "locating/basic");
                method.setEntity(new StringEntity("basic", ContentType.TEXT_PLAIN));
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
                } finally {
                    method.releaseConnection();
                }
            }
            {
                URI uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(url.getHost())
                        .setPort(url.getPort())
                        .setPath(url.getPath() + "locating/queryParam")
                        .setParameter("param", "hello world")
                        .build();
                HttpGet method = new HttpGet(uri);
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("hello world"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "locating/matrixParam;param=matrix");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("matrix"));
                } finally {
                    method.releaseConnection();
                }
            }
            {
                HttpGet method = new HttpGet(url.toString() + "locating/uriParam/1234");
                try (CloseableHttpResponse response = client.execute(method)) {
                    Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatusLine().getStatusCode());
                    Assert.assertTrue(EntityUtils.toString(response.getEntity()).equals("1234"));
                } finally {
                    method.releaseConnection();
                }
            }
        }
    }

}
