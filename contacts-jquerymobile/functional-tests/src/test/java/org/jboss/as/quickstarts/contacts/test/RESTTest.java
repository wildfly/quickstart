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
package org.jboss.as.quickstarts.contacts.test;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for REST API of the application
 *
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class RESTTest {

    private static final String NEW_CONTACT_FIRSTNAME = "John";
    private static final String NEW_CONTACT_LASTNAME = "Doe";
    private static final String NEW_CONTACT_EMAIL = "john.doe@redhat.com";
    private static final String NEW_CONTACT_BIRTHDATE = "1970-01-01";
    private static final String NEW_CONTACT_PHONE = "7894561239";

    private static final String DEFAULT_CONTACT_FIRSTNAME = "John";
    private static final String DEFAULT_CONTACT_LASTNAME = "Smith";
    private static final int DEFAULT_CONTACT_ID = 10001;

    private static final String API_PATH = "rest/contacts/";

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    /**
     * Creates deployment which is sent to the container upon test's start.
     *
     * @return war file which is deployed while testing, the whole application in our case
     */
    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.contacts();
    }

    @Test
    @InSequence(1)
    public void testGetContact() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH + DEFAULT_CONTACT_ID));

        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject contact = new JSONObject(responseBody);

        assertEquals(DEFAULT_CONTACT_ID, contact.getInt("id"));
        assertEquals(DEFAULT_CONTACT_FIRSTNAME, contact.getString("firstName"));
        assertEquals(DEFAULT_CONTACT_LASTNAME, contact.getString("lastName"));
    }

    @Test
    @InSequence(2)
    public void testAddContact() throws Exception {
        HttpPost post = new HttpPost(contextPath.toString() + API_PATH);
        post.setHeader("Content-Type", "application/json");
        String newContactJSON = new JSONStringer().object()
            .key("firstName").value(NEW_CONTACT_FIRSTNAME)
            .key("lastName").value(NEW_CONTACT_LASTNAME)
            .key("email").value(NEW_CONTACT_EMAIL)
            .key("phoneNumber").value(NEW_CONTACT_PHONE)
            .key("birthDate").value(NEW_CONTACT_BIRTHDATE)
            .endObject().toString();
        post.setEntity(new StringEntity(newContactJSON));

        HttpResponse response = httpClient.execute(post);

        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    @InSequence(3)
    public void testGetAllContacts() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH));
        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray contacts = new JSONArray(responseBody);

        assertEquals(3, contacts.length());

        assertEquals(1, contacts.getJSONObject(0).getInt("id"));
        assertEquals(NEW_CONTACT_FIRSTNAME, contacts.getJSONObject(0).getString("firstName"));
        assertEquals(NEW_CONTACT_LASTNAME, contacts.getJSONObject(0).getString("lastName"));
        assertEquals(NEW_CONTACT_EMAIL, contacts.getJSONObject(0).getString("email"));
        assertEquals(NEW_CONTACT_PHONE, contacts.getJSONObject(0).getString("phoneNumber"));
    }
}
