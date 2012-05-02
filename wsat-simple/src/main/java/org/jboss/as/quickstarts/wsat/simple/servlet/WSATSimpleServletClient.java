/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.wsat.simple.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.handler.Handler;

import org.jboss.as.quickstarts.wsat.simple.jaxws.RestaurantServiceAT;
import org.jboss.as.quickstarts.wsat.simple.jaxws.RestaurantServiceATService;

import com.arjuna.mw.wst11.UserTransaction;
import com.arjuna.mw.wst11.UserTransactionFactory;
import com.arjuna.mw.wst11.client.JaxWSHeaderContextProcessor;

/**
 * <p>
 * A simple servlet 3 that begins a WS-AtomicTransaction and invokes a Web service. If the call is successful, the transaction is committed.
 * </p>
 * <p/>
 * <p/>
 * The servlet is registered and mapped to /WSATSimpleServletClient using the {@linkplain javax.servlet.annotation.WebServlet}
 *
 * @author Paul Robinson (paul.robinson@redhat.com)
 * @HttpServlet}. </p>
 */
@WebServlet("/WSATSimpleServletClient")
public class WSATSimpleServletClient extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    @WebServiceRef(value=RestaurantServiceATService.class)
    private RestaurantServiceAT client;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        /*
         * Add client handler chain
         */
        BindingProvider bindingProvider = (BindingProvider) client;
        List<Handler> handlers = new ArrayList<Handler>(1);
        handlers.add(new JaxWSHeaderContextProcessor());
        bindingProvider.getBinding().setHandlerChain(handlers);

        /*
         * Lookup the DNS name of the server from the environment and set the endpoint address on the client.
         */
        String openshift = System.getenv("OPENSHIFT_APP_DNS");
        if (openshift != null) {
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://"+openshift+"/RestaurantServiceAT");
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.write("<h1>Quickstart: This example demonstrates the deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a war archive for deployment to *JBoss AS 7*.</h1>");

        System.out.println("[CLIENT] Creating a new WS-AT User Transaction");
        UserTransaction ut = UserTransactionFactory.userTransaction();
        try {
            System.out.println("[CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)");
            ut.begin();
            System.out.println("[CLIENT] invoking makeBooking() on WS");
            client.makeBooking();
            System.out.println("[CLIENT] committing Atomic Transaction (This will cause the AT to complete successfully)");
            ut.commit();

            out.write("<p><i>Go to your JBoss Application Server console or Server log to see the result of the transaction</i></p>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rollbackIfActive(ut);
            client.reset();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }


    /**
     * Utility method for rolling back a transaction if it is currently active.
     *
     * @param ut The User Business Activity to cancel.
     */
    private void rollbackIfActive(UserTransaction ut) {
        try {
            ut.rollback();
        } catch (Throwable th2) {
            // do nothing, not active
        }
    }

}
