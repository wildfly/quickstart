/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.cluster.hasingleton.service.client;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.ServiceAccess;

/**
 * A client to call the SingletonService via EJB remoting (AS7) to demonstrate the behaviour of the singleton.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class SingletonServiceClient {
    /**
     * Proxy of the SLSB
     */
    private final ServiceAccess accessBean;

    /**
     * Constructor to prepare the client-context.<br/>
     * There must be a jboss-ejb-client.properties file in the classpath to specify the server connection(s).
     *
     * @throws NamingException
     */
    private SingletonServiceClient() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        String lookupName = "ejb:/jboss-as-cluster-ha-singleton-service/ServiceAccessBean!" + ServiceAccess.class.getName();
        System.out.println("Lookup Bean >" + lookupName);
        accessBean = (ServiceAccess) context.lookup(lookupName);
    }

    private String getServiceNodeName() {
        return accessBean.getNodeNameOfTimerService();
    }

    /**
     * Call the EJB 4 times to demonstrate that despite load balancing the SingletonService is called at the same server.
     *
     * @param args no arguments needed
     * @throws NamingException
     */
    public static void main(String[] args) throws NamingException {
        SingletonServiceClient client = new SingletonServiceClient();

        for (int i = 0; i < 4; i++) {
            System.out.println("#" + i + " The service is active on node with name = " + client.getServiceNodeName());
        }

    }

}
