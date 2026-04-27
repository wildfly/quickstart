/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2026 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jboss.as.quickstarts.helloworld_dynamic_client_ssl_context;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import javax.net.ssl.SSLContext;

import static org.jboss.as.quickstarts.helloworld_dynamic_client_ssl_context.KeyStoreUtils.loadKeyPairFromKeyStore;
import static org.junit.Assert.assertEquals;

/**
 * The very basic runtime integration testing.
 * @author Richard Zan
 */
public class BasicRuntimeIT {

    private static final String SERVER_HOST_FIRST_PORT = "https://localhost:9443";
    private static final String SERVER_HOST_SECOND_PORT = "https://localhost:10443";
    private static final String ARTIFACT_ID = "/helloworld-dynamic-client-ssl-context/";
    private static final String DEFAULT_SERVER_DIR = System.getProperty("user.dir") + "/target/server";

    @Test
    public void testFirstHTTPSEndpointIsAvailable() throws IOException, URISyntaxException, KeyStoreException {
        String serverDir  = System.getProperty("server.dir");
        if (serverDir == null) {
            serverDir = DEFAULT_SERVER_DIR;
        }
        String serverConfigDir = serverDir + "/standalone/configuration";

        HttpGet request = new HttpGet(new URI(SERVER_HOST_FIRST_PORT+ARTIFACT_ID));
        KeyStore trustStore = loadKeyPairFromKeyStore(
                serverConfigDir,
                "server1.keystore.P12",
                "secret",
                "localhost1",
                "PKCS12");
        final HttpClient client = getHttpClientWithSSL(
                new File(serverConfigDir + "/client1.keystore.P12"),
                "secret",
                "PKCS12",
                new File(serverConfigDir + "/client1.truststore.P12"),
                "secret",
                "PKCS12");
        HttpResponse response = client.execute(request);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testSecondHTTPSEndpointIsAvailable() throws IOException, URISyntaxException, KeyStoreException {
        String serverDir  = System.getProperty("server.dir");
        if (serverDir == null) {
            serverDir = DEFAULT_SERVER_DIR;
        }
        String serverConfigDir = serverDir + "/standalone/configuration";

        HttpGet request = new HttpGet(new URI(SERVER_HOST_SECOND_PORT+ARTIFACT_ID));
        KeyStore trustStore = loadKeyPairFromKeyStore(
                serverConfigDir,
                "server2.keystore.P12",
                "secret",
                "localhost2",
                "PKCS12");
        final HttpClient client = getHttpClientWithSSL(
                new File(serverConfigDir + "/client2.keystore.P12"),
                "secret",
                "PKCS12",
                new File(serverConfigDir + "/client2.truststore.P12"),
                "secret",
                "PKCS12");
        HttpResponse response = client.execute(request);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    public HttpClient getHttpClientWithSSL(File keyStoreFile, String keyStorePassword, String keyStoreProvider,
                                           File trustStoreFile, String trustStorePassword, String trustStoreProvider) {

        try {
            KeyStore trustStore = KeyStore.getInstance(trustStoreProvider);
            try (FileInputStream fis = new FileInputStream(trustStoreFile)) {
                trustStore.load(fis, trustStorePassword.toCharArray());
            }
            SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                    .setProtocol("TLS")
                    .loadTrustMaterial(trustStore, null);
            if (keyStoreFile != null) {
                KeyStore keyStore = KeyStore.getInstance(keyStoreProvider);
                try (FileInputStream fis = new FileInputStream(keyStoreFile)) {
                    keyStore.load(fis, keyStorePassword.toCharArray());
                }
                sslContextBuilder.loadKeyMaterial(keyStore, keyStorePassword.toCharArray(), null);
            }
            SSLContext sslContext = sslContextBuilder.build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", socketFactory)
                    .build();

            return HttpClientBuilder.create()
                    .setSSLSocketFactory(socketFactory)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                    .setSchemePortResolver(new DefaultSchemePortResolver())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Creating HttpClient with customized SSL failed.", e);
        }
    }
}