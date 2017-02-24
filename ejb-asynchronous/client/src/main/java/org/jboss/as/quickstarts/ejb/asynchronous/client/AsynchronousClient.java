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
package org.jboss.as.quickstarts.ejb.asynchronous.client;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.ejb.asynchronous.AsynchronousAccess;
import org.jboss.as.quickstarts.ejb.asynchronous.ParallelAccess;

/**
 * A client to call the SingletonService via EJB remoting to demonstrate the behaviour of asynchronous invocations.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class AsynchronousClient {
    private static final Logger LOGGER = Logger.getLogger(AsynchronousClient.class.getName());
    /**
     * Proxy of the SLSB with asynchronous methods
     */
    private final AsynchronousAccess accessBean;
    /**
     * Proxy of the SLSB that uses asynchronous bean calls inside to parallelize internal actions.
     */
    private final ParallelAccess parallelBean;

    /**
     * Constructor to prepare the client-context.<br/>
     * There must be a jboss-ejb-client.properties file in the classpath to specify the server connection(s).
     *
     * @throws NamingException
     */
    private AsynchronousClient() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        String lookupName = "ejb:/ejb-asynchronous-ejb/AsynchronousAccessBean!" + AsynchronousAccess.class.getName();
        LOGGER.info("Lookup Bean >" + lookupName);
        accessBean = (AsynchronousAccess) context.lookup(lookupName);
        lookupName = "ejb:/ejb-asynchronous-ejb/ParallelAccessBean!" + ParallelAccess.class.getName();
        LOGGER.info("Lookup Bean >" + lookupName);
        parallelBean = (ParallelAccess) context.lookup(lookupName);
    }

    /**
     * Demonstrate a fire-and-forget call to an asynchronous bean.
     *
     * @throws InterruptedException
     */
    private void fireAndForget() throws InterruptedException {
        long sleepMillis = 15000;
        accessBean.fireAndForget(sleepMillis);
        LOGGER.info(String.format("The server log should contain a message at (about) %s, indicating that the call to the asynchronous bean completed.",
            new Date(new Date().getTime() + sleepMillis)));
    }

    /**
     * Demonstrate how to call an asynchronous EJB, and then perform another task whilst waiting for the result.
     * If the result is not present after the timeout of get(<timeout>) the result will be ignored.
     *
     * @throws TimeoutException Will be thrown if you change the timing
     */
    private void getResultAsync() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> myResult = accessBean.longerRunning(200); // Start a call with a short duration
        // simulate something
        // wait below 200ms will force a timeout during get
        Thread.sleep(400);
        // If you handle the TimeoutException you are able to ignore the result
        // WARNING: there might be an ERROR at server side that the result is not delivered
        LOGGER.info("Got the async result as expected => " + myResult.get(1, TimeUnit.MILLISECONDS));
    }

    /**
     * Demonstrate how to call an asynchronous EJB and continue local work meanwhile. After finishing local work
     * wait for the result of the server call.<br/>
     * Remember that the call of Future.get() will have a remote roundtrip to the server.
     */
    private void waitForAsyncResult() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> myResult = accessBean.longerRunning(1500); // Start a call with a short duration
        // you might do something here

        // get() without a timeout will wait until the remote result is present.
        LOGGER.info("Got the async result as expected after wait => " + myResult.get());
    }

    /**
     * Invoke a remote synchronous EJB method. The remote method uses asynchronous calls internally, to parallelize it's workload.
     */
    private void callAnEJBwithAsyncAccess() {
        Collection<String> results = parallelBean.invokeAsyncParallel();
        LOGGER.info("Results of the parallel (server) processing : " + results);
    }

    /**
     * Demonstrate how a EJB can call different annotated asynchronous methods within the same application.
     */
    private void waitForAnotherAsyncResult2() throws InterruptedException, ExecutionException, TimeoutException {
        parallelBean.callInterfaceAnnotatedMethod();
    }

    /**
     * Demonstrate how to handle an Exception from an asynchronous call.
     */
    private void callAsyncWithFailure() throws InterruptedException {
        Future<String> x;
        try {
            x = accessBean.failure(); // this method will return successfully, because the invocation will be successful!
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected failure during start asynchronous execution!", e);
        }
        try {
            x.get(); // this will not return successfully
        } catch (ExecutionException e) {
            // the IllegalAccessException is thrown by the bean method
            if (e.getCause() instanceof IllegalAccessException) {
                // This is the expected behavior
                LOGGER.info("Catch the expected Exception of the asynchronous execution!");
            } else if (e.getCause().getCause() instanceof IllegalAccessException) {
                LOGGER.info("Catch the covered Exception of the asynchronous execution, you may be using an older release of JBoss EAP!");
            } else {
                throw new RuntimeException("Unexpected ExecutionException during asynchronous call!", e);
            }
        }
    }

    /**
     * Call all the different asynchronous methods.
     *
     * @param args no arguments needed
     */
    public static void main(String[] args) throws Exception {
        AsynchronousClient client = new AsynchronousClient();

        client.fireAndForget();
        client.getResultAsync();
        client.waitForAsyncResult();

        client.callAsyncWithFailure();

        client.callAnEJBwithAsyncAccess();
        client.waitForAnotherAsyncResult2();
    }

}
