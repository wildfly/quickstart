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
package org.jboss.as.quickstarts.servlet.filterlistener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple servlet that exists as a target for Servlet Filters and Servlet Listeners.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@SuppressWarnings("serial")
@WebServlet("/FilterExample")
public class FilterExampleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<!DOCTYPE HTML>");
        writer.println("<html>");
        writer.println(" <head>");
        writer.println("  <title>servlet-filterlistener</title>");
        writer.println(" </head>");
        writer.println(" <body>");
        writer.println("  <form>");
        writer.println("   <label for=userInput>Enter some text:</label> <input type=text name=userInput>");
        writer.println("   <button type=submit>Send</button></form>");
        writer.println("  </form>");

        if (req.getParameter("userInput") != null) {
            writer.println("  <h1>You Typed: " + req.getParameter("userInput") + "</h1>");
        }

        writer.println(" </body>");
        writer.println("</html>");
        writer.close();
    }

}
