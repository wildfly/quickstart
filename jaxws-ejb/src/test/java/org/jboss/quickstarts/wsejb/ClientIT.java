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
package org.jboss.quickstarts.wsejb;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author rsearls@redhat.com
 */
public class ClientIT {
    /**
     * The path of the WSDL endpoint in relation to the deployed web application.
     */
    private static final String WSDL_PATH = "EJB3Bean?wsdl";

    protected static URL getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080";
        }
        try {
            return new URI(host + "/" + WSDL_PATH).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }
    private static URL deploymentUrl;

    private EJB3RemoteInterface client;

    @BeforeClass
    public static void beforeClass() throws MalformedURLException {
        URL deploymentUrl = getHTTPEndpoint();

        System.out.println("WSDL Deployment URL: " + deploymentUrl);

        // Set the deployment url
        ClientIT.deploymentUrl = deploymentUrl;
    }

    @Before
    public void setup() {
        QName serviceName = new QName("http://wsejb.quickstarts.jboss.org/", "EJB3BeanService");
        Service service = Service.create(deploymentUrl, serviceName);
        client = service.getPort(EJB3RemoteInterface.class);
    }

    @Test
    public void testEcho() {
        System.out.println("[Client] Requesting the WebService...");
        // Get a response from the ejb WebService
        final String response = client.echo("hello");
        assertEquals(response, "EJB3Bean returning: hello");
        System.out.println("[WebService] " + response);
    }
}
