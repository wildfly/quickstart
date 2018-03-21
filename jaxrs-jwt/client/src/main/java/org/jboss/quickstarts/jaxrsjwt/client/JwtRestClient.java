/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.quickstarts.jaxrsjwt.client;

import org.jboss.quickstarts.jaxrsjwt.model.Jwt;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class JwtRestClient {
    private static final String AUTHZ_HEADER = "Authorization";
    private static final String REST_TARGET_URL = "http://localhost:8080/jaxrs-jwt-service/rest";
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PW = "adminpw";
    private static final String CUSTOMER_NAME = "customer";
    private static final String CUSTOMER_PW = "customerpw";

    private String username;
    private String password;

    public JwtRestClient() {
        username = null;
        password = null;
    }

    public JwtRestClient(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static void main(String[] args) {
        JwtRestClient adminClient = new JwtRestClient(ADMIN_NAME, ADMIN_PW);
        adminClient.test(true);
        JwtRestClient customerClient = new JwtRestClient(CUSTOMER_NAME, CUSTOMER_PW);
        customerClient.test(true);
        //test without token
        JwtRestClient noToken = new JwtRestClient();
        noToken.test(false);

    }

    public void test(boolean obtainToken) {
        System.out.println("------------------------------");
        System.out.println("Testing "+(username != null ? username : "without token")+" ");
        System.out.println("------------------------------");
        Jwt jwt;
        String authzHeaderValue = null;
        if (obtainToken) {
            System.out.println("Obtaining JWT...");
            Form form = new Form();
            form.param("username", username);
            form.param("password", password);
            jwt = ClientBuilder.newClient().target(REST_TARGET_URL).path("/token").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Jwt.class);
            if (jwt == null) {
                System.out.println("Failed to obtain JWT.");
                return;
            }
            authzHeaderValue = "Bearer " + jwt.getToken();
        }

        getUsingJwt("/protected", authzHeaderValue);
        getUsingJwt("/public", authzHeaderValue);
        getUsingJwt("/customer", authzHeaderValue);
        getUsingJwt("/claims", authzHeaderValue);

    }

    private void getUsingJwt(String path, String authzHeaderValue) {
        System.out.println("Accessing " + path + "...");
        Response response = null;
        if (authzHeaderValue != null)
            response = ClientBuilder.newClient().target(REST_TARGET_URL).path(path).request().header(AUTHZ_HEADER, authzHeaderValue).get();
        else
            response = ClientBuilder.newClient().target(REST_TARGET_URL).path(path).request().get();
        System.out.println("Status: " + response.getStatus()+"\n" + (response.getStatus() == 200 ? response.readEntity(String.class)+"\n" : ""));
        response.close();
    }
}