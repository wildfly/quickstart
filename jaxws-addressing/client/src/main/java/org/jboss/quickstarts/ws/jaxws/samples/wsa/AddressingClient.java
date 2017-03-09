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
package org.jboss.quickstarts.ws.jaxws.samples.wsa;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.AddressingFeature;

/**
 * @author rsearls@redhat.com
 */
public final class AddressingClient {
    private static final String serviceURL =
            "http://localhost:8080/jaxws-addressing/AddressingService";

    public static void main(String[] args) throws Exception {
        // construct proxy
        QName serviceName =
                new QName("http://www.jboss.org/jbossws/ws-extensions/wsaddressing",
                        "AddressingService");
        URL wsdlURL = new URL(serviceURL + "?wsdl");
        Service service = Service.create(wsdlURL, serviceName);
        ServiceIface proxy = service.getPort(ServiceIface.class, new AddressingFeature());
        // invoke method
        System.out.println(proxy.sayHello());
    }
}
