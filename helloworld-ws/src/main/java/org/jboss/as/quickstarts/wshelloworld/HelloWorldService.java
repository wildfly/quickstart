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

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * A simple example of how to setup a JAX-WS Web Service. It can say hello to everyone or to someone in particular.
 *
 * @author lnewson@redhat.com
 */

@WebService(targetNamespace = "http://www.jboss.org/eap/quickstarts/wshelloworld/HelloWorld")
public interface HelloWorldService {

    /**
     * Say hello as a response
     *
     * @return A simple hello world message
     */
    @WebMethod
    String sayHello();

    /**
     * Say hello to someone precisely
     *
     * @param name The name of the person to say hello to
     * @return the number of current bookings
     */
    @WebMethod
    String sayHelloToName(String name);

    /**
     * Say hello to a list of people
     *
     * @param names The list of names to say hello to
     * @return the number of current bookings
     */
    @WebMethod
    String sayHelloToNames(List<String> names);
}
