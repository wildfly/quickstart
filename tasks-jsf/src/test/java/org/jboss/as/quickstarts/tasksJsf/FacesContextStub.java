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
package org.jboss.as.quickstarts.tasksJsf;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;

/**
 * Stub of {@link FacesContext} to allow testing without real JSF session.
 *
 * @author Lukas Fryc
 *
 */
public class FacesContextStub extends FacesContext {

    FacesContextStub(String test) {
    }

    public static void setCurrentInstance(FacesContext facesContext) {
        FacesContext.setCurrentInstance(facesContext);
    }

    @Override
    public Application getApplication() {
        return null;
    }

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        return null;
    }

    @Override
    public ExternalContext getExternalContext() {
        return null;
    }

    @Override
    public Severity getMaximumSeverity() {
        return null;
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        return null;
    }

    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        return null;
    }

    @Override
    public RenderKit getRenderKit() {
        return null;
    }

    @Override
    public boolean getRenderResponse() {
        return false;
    }

    @Override
    public boolean getResponseComplete() {
        return false;
    }

    @Override
    public ResponseStream getResponseStream() {

        return null;
    }

    @Override
    public void setResponseStream(ResponseStream responseStream) {

    }

    @Override
    public ResponseWriter getResponseWriter() {

        return null;
    }

    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {

    }

    @Override
    public UIViewRoot getViewRoot() {

        return null;
    }

    @Override
    public void setViewRoot(UIViewRoot root) {

    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {

    }

    @Override
    public void release() {

    }

    @Override
    public void renderResponse() {

    }

    @Override
    public void responseComplete() {

    }

}
