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
package org.jboss.as.quickstarts.jaxrsclient.test;

import static jakarta.ws.rs.HttpMethod.POST;
import java.io.IOException;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

import org.jboss.as.quickstarts.jaxrsclient.model.Contact;

//This filter adds a username to SavedBy field
public class SavedByClientRequestFilter implements ClientRequestFilter {

    public static final String USERNAME = "quickstartUser";

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        if (POST.equals(method) && requestContext.hasEntity()) {
            Contact contact = (Contact) requestContext.getEntity();
            contact.setSavedBy(USERNAME);
            requestContext.setEntity(contact);
        }

    }

}
