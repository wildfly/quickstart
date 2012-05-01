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
package org.jboss.as.quickstarts.erraihelloworld.server;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

import org.jboss.as.quickstarts.erraihelloworld.client.shared.HelloWorldResource;


/**
 * A simple REST service which is able to say hello to someone using
 * HelloService. Please take a look at the web.xml where JAX-RS is enabled and
 * notice the @PathParam which expects the URL to contain {@code /json/David} or
 * {@code /xml/Mary}.
 *
 * @author bsutter@redhat.com
 */
public class HelloWorldResourceImpl implements HelloWorldResource {

  @Inject
  HelloService helloService;

  @Override
  public String getHelloWorldJSON(@PathParam("name") String name) {
    System.out.println("name: " + name);
    return helloService.createHelloMessage(name);
  }

  @Override
  public String getHelloWorldXML(@PathParam("name") String name) {
    System.out.println("name: " + name);
    return "<xml><result>" + helloService.createHelloMessage(name)
        + "</result></xml>";
  }

}
