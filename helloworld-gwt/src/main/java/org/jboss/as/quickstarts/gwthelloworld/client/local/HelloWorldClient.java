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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

/**
 * Companion class to the UI declared in {@linkplain HelloWorldClient.ui.xml}.
 * Handles events and updates the DOM in response.
 * 
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class HelloWorldClient {

  interface MyUiBinder extends UiBinder<Panel, HelloWorldClient> {}
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField Panel root;
  @UiField Label result;
  @UiField InputElement name;
  @UiField Button sayHelloButton;
  
  HelloWorldClient() {
    uiBinder.createAndBindUi(this);
    DOM.setElementAttribute(sayHelloButton.getElement(), "name", "sayHelloButton");
  }

  /**
   * Handles a click of the button by sending an AJAX request to the
   * HelloWorldResource and then updating the {@code result} label in response.
   * 
   * @param e Details of the click event. Ignored by this handler.
   */
  @UiHandler("sayHelloButton")
  public void onButtonClick(ClickEvent e) {
    try {
      new RequestBuilder(RequestBuilder.GET, "hello/json/" + URL.encodePathSegment(name.getValue()))
        .sendRequest(null, new RequestCallback() {

        @Override
        public void onResponseReceived(Request request, Response response) {
          if (response.getStatusCode() == Response.SC_OK) {
            HelloResponse r = (HelloResponse) JsonUtils.safeEval(response.getText());
            result.setText(r.getResult());
          } else {
            handleError("Server responded with status code " + response.getStatusCode());
          }
        }

        @Override
        public void onError(Request request, Throwable exception) {
          handleError(exception.getMessage());
        }
      });
    } catch (RequestException exception) {
      handleError(exception.getMessage());
    }
  }
  
  private void handleError(String details) {
    result.setText("Sorry, can't say hello now. " + details);
  }

  /**
   * Returns the DOM fragment that this 
   * @return
   */
  public Panel getElement() {
    return root;
  }

}
