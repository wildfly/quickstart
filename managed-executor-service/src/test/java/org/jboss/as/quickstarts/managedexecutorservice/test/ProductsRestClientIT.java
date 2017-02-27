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
package org.jboss.as.quickstarts.managedexecutorservice.test;

import java.net.URL;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.quickstarts.managedexecutorservice.concurrency.DeleteTask;
import org.jboss.as.quickstarts.managedexecutorservice.model.Product;
import org.jboss.as.quickstarts.managedexecutorservice.rest.JaxRsActivator;
import org.jboss.as.quickstarts.managedexecutorservice.util.Resources;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
@RunAsClient
public class ProductsRestClientIT {

    //private static final String REST_TARGET_URL = "http://localhost:8080/managed-executor-service/rest/products";

    private Logger log = Logger.getLogger(ProductsRestClientIT.class.getName());

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war =  ShrinkWrap.create(WebArchive.class, "managed-executor-service.war")
                .addPackage(JaxRsActivator.class.getPackage())
                .addPackage(DeleteTask.class.getPackage())
                .addPackage(Product.class.getPackage())
                .addPackage(Resources.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-ds.xml", "quickstart-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                ;

        return war;
    }

    private String getRequestUrl(String path) {
        StringBuffer sb = new StringBuffer(deploymentUrl.toString())
                .append("rest/products");
        if (path != null) {
            sb.append(path);
        }
        return sb.toString();
    }

    // This test shows basic operations
    @Test
    public void testRestResources() {
        log.info("creating a new product");
        Product c = new Product();
        c.setName("A Product");
        c.setPrice(100);
        Response response = ClientBuilder.newClient().target(getRequestUrl(null)).request().post(Entity.entity(c, MediaType.APPLICATION_JSON), Response.class);
        Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());

        log.info("Product created. Executing a long running task");
        String result = ClientBuilder.newClient().target(getRequestUrl("/longrunningtask")).request().get(String.class);
        Assert.assertTrue(result.startsWith("Result:"));

        log.info("Deleting all products");
        response = ClientBuilder.newClient().target(getRequestUrl(null)).request().delete();
        Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
    }
}
