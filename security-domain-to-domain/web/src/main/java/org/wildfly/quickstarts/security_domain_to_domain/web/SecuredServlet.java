/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc. and/or its affiliates, and individual
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

package org.wildfly.quickstarts.security_domain_to_domain.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wildfly.quickstarts.security_domain_to_domain.ejb.ManagementBean;

/**
 * A simple secured servlet that will show information about the current authenticated identity and also information about the
 * representation of the identity as it calls an EJB.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@SuppressWarnings("serial")
@WebServlet("/SecuredServlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "User" }))
public class SecuredServlet extends HttpServlet {

    @EJB
    private ManagementBean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final PrintWriter writer = resp.getWriter();

        writer.println("<html><head><title>security-domain-to-domain</title></head><body>");
        writer.println("<h1>Successfully called Secured Servlet </h1>");
        writer.println("<h2>Identity as visible to servlet.</h2>");
        writer.println(String.format("<p>Principal  : %s</p>", req.getUserPrincipal().getName()));
        writer.println(String.format("<p>Remote User : %s</p>", req.getRemoteUser()));
        writer.println(String.format("<p>Authentication Type : %s</p>", req.getAuthType()));

        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "User", req.isUserInRole("User")));
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "Manager", req.isUserInRole("Manager")));

        writer.println("<h2>Identity as visible to EJB.</h2>");
        writer.println(String.format("<p>Principal  : %s</p>", bean.getCallerPrincipal().getName()));
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "User", bean.userHasRole("User")));
        writer.println(String.format("<p>Caller Has Role '%s'=%b</p>", "Manager", bean.userHasRole("Manager")));

        writer.println("</body></html>");
        writer.close();
    }

}
