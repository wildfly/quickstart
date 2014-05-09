/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.appclient.acc.client;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.appclient.acc.client.interceptor.ClientInterceptor;
import org.jboss.as.quickstarts.appclient.server.ejb.StatelessSession;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.logging.Logger;

/**
 * A simple EJB client to demonstrate the use of the JBoss application container
 * to show that the injection will work for server EJB-beans.
 *  
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    /**
     * <p>According to the JavaEE Platform specification (EE7 JSR342 - chapter EE5.16)
     * the container must provide the following boolean property as <code>TRUE</code>
     * if this client runs in an ApplicationClientContainer.<br/>
     * If running in a Web or EJB container the property is <code>FALSE</code>.</p>
     * <p>Can be used to ensure that the Injection works</p>
     */
    @Resource(lookup = "java:comp/InAppClientContainer")
    private static boolean isInAppclient;
    @EJB
    private static StatelessSession slsb;
    

    /** no instance necessary */
    private Main() {
    }

    /**
     * @param args the command line arguments
     * @throws NamingException 
     */
    public static void main(String args[]) throws NamingException {
        // Show that the client is started with arguments at command line
        LOG.info("Main started " + ( args.length != 0 ? "with" : "without") + " arguments");
        if(args.length > 0) LOG.info("            " + Arrays.asList(args));
        
        // ensure that the program is running in an AppClientContainer
        // the value might be NULL if we are running as a simple Java program
        if(!Boolean.TRUE.equals(isInAppclient)) {
            throw new RuntimeException("Not running in an Application Client Container");
        }

        // add an client side interceptor to provide the client machine name to the server application
        EJBClientContext.getCurrent().registerInterceptor(0, new ClientInterceptor());
        LOG.info(slsb.getGreeting());
        slsb.invokeWithClientContext();
    }
}
