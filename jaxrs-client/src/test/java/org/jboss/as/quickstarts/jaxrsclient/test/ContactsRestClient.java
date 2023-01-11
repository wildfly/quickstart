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

public class ContactsRestClient extends AbstractContactsRestClient {

    private static final String REST_TARGET_URL = "http://localhost:8080/jaxrs-client/rest/contacts";

    public static void main(String[] args) throws Exception {
        ContactsRestClient client = new ContactsRestClient();
        client.cruedTest();
        client.asyncCrudTest();
        client.delayedInvocationTest();
        client.invocationCallBackTest();
        client.requestResponseFiltersTest();

    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    ;

    @Override
    String getRequestUrl() {
        String host = getServerHost();
        if (host == null) {
            host = REST_TARGET_URL;
        }
        return "http://" + host + "/jaxrs-client/rest/contacts";
    }
}
