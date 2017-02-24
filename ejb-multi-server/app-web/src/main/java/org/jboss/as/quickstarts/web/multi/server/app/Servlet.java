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
package org.jboss.as.quickstarts.web.multi.server.app;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.as.quickstarts.ejb.multi.server.app.AppOne;
import org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo;

/**
 * A simple servlet that is used to invoke all other application EJB's.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@WebServlet(urlPatterns = "/*")
public class Servlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Servlet.class.getName());
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        boolean fail = false;

        LOGGER.info("Servlet is called " + new Date());

        response.setContentType("html");
        write(response, "<h1>Example Servlet to show how EJB's can be invoked</h1>");
        write(response, "The node.names are read from the destination server via EJB invocation.<br/>");
        write(response, "It shows the name of the host instance (host-controller) and the unique server name on this host.<br/>");

        try {
            final InitialContext iCtx = getContext();

            write(response, "<h2>Invoke AppOne on different server</h2>");
            try {
                AppOne proxy = (AppOne) lookup(response, iCtx,
                    "ejb:ejb-multi-server-app-one/ejb//AppOneBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppOne");
                if (proxy != null) {
                    write(response, "Invocation #1 return node.name => " + proxy.getJBossNodeName() + "<br/>");
                    // second invocation shows whether the same or a different node is reached
                    write(response, "Invocation #2 return node.name => " + proxy.getJBossNodeName() + "<br/>");
                } else {
                    fail = true;
                }
            } catch (Exception n) {
                LOGGER.log(Level.SEVERE, "Failed to invoke AppOne", n);
                write(response, "Failed to invoke AppOne<br/>");
                write(response, n.getMessage());
                fail = true;
            }
            write(response, "<h2>Invoke AppTwo on different server</h2>");
            try {
                AppTwo proxy = (AppTwo) lookup(response, iCtx,
                    "ejb:ejb-multi-server-app-two/ejb//AppTwoBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppTwo");
                if (proxy != null) {
                    write(response, "Invocation #1 return node.name => " + proxy.getJBossNodeName() + "<br/>");
                    // second invocation shows whether the same or a different node is reached
                    write(response, "Invocation #2 return node.name => " + proxy.getJBossNodeName() + "<br/>");
                } else {
                    fail = true;
                }
            } catch (Exception n) {
                LOGGER.log(Level.SEVERE, "Failed to invoke AppTwo", n);
                write(response, "Failed to invoke AppTwo<br/>");
                write(response, n.getMessage());
                fail = true;
            }
        } catch (NamingException e) {
            write(response, "<h2>Failed to initialize InitialContext</h2>");
            write(response, e.getMessage());
        }

        if (fail) {
            write(response,
                "<br/><br/><br/><p><b><i>Not all invocations are successful, see <i>EAP_HOME</i>/domain/servers/app-web/log/server.log</i></b></p>");
        } else {
            write(response, "<br/><br/><br/><p><i>All invocations are successful</i></p>");
        }
        write(response, "<p>Finish at " + new Date() + "</p>");
    }

    private static void write(HttpServletResponse writer, String message) {

        try {
            writer.getWriter().write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InitialContext getContext() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(jndiProperties);
    }

    private Object lookup(HttpServletResponse response, InitialContext ic, String name) {
        try {
            Object proxy = ic.lookup(name);
            if (proxy == null) {
                write(response, "lookup(" + name + ") returns no proxy object");
            }
            return proxy;
        } catch (NamingException e) {
            write(response, "Failed to lookup(" + name + ")");
            return null;
        }
    }
}
