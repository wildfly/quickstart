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
package hello.client;

import hello.server.ejb.Hello;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * A SOAP based client to the HelloBean EJB.<br>
 * <br>
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
public class HelloClient {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(HelloClient.class.getName());

    // ------------------------------------------------------------------------
    // Java application main method
    // ------------------------------------------------------------------------

    public static void main(String[] args) throws NamingException, MalformedURLException {

        String wsdlUrl = "http://localhost:8080/wildfly-helloworld-classfiletransformers-8.1.0-SNAPSHOT/HelloBean?wsdl";

        Hello webServiceClient = createClient(wsdlUrl, Hello.class);

        String result = webServiceClient.sayHello("client");

        logger.info(String.format("Message from server: %s", result));

    }

    // ------------------------------------------------------------------------
    // Private helper methods
    // ------------------------------------------------------------------------

    /**
     * Generates a dynamic proxy that can talk SOAP with wildfly's JAX-WS stack.
     */
    private static <T> T createClient(String wsdl, Class<T> intf) throws MalformedURLException {
        URL wsdlUrl = new URL(wsdl);

        String namespace = inferXmlNamespace(intf);

        QName service = new QName(namespace, String.format("%sBeanService", intf.getSimpleName()));

        return (T) new Service(wsdlUrl, service) {
        }.getPort(new QName(namespace, String.format("%sBeanPort", intf.getSimpleName())), intf);
    }

    private static String inferXmlNamespace(Class<?> intf) {
        StringBuilder result = new StringBuilder("http://");

        String[] packages = intf.getPackage().getName().split("\\.");
        for (int i = packages.length - 1; i >= 0; i--) {
            result.append(packages[i]);
            if (i != 0) {
                result.append(".");
            }
        }
        result.append("/");
        return result.toString();
    }

}
