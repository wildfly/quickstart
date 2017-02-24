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
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

/**
 * <p>
 * An example how to use the scoped context in JBoss EAP.<br/>
 * If a server without that feature is used the outbound-connection will be used
 * and the behavior is different.
 * </p>
 * <p>
 * The functionality will be the same as MainAppBean provide, the interface
 * @{link MainApp} will be shared to use both.<br/>
 * The sub applications, deployed in different servers are called direct by
 * using the ejb-client scoped context properties.
 * </p>
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class MainAppSContextBean implements MainApp {
    private static final Logger LOGGER = Logger.getLogger(MainAppSContextBean.class);

    private static Integer activeInstances = 0;
    private static Context appOneScopedEjbContext;
    private static Context appTwoScopedEjbContextA;
    private static Context appTwoScopedEjbContextB;

    @Resource
    SessionContext context;

    private void createAppOneContext() {
        final Properties ejbClientContextProps = new Properties();
        ejbClientContextProps.put("endpoint.name", "appMain->appOne_endpoint");
        // --- Property to enable scoped EJB client context which will be tied
        // to the JNDI context ---
        ejbClientContextProps.put("org.jboss.ejb.client.scoped.context", true);
        // Property which will handle the ejb: namespace during JNDI lookup
        ejbClientContextProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        ejbClientContextProps.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        // add a property which lists the connections that we are configuring.
        // In this example, we are just configuring a single connection.
        final String connectionName = "appOneConnection";
        ejbClientContextProps.put("remote.connections", connectionName);
        // add the properties to connect the app-one host
        ejbClientContextProps.put("remote.connection." + connectionName + ".host", "localhost");
        ejbClientContextProps.put("remote.connection." + connectionName + ".port", "8180");
        ejbClientContextProps.put("remote.connection." + connectionName + ".username", "quickuser1");
        ejbClientContextProps.put("remote.connection." + connectionName + ".password", "quick123+");
        ejbClientContextProps.put("remote.clusters", "ejb");
        ejbClientContextProps.put("remote.cluster.ejb.username", "quickuser2");
        ejbClientContextProps.put("remote.cluster.ejb.password", "quick+123");

        try {
            appOneScopedEjbContext = (Context) new InitialContext(ejbClientContextProps).lookup("ejb:");
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appOne')", e);
        }
    }

    private void createAppTwoContext() {
        final Properties ejbClientContextProps = new Properties();
        ejbClientContextProps.put("endpoint.name", "appMain->appTwoA_endpoint");
        // Property to enable scoped EJB client context which will be tied to
        // the JNDI context
        ejbClientContextProps.put("org.jboss.ejb.client.scoped.context", true);
        // Property which will handle the ejb: namespace during JNDI lookup
        ejbClientContextProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

        final String connectionName = "appTwoConnection";
        ejbClientContextProps.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        // add the properties to connect the app-one host
        ejbClientContextProps.put("remote.connections", connectionName);
        ejbClientContextProps.put("remote.connection." + connectionName + ".host", "localhost");
        ejbClientContextProps.put("remote.connection." + connectionName + ".port", "8280");
        ejbClientContextProps.put("remote.connection." + connectionName + ".username", "quickuser1");
        ejbClientContextProps.put("remote.connection." + connectionName + ".password", "quick123+");

        try {
            appTwoScopedEjbContextA = (Context) new InitialContext(ejbClientContextProps).lookup("ejb:");
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoA')", e);
        }

        // change the necessary properties to call the other server
        ejbClientContextProps.put("endpoint.name", "appMain->appTwoB_endpoint");
        ejbClientContextProps.put("remote.connection." + connectionName + ".port", "8880");
        ejbClientContextProps.put("remote.connection." + connectionName + ".username", "quickuser2");
        ejbClientContextProps.put("remote.connection." + connectionName + ".password", "quick+123");

        try {
            appTwoScopedEjbContextB = (Context) new InitialContext(ejbClientContextProps).lookup("ejb:");
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoB')", e);
        }
    }

    @PostConstruct
    private void createScopedContext() {
        synchronized (activeInstances) {
            if (activeInstances == 0) {
                LOGGER.info("First instance of " + this.getClass().getSimpleName() + " initializing scoped-context");
                createAppOneContext();
                createAppTwoContext();
            }
            activeInstances++;
        }
    }

    @PreDestroy
    private void destroyScopedContext() {
        synchronized (activeInstances) {
            activeInstances--;
            if (activeInstances == 0) {
                LOGGER.info("Last active instance of " + this.getClass().getSimpleName() + " closing scoped-context");
                try {
                    appOneScopedEjbContext.close();
                } catch (NamingException e) {
                    LOGGER.error("Could not close the scoped-context of AppOne", e);
                }
                appOneScopedEjbContext = null;
                try {
                    appTwoScopedEjbContextA.close();
                } catch (NamingException e) {
                    LOGGER.error("Could not close the scoped-context of AppTwo (A)", e);
                }
                appTwoScopedEjbContextA = null;
                try {
                    appTwoScopedEjbContextB.close();
                } catch (NamingException e) {
                    LOGGER.error("Could not close the scoped-context of AppTwo (B)", e);
                }
                appTwoScopedEjbContextB = null;
            }
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
        final StringBuilder result = new StringBuilder("MainAppSContext[" + caller.getName() + "]@" + getJBossNodeName());
        // Call AppOne with the direct ejb: naming
        try {
            result.append("  >  [ " + invokeAppOne(text));
        } catch (Exception e) {
            LOGGER.error("Could not invoke AppOne", e);
        }

        result.append(" > " + invokeAppTwo(text));

        result.append(" ]");

        return result.toString();
    }

    /**
     * Invoke the AppOne with the scoped client context. The initial connection
     * will use the 'quickuser1', to differentiate the cluster connection it
     * will be use the user 'quickuser2' to see if the clustered context is used
     * or not.
     *
     * @param text
     *            Simple text which will be logged at server side.
     * @return simple collection of the returned results
     */
    private String invokeAppOne(String text) {
        try {
            // this context will not use the server configured
            // 'outbound-connection' and also did not use the
            // jboss-ejb-client.xml.
            final AppOne bean = (AppOne) appOneScopedEjbContext.lookup("ejb-multi-server-app-one/ejb//AppOneBean!" + AppOne.class.getName());

            StringBuilder result = new StringBuilder("{");
            for (int i = 0; i < 8; i++) {
                // invoke on the bean
                final String appOneResult = bean.invoke(text);
                if (i > 0) {
                    result.append(", ");
                }
                result.append(appOneResult);
            }
            result.append("}");

            LOGGER.info("AppOne (loop) returns : " + result);
            return result.toString();

        } catch (NamingException e) {
            LOGGER.error("Could not invoke appOne", e);
            return null;
        }
    }

    /**
     * Close the given context and write a log message which endpoint is closed.
     *
     * @param iCtx
     *            context to close, <code>null</code> will be ignored.
     */
    private static void saveContextClose(Context iCtx) {
        if (iCtx != null) {
            try {
                LOGGER.info("close Context " + iCtx.getEnvironment().get("endpoint.name"));
                iCtx.close();

            } catch (NamingException e) {
                LOGGER.error("InitialContext can not be closed", e);
            }
        }
    }

    /**
     * Invoke the App2 with different ejb-client context. The server AppTwoA
     * will be called with the user quickuser1. The server AppTwoB will be
     * called with the user quickuser2. Both invocations are separate, there
     * will no mix between. Also the outbound-connection is not used.
     *
     * @param text
     *            Simple text which will be logged at server side.
     * @return simple collection of the returned results
     */
    private String invokeAppTwo(String text) {
        AppTwo beanA = null;
        AppTwo beanB = null;

        try {
            beanA = (AppTwo) appTwoScopedEjbContextA.lookup("ejb-multi-server-app-two/ejb//AppTwoBean!" + AppTwo.class.getName());
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoA')");
        }

        try {
            beanB = (AppTwo) appTwoScopedEjbContextB.lookup("ejb-multi-server-app-two/ejb//AppTwoBean!" + AppTwo.class.getName());
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoB')");
        }

        StringBuilder result = new StringBuilder(" appTwo loop(4 time A-B expected){");
        for (int i = 0; i < 4; i++) {
            // invoke on the bean
            String appResult = beanA.invokeSecured(text);
            if (i > 0) {
                result.append(", ");
            }
            result.append(appResult);
            appResult = beanB.invokeSecured(text);
            result.append(", ");
            result.append(appResult);
        }
        result.append("}");

        LOGGER.info("AppTwo (loop) returns : " + result);

        return result.toString();
    }

    /**
     * Same method as {@link #invokeAppTwo(String)}, but the scoped context is created and destroyed
     * inside the method. Because of this it is not possbile to use the transaction managed by the container
     * as the connection must be open until the container send commit/rollback to the foreign server.
     * Therefore it is necessary to add the annotation @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
     * to the {@link #invokeAll(String)} method.
     *
     * For test you might rename this method.
     *
     * @param text
     *            Simple text which will be logged at server side.
     * @return simple collection of the returned results
     */
    @SuppressWarnings("unused")
    private String invokeAppTwoAlternative(String text) {
        AppTwo beanA = null;
        AppTwo beanB = null;

        final Properties ejbClientContextProps = new Properties();
        ejbClientContextProps.put("endpoint.name", "appMain->appTwoA_endpoint");
        // Property to enable scoped EJB client context which will be tied to
        // the JNDI context
        ejbClientContextProps.put("org.jboss.ejb.client.scoped.context", true);
        // Property which will handle the ejb: namespace during JNDI lookup
        ejbClientContextProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

        final String connectionName = "appTwoConnection";
        ejbClientContextProps.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        // add the properties to connect the app-one host
        ejbClientContextProps.put("remote.connections", connectionName);
        ejbClientContextProps.put("remote.connection." + connectionName + ".host", "localhost");
        ejbClientContextProps.put("remote.connection." + connectionName + ".port", "8280");
        ejbClientContextProps.put("remote.connection." + connectionName + ".username", "quickuser1");
        ejbClientContextProps.put("remote.connection." + connectionName + ".password", "quick123+");

        Context iCtxA = null;
        try {
            iCtxA = (Context) new InitialContext(ejbClientContextProps).lookup("ejb:");
            beanA = (AppTwo) iCtxA.lookup("ejb-multi-server-app-two/ejb//AppTwoBean!" + AppTwo.class.getName());
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoA')");
        }

        // change the necessary properties to call the other server
        ejbClientContextProps.put("endpoint.name", "appMain->appTwoB_endpoint");
        ejbClientContextProps.put("remote.connection." + connectionName + ".port", "8880");
        ejbClientContextProps.put("remote.connection." + connectionName + ".username", "quickuser2");
        ejbClientContextProps.put("remote.connection." + connectionName + ".password", "quick+123");
        Context iCtxB = null;
        try {
            iCtxB = (Context) new InitialContext(ejbClientContextProps).lookup("ejb:");
            beanB = (AppTwo) iCtxB.lookup("ejb-multi-server-app-two/ejb//AppTwoBean!" + AppTwo.class.getName());
        } catch (NamingException e) {
            LOGGER.error("Could not create InitialContext('appTwoB')");
        }

        StringBuilder result = new StringBuilder(" appTwo loop(4 time A-B expected){");
        for (int i = 0; i < 4; i++) {
            // invoke on the bean
            String appResult = beanA.invokeSecured(text);
            if (i > 0) {
                result.append(", ");
            }
            result.append(appResult);
            appResult = beanB.invokeSecured(text);
            result.append(", ");
            result.append(appResult);
        }
        result.append("}");

        LOGGER.info("AppTwo (loop) returns : " + result);

        // should be closed and null the reference to close the connection
        saveContextClose(iCtxA);
        iCtxA = null;
        saveContextClose(iCtxB);
        iCtxB = null;

        return result.toString();
    }
}
