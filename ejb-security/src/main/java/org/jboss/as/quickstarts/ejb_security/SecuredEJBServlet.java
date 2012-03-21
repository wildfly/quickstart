/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates,
 * and individual contributors as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2012,
 * @author Sherif Makary */

package org.jboss.as.quickstarts.ejb_security;

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

import org.jboss.as.quickstarts.ejb_security.SecuredEJB;

/**
 * A simple secured Servlet which calls a secured EJB. Upon successful authentication and authorization the
 * EJB will return the principal's name. Servlet security is implemented using annotations.
 * 
 * @author Sherif Makary
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/SecuredEJBServlet")
@ServletSecurity(@HttpConstraint(rolesAllowed="guest"))
public class SecuredEJBServlet extends HttpServlet {

   private static String PAGE_HEADER = "<html><head /><body>";

   private static String PAGE_FOOTER = "</body></html>";

   // Inject the Secured EJB
   @EJB
   private SecuredEJB securedEJB;

   /**
    * Servlet entry point method which calls securedEJB.getSecurityInfo()
    */
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      PrintWriter writer = resp.getWriter();
      String principal = null;
      String authType = null;
      String remoteUser = null;

      // Get security principal
      principal = securedEJB.getSecurityInfo();
      // Get user name from login principal
      remoteUser = req.getRemoteUser();
      // Get authentication type
      authType = req.getAuthType();

      writer.println(PAGE_HEADER);
      writer.println("<h1>" + "Successfully called Secured EJB " + "</h1>");
      writer.println("<p>" + "Principal  : " + principal + "</p>");
      writer.println("<p>" + "Remote User : " + remoteUser + "</p>");
      writer.println("<p>" + "Authentication Type : " + authType + "</p>");
      writer.println(PAGE_FOOTER);
      writer.close();
   }

}
