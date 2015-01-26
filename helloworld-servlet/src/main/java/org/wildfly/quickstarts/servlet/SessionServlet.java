/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wildfly.quickstarts.servlet.util.ServletUtil;

@SuppressWarnings("rawtypes")
@WebServlet("/Session")
public class SessionServlet extends HttpServlet {

	private static final long serialVersionUID = -8407250690241513430L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType(ServletUtil.contentType());
		
		PrintWriter out = resp.getWriter();
		
		out.println(ServletUtil.pageHeader("Session Test Example"));
		out.println("<h3>Session Test Example</h3>");
		
		HttpSession session = req.getSession(true);
        out.println("Session ID: " + session.getId());
        out.println("<br>");
        out.println("Created: ");
        out.println(new Date(session.getCreationTime()) + "<br>");
        out.println("Last Accessed: ");
        out.println(new Date(session.getLastAccessedTime()));

        String dataName = req.getParameter("dataname");
        String dataValue = req.getParameter("datavalue");
        if (dataName != null && dataValue != null) {
            session.setAttribute(dataName, dataValue);
        }

        out.println("<P>");
        out.println("The following data is in your session: " + "<br><br>");
        Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = session.getAttribute(name).toString();
            out.println(name + " = " + value + "<br>");
        }

        out.println("<P>");

        out.println("<P>POST based form:<br>");
        out.print("<form action=\"");
        out.print(resp.encodeURL("Session"));
        out.print("\" ");
        out.println("method=POST>");
        out.println("Name of Session Attribute: ");
        out.println("<input type=text size=20 name=dataname>");
        out.println("<br>");
        out.println("Value of Session Attribute: ");
        out.println("<input type=text size=20 name=datavalue>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("<P>GET based form:<br>");
        out.print("<form action=\"");
        out.print(resp.encodeURL("Session"));
        out.print("\" ");
        out.println("method=GET>");
        out.println("Name of Session Attribute: ");
        out.println("<input type=text size=20 name=dataname>");
        out.println("<br>");
        out.println("Value of Session Attribute: ");
        out.println("<input type=text size=20 name=datavalue>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.print("<p><a href=\"");
        out.print(resp.encodeURL("Session?dataname=foo&datavalue=bar"));
        out.println("\" >URL encoded </a>");

		out.println(ServletUtil.pageFooter());
				
		out.close();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	
}
