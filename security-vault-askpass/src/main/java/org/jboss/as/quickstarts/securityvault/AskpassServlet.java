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
package org.jboss.as.quickstarts.securityvault;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * A simple Servlet which tries to access the Datasource secured by Vault.
 *
 * @author Ivo Studensky
 *
 */
@SuppressWarnings("serial")
@WebServlet("/AskpassServlet")
public class AskpassServlet extends HttpServlet {

    private static String PAGE_HEADER = "<html><head><title>security-vault-askpass-servlet</title></head><body>";

    private static String PAGE_FOOTER = "</body></html>";

    @Resource(mappedName = "java:jboss/datasources/SecurityVaultDS")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Security Vault Askpass Servlet " + "</h1>");
        try {
            boolean canConnect = ds.getConnection().isValid(5);
            writer.println("The connection to the datasource is valid? " + canConnect);
        } catch (SQLException e) {
            writer.println("ERROR: Could not connect to the datasource due to " + e);
            writer.println("<pre>");
            e.printStackTrace(writer);
            writer.println("</pre>");
        }
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}
