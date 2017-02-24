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
package org.jboss.as.quickstarts.managedexecutorservice.rest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.as.quickstarts.managedexecutorservice.concurrency.DeleteTask;
import org.jboss.as.quickstarts.managedexecutorservice.concurrency.LongRunningTask;
import org.jboss.as.quickstarts.managedexecutorservice.concurrency.PersitTask;
import org.jboss.as.quickstarts.managedexecutorservice.model.Product;

@Path("/products")
public class ProductResourceRESTService {

    @Inject
    private Logger log;

    @Resource
    private ManagedExecutorService managedExecutorService;

    // Here we use Instance so PersistTask can have CDI injections available
    @Inject
    private Instance<PersitTask> persisTaskInstance;

    // Here we use Instance so LongRunningTask can have CDI injections available
    @Inject
    private Instance<LongRunningTask> longRunningTaskIntance;

    // Here we use Instance so DeleteTask can have CDI injections available
    @Inject
    private Instance<DeleteTask> deleteTaskInstance;

    /**
     * Creates a new contact from the values provided and will return a JAX-RS response with either 200 ok, or 400 (BAD REQUEST)
     * in case of errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createContact(Product product) {
        Response.ResponseBuilder builder = null;
        try {
            PersitTask pt = persisTaskInstance.get();
            pt.setProduct(product);
            // Store the product on a separated Thread
            log.info("Will create a new Product on other Thread");
            managedExecutorService.execute(pt);

            // Create an "ok" response with the persisted contact
            log.info("Returning response");
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            e.printStackTrace();
            builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }

        return builder.build();
    }

    @GET
    @Path("/longrunningtask")
    public String processLongRunningTask() throws InterruptedException, ExecutionException {
        LongRunningTask lrt = longRunningTaskIntance.get();
        log.info("Submitting a new long running task to be executed");
        Future<Integer> futureResult = managedExecutorService.submit(lrt);
        // wait for the result to be available
        while (!futureResult.isDone()) {
            log.info("Waiting for the result to be available...");
            Thread.sleep(300);
        }
        Integer result = futureResult.get();
        log.info("Result is available. Returning result..." + result);
        // Return the result
        return "Result: " + result;
    }

    @DELETE
    public Response deleteAllProducts() {
        DeleteTask dt = deleteTaskInstance.get();
        // Delete all Products on a separated Thread
        log.info("Will delete all Products on other Thread");
        managedExecutorService.execute(dt);
        log.info("Returning response");
        return Response.ok().build();
    }
}
