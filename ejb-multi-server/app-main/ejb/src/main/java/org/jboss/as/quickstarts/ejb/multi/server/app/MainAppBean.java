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
package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;
import java.util.Hashtable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb3.annotation.SecurityDomain;

import org.jboss.logging.Logger;

/**
 * <p>The main bean called by the standalone client.</p>
 * <p>The sub applications, deployed in different servers are called direct or via indirect naming to hide the lookup name and use
 * a configured name via comp/env environment.</p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
@SecurityDomain("other")
public class MainAppBean implements MainApp {
    private static final Logger LOGGER = Logger.getLogger(MainAppBean.class);
    @Resource
    SessionContext context;

    /**
     * The context to invoke foreign EJB's as the SessionContext can not be used for that.
     */
    private InitialContext iCtx;

    @EJB(lookup = "ejb:ejb-multi-server-app-one/ejb//AppOneBean!org.jboss.as.quickstarts.ejb.multi.server.app.AppOne")
    AppOne appOneProxy;

    /**
     * Initialize and store the context for the EJB invocations.
     */
    @PostConstruct
    public void init() {
        try {
            final Hashtable<String, String> p = new Hashtable<>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.iCtx = new InitialContext(p);
        } catch (NamingException e) {
            throw new RuntimeException("Could not initialize context", e);
        }
    }

    @Override
    public String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }

    @Override
    public String invokeAll(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("[" + caller.getName() + "] " + text);
        final StringBuilder result = new StringBuilder("MainApp[" + caller.getName() + "]@" + System.getProperty("jboss.node.name"));

        // Call AppOne with the direct ejb: naming
        try {
            result.append("  >  [ " + invokeAppOne(text));
        } catch (Exception e) {
            LOGGER.error("Could not invoke AppOne", e);
        }

        String lookup = "";
        // Call AppTwo with the direct ejb: naming
        try {
            lookup = "ejb:ejb-multi-server-app-two/ejb//AppTwoBean!" + AppTwo.class.getName();
            result.append(" > " + invokeAppTwo(lookup, text));
            LOGGER.info("Invoke '" + lookup + " OK");
        } catch (Exception e) {
            LOGGER.error("Could not invoke apptwo '" + lookup + "'", e);
        }

        // Call AppTwo by using the local alias configured in
        // META-INF/ejb-jar.xml
        try {
            lookup = "java:comp/env/AppTwoAlias";
            result.append(" ; " + invokeAppTwo(lookup, text));
            LOGGER.info("Invoke '" + lookup + " OK");
        } catch (Exception e) {
            LOGGER.error("Could not invoke apptwo '" + lookup + "'", e);
        }

        result.append(" ]");

        return result.toString();
    }

    /**
     * The application one can only be called with the standard naming, there is
     * no alias.
     *
     * @param text
     *            Simple text for logging in the target servers logfile
     * @return A text with server details for demonstration
     */
    private String invokeAppOne(String text) {
        try {
            // invoke on the bean
            final String appOneResult = this.appOneProxy.invoke(text);

            LOGGER.info("AppOne return : " + appOneResult);
            return appOneResult;
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke appOne", e);
        }
    }

    /**
     * The application two can be called via lookup.
     * <ul>
     * <li>with the standard naming
     * <i>ejb:ejb-multi-server-app-two/ejb//AppTwoBean!org.jboss.as.quickstarts
     * .ejb.multi.server.app.AppTwo</i></li>
     * <li><i>java:global/AliasAppTwo</i> the alias provided by the server
     * configuration <b>this is not recommended</b></li>
     * <li><i>java:comp/env/AppTwoAlias</i> the local alias provided by the
     * ejb-jar.xml configuration</li>
     * </ul>
     *
     * @param text
     *            Simple text for logging in the target servers logfile
     * @return A text with server details for demonstration
     */
    private String invokeAppTwo(String lookup, String text) throws NamingException {
        final AppTwo bean = (AppTwo) iCtx.lookup(lookup);

        // invoke on the bean
        final String appTwoResult = bean.invoke(text);

        LOGGER.info("AppTwo return : " + appTwoResult);
        return appTwoResult;
    }
}
