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
package org.jboss.as.quickstarts.bmt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A servlet which shows how to manually manage transactions and database resources using
 * both managed (session bean) and non-managed (simple CDI pojo) components. Reasons
 * why the developer might want to control transaction demarcation are various, for example
 * application requirements may need to access synchronizations or there may be XAResources
 * that are not supported by the JEE container.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /BMT using the {@linkplain WebServlet
 * @HttpServlet}. The {@link ManagedComponent} and {@link UnManagedComponent} are injected by CDI.
 * </p>
 *
 * @author Mike Musgrove
 *
 */

@WebServlet("/BMT")
public class TransactionServlet extends HttpServlet {
    /** Default value included to remove warning. **/
    private static final long serialVersionUID = 1L;

    static String PAGE_HEADER = "<html><head><title>bmt</title></head><body>";

    static String PAGE_CONTENT = "<h1>Stepping Outside the Container (with JPA and JTA)</h1>"
        + "<form>"
        + "<input checked type=\"checkbox\" name=\"strategy\" value=\"managed\" /> Use bean managed Entity Managers <br />"
        + "Key: <input type=\"text\" name=\"key\" /><br />"
        + "Value: <input type=\"text\" name=\"value\" /><br />"
        + "<input type=\"submit\" value=\"Submit\" /><br />"
        + "</form>";

    static String PAGE_FOOTER = "</body></html>";

    /*
     * Inject a stateless bean. Although stateless beans are thread safe it is probably not a solution that scales particularly
     * well.
     */
    @Inject
    ManagedComponent managedBean;

    /*
     * Create a CDI POJO that will manage both transactions and the JPA EntityManager itself
     */
    @Inject
    UnManagedComponent unManagedBean;

    /**
     * <p>
     * Servlet entry point.
     * </p>
     * <p>
     * The behaviour of the servlet is controlled by servlet query parameter or form parameters. If parameters named "key" and
     * "value" are present then that pair is added (or the key is updated if it already exists) to the database. If the form
     * parameter "strategy" is not set to the value "managed" then both the transaction and the EntityManager are controlled
     * manually. Otherwise the Entity Manager is controlled by the container and the transaction is controlled by the developer.
     * </p>
     *
     * @param req the HTTP request
     * @param resp the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String responseText;
        String key = req.getParameter("key");
        String value = req.getParameter("value");
        String txStrategy = req.getParameter("strategy");

        if ("managed".equalsIgnoreCase(txStrategy))
            responseText = managedBean.updateKeyValueDatabase(key, value);
        else
            responseText = unManagedBean.updateKeyValueDatabase(key, value);

        writer.println(PAGE_HEADER);
        writer.println(PAGE_CONTENT);
        writer.println("<p>" + responseText + "</p>");
        writer.println(PAGE_FOOTER);

        writer.close();
    }

}
