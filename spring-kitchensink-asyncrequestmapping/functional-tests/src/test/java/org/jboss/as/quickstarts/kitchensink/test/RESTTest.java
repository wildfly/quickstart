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
package org.jboss.as.quickstarts.kitchensink.test;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONObject;
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

    private static final String DEFAULT_MEMBER_NAME = "John Smith";
    private static final String DEFAULT_MEMBER_EMAIL = "john.smith@mailinator.com";
    private static final String DEFAULT_MEMBER_PHONE = "2125551212";

    private static final String API_PATH = "rest/members";

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
        return Deployments.kitchensink();
    }

    @Test
    public void testGetMember() throws Exception {
        HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + API_PATH + "/0"));

        assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject member = new JSONObject(responseBody);

        assertEquals(0, member.getInt("id"));
        assertEquals(DEFAULT_MEMBER_NAME, member.getString("name"));
        assertEquals(DEFAULT_MEMBER_EMAIL, member.getString("email"));
        assertEquals(DEFAULT_MEMBER_PHONE, member.getString("phoneNumber"));
    }

}
