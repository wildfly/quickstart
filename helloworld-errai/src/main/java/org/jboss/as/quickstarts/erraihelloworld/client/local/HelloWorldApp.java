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
package org.jboss.as.quickstarts.erraihelloworld.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.as.quickstarts.erraihelloworld.client.shared.HelloWorldResource;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the entry point to the client portion of the web application. At
 * compile time, Errai finds the {@code @EntryPoint} annotation on this class
 * and generates bootstrap code that creates an instance of this class when the
 * page loads. This client-side bootstrap code will also call the
 * {@link #init()} method because it is annotated with the
 * {@code @PostConstruct} CDI annotation.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@EntryPoint
public class HelloWorldApp {

  /**
   * Errai's JAX-RS module generates a stub class that makes AJAX calls back to
   * the server for each resource method on the {@link HelloWorldResource}
   * interface. The paths and HTTP methods for the AJAX calls are determined
   * automatically based on the JAX-RS annotations ({@code @Path}, {@code @GET},
   * {@code @POST}, and so on) on the resource.
   * <p>
   * You can create additional JAX-RS proxies by following the same pattern
   * ({@code @Inject Caller<MyResourceType>}) with your own JAX-RS resource
   * classes.
   */
  @Inject
  private Caller<HelloWorldResource> helloWorldCaller;

  /**
   * This method creates an instance of the UiBinder UI {@link HelloWorldClient}
   * and attaches it to the RootPanel, the top-level DOM node that is an
   * ancestor to all GWT widgets on the page (the {@code <body>} element of the
   * HTML document).
   */
  @PostConstruct
  public void init() {
    RootPanel.get().add(new HelloWorldClient(helloWorldCaller).getElement());
  }

}
