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
package org.jboss.quickstarts.ws.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.quickstarts.ws.jaxws.samples.jsr181pojo.JSEBean;

/**
 * @author rsearls@redhat.com
 */
public class Client {

    public static void main(String[] args) {
        String endPointAddress = "http://localhost:8080/jaxws-pojo-endpoint/JSEBean";
        QName serviceName = new QName("http://jsr181pojo.samples.jaxws.ws.quickstarts.jboss.org/", "JSEBeanService");

        try {
            URL wsdlURL = new URL(endPointAddress + "?wsdl");
            Service service = Service.create(wsdlURL, serviceName);
            JSEBean proxy = service.getPort(JSEBean.class);
            System.out.println(proxy.echo("pojoClient calling"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
