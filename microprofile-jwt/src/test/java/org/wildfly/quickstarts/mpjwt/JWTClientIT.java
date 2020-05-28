/*
 * JBoss, Home of Professional Open Source
 * Copyright 2020 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.quickstarts.mpjwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test case to test the JWT secured deployment created within the quickstart and to test the invocations illustrated by the
 * quickstart.
 *
 * This test case is testing the functionality described in the quickstart not the overall JWT implementation so it does not go
 * into more detail testing items such as expired tokens.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class JWTClientIT {

    private static final String ROOT_PATH = "/Sample/";
    private static final String HELLO_WORLD = "helloworld";
    private static final String SUBSCRIPTION = "subscription";
    private static final String BIRTHDAY = "birthday";

    private static final String DATE = "2017-09-15";
    private static final String ECHOER_GROUP = "Echoer";
    private static final String OTHER_GROUP = "Other";
    private static final String SUBSCRIBER_GROUP = "Subscriber";

    private static final String ANONYMOUS = "anonymous";
    private static final String PRINCIPAL_NAME = "testUser";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private static final String APP_NAME = "microprofile-jwt";

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive =  ShrinkWrap.create(WebArchive.class, APP_NAME + ".war")
                .addClass(App.class)
                .addClass(SampleEndPoint.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
                .addAsManifestResource("META-INF/microprofile-config.properties", "microprofile-config.properties")
                .addAsManifestResource("META-INF/public.pem", "public.pem");

        return webArchive;
    }

    private String getDeploymentUrl() {
        String deploymentUrl = this.deploymentUrl.toString();

        return deploymentUrl.endsWith("/") ? deploymentUrl.substring(0, deploymentUrl.length() - 1) : deploymentUrl;
    }

    @Test
    public void testHelloWorld() throws Exception {
        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + HELLO_WORLD);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(httpResponse.getEntity());
        assertTrue("Call was anonymous", body.contains(ANONYMOUS));

        httpResponse.close();
    }

    @Test
    public void testAuthenticated() throws Exception {
        String jwtToken = TokenUtil.generateJWT(PRINCIPAL_NAME, DATE , ECHOER_GROUP, SUBSCRIBER_GROUP);

        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + HELLO_WORLD);
        httpGet.addHeader(AUTHORIZATION, BEARER + " " + jwtToken);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(httpResponse.getEntity());
        assertTrue("Call was authenticated", body.contains(PRINCIPAL_NAME));

        httpResponse.close();
    }

    @Test
    public void testAuthorizationRequired() throws Exception {
        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + SUBSCRIPTION);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Authorization required", 403, httpResponse.getStatusLine().getStatusCode());

        httpResponse.close();
    }

    @Test
    public void testAuthorized() throws Exception {
        String jwtToken = TokenUtil.generateJWT(PRINCIPAL_NAME, DATE, ECHOER_GROUP, SUBSCRIBER_GROUP);

        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + SUBSCRIPTION);
        httpGet.addHeader(AUTHORIZATION, BEARER + " " + jwtToken);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(httpResponse.getEntity());
        assertTrue("Call was authenticated", body.contains(PRINCIPAL_NAME));

        httpResponse.close();
    }

    @Test
    public void testMissingRole() throws Exception {
        String jwtToken = TokenUtil.generateJWT(PRINCIPAL_NAME, DATE, ECHOER_GROUP, OTHER_GROUP);

        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + SUBSCRIPTION);
        httpGet.addHeader(AUTHORIZATION, BEARER + " " + jwtToken);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Authorization Required", 403, httpResponse.getStatusLine().getStatusCode());

        httpResponse.close();
    }

    @Test
    public void testClaimAccess() throws Exception {
        String jwtToken = TokenUtil.generateJWT(PRINCIPAL_NAME, DATE, ECHOER_GROUP, SUBSCRIBER_GROUP);

        HttpGet httpGet = new HttpGet(getDeploymentUrl() + ROOT_PATH + BIRTHDAY);
        httpGet.addHeader(AUTHORIZATION, BEARER + " " + jwtToken);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(httpResponse.getEntity());
        assertTrue("Call was authenticated", body.contains("Happy Birthday") ||
                body.contains("days until your next birthday"));

        httpResponse.close();
    }

}
