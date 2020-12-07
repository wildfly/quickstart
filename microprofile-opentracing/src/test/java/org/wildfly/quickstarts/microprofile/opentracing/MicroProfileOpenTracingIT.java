/*
 * JBoss, Home of Professional Open Source
 * Copyright 2020, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.microprofile.opentracing;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;

/**
 * Simple tests for MicroProfile OpenTracing quickstart. Arquillian deploys an JAR archive to the application server, which
 * contains several REST endpoint and verifies that they are correctly invoked and the created spans are collected.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
@RunWith(Arquillian.class)
public class MicroProfileOpenTracingIT {

    @ArquillianResource
    private URL deploymentURL;

    private Client client;

    @Inject
    private Tracer tracer;

    @Before
    public void before() {
        client = ClientBuilder.newClient();

        // reset the mocked tracer for the next test
        ((MockTracer) tracer).reset();
    }

    @After
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, JaxRsApplication.class.getPackage())
            .addPackages(true, MockTracer.class.getPackage())
            .addAsServiceProvider(TracerFactory.class, MockTracerFactory.class)
            // enable CDI
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Tests that tracing is triggered as expected.
     */
    @Test
    public void testTrace() {
        appGetInvoke("/hello/traced");

        List<MockSpan> finishedSpans = ((MockTracer) tracer).finishedSpans();

        Assert.assertEquals("Invalid number of created spans", 1, finishedSpans.size());
        Assert.assertEquals("Application created an invalid span", "hello-operation", finishedSpans.get(0).operationName());
    }

    /**
     * Tests that tracing is not triggered when disabled.
     */
    @Test
    public void testExplicitlyDisabledTracing() {
        appGetInvoke("/hello/notTraced");

        List<MockSpan> finishedSpans = ((MockTracer) tracer).finishedSpans();

        Assert.assertEquals("Invalid number of created spans", 0, finishedSpans.size());
    }

    /**
     * Tests that explicit tracing in CDI works as expected.
     */
    @Test
    public void testCDITracing() {
        appGetInvoke("/hello/cdi-trace");

        List<MockSpan> finishedSpans = ((MockTracer) tracer).finishedSpans();

        Assert.assertEquals("Invalid number of created spans", 4, finishedSpans.size());
        finishedSpans.forEach(span -> Assert.assertTrue(span.operationName().equals("process-hello") ||
            span.operationName().equals("prepare-hello") ||
            span.operationName().equals("org.wildfly.quickstarts.microprofile.opentracing.ExplicitlyTracedBean.getHello") ||
            span.operationName().equals("GET:org.wildfly.quickstarts.microprofile.opentracing.TracedResource.cdiHello")));
    }


    private void appGetInvoke(String path) {
        Response response = client
            .target(deploymentURL.toString())
            .path(path)
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());

        response.close();
    }

}
