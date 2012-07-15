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
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

import com.arjuna.mw.wst11.client.JaxWSHeaderContextProcessor;
import org.jboss.as.quickstarts.wsba.participantcompletion.simple.jaxws.SetServiceBA;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A Client stub to the SetService.
 * 
 * @author paul.robinson@redhat.com, 2012-01-04
 */
@ClientStub
public class Client implements SetServiceBA {
    private SetServiceBA set;

    /**
     * Default constructor with hard-coded values for the SetService endpoint details (wsdl url, service name & port name)
     * 
     * @throws MalformedURLException if the WSDL url is malformed.
     */
    public Client() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/test/SetServiceBA?wsdl");
        QName serviceName = new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/helloworld/wsba/participantcompletion/set",
                "SetServiceBAService");
        QName portName = new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/helloworld/wsba/participantcompletion/set",
                "SetServiceBA");

        Service service = Service.create(wsdlLocation, serviceName);
        set = service.getPort(portName, SetServiceBA.class);

        /*
         * Add client handler chain so that XTS can add the transaction context to the SOAP messages.
         */
        BindingProvider bindingProvider = (BindingProvider) set;
        List<Handler> handlers = new ArrayList<Handler>(1);
        handlers.add(new JaxWSHeaderContextProcessor());
        bindingProvider.getBinding().setHandlerChain(handlers);
    }

    /**
     * Add a value to the set
     * 
     * @param value Value to add to the set.
     * @throws AlreadyInSetException if the item is already in the set.
     * @throws SetServiceException if an error occurred during the adding of the item to the set.
     */
    public void addValueToSet(String value) throws AlreadyInSetException, SetServiceException {
        set.addValueToSet(value);
    }

    /**
     * Query the set to see if it contains a particular value.
     * 
     * @param value the value to check for.
     * @return true if the value was present, false otherwise.
     */
    public boolean isInSet(String value) {
        return set.isInSet(value);
    }

    /**
     * Empty the set
     */
    public void clear() {
        set.clear();
    }
}
