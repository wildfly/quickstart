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
package org.jboss.as.quickstarts.gwthelloworld.client.local;

import org.jboss.as.quickstarts.gwthelloworld.server.HelloWorldResource;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents the JSON response object sent back from the
 * {@link HelloWorldResource JAX-RS HelloWorldResource service}.
 * <p>
 * This is a <a href=
 * "http://code.google.com/webtoolkit/doc/latest/DevGuideCodingBasicsOverlay.html"
 * >JavaScript overlay type</a>. It allows the client code to interact with the
 * JSON objects that were created by the browser's JSON parser directly from the
 * REST response. Another approach would have been to create a proper JavaBean
 * class for the response object, but then the JSON fields would have to be
 * copied into the JavaBeans properties field-by-field. A third approach would
 * be to work with GWT's client-side JavaScript API directly, but then there is
 * no code completion on anything, no type safety, and nowhere to attach JavaDoc
 * comments.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class HelloResponse extends JavaScriptObject {

  protected HelloResponse() {}

  /**
   * Returns the result message sent by the JAX-RS resource.
   * <p>
   * Implementation note: this is an example of a GWT JSNI method. The between
   * the special comment sequence <code>/*-{</code> and <code>}-*&#47;</code> is
   * JavaScript code. This code is checked by the GWT compiler and ends up in
   * the generated JavaScript.
   * <p>
   * In the special case of a <a href=
   * "http://code.google.com/webtoolkit/doc/latest/DevGuideCodingBasicsOverlay.html"
   * >JavaScript overlay type</a>, {@code this} refers to the JavaScript object
   * on which this object is overlaid. Specifically in this case, it's the
   * parsed JSON response from
   * {@link HelloWorldResource#getHelloWorldJSON(String)}.
   *
   * @return the result message sent by the JAX-RS resource.
   */
  public final native String getResult() /*-{ return this.result; }-*/;

}
