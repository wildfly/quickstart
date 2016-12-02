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
package org.jboss.as.quickstarts.wshelloworld;

import org.jboss.as.quickstarts.wshelloworld.HelloWorldService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A Client stub to the HelloWorld JAX-WS Web Service.
 *
 * @author lnewson@redhat.com
 */
public class Client implements HelloWorldService {
    private HelloWorldService helloWorldService;

    /**
     * Default constructor
     *
     * @param url The URL to the Hello World WSDL endpoint.
     */
    public Client(final URL wsdlUrl) {
        QName serviceName = new QName("http://www.wildfly.org/quickstarts/wshelloworld/HelloWorld", "HelloWorldService");

        Service service = Service.create(wsdlUrl, serviceName);
        helloWorldService = service.getPort(HelloWorldService.class);
        assert (helloWorldService != null);
    }

    /**
     * Default constructor
     *
     * @param url The URL to the Hello World WSDL endpoint.
     * @throws MalformedURLException if the WSDL url is malformed.
     */
    public Client(final String url) throws MalformedURLException {
        this(new URL(url));
    }

    /**
     * Gets the Web Service to say hello
     */
    @Override
    public String sayHello() {
        return helloWorldService.sayHello();
    }

    /**
     * Gets the Web Service to say hello to someone
     */
    @Override
    public String sayHelloToName(final String name) {
        return helloWorldService.sayHelloToName(name);
    }

    /**
     * Gets the Web Service to say hello to a group of people
     */
    @Override
    public String sayHelloToNames(final List<String> names) {
        return helloWorldService.sayHelloToNames(names);
    }
}
