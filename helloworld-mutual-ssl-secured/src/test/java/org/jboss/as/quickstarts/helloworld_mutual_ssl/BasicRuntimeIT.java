/*
 * Copyright 2023 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.helloworld_mutual_ssl;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;


import static org.jboss.as.quickstarts.helloworld_mutual_ssl.KeystoreUtil.createTrustStore;
import static org.junit.Assert.assertEquals;

/**
 * The very basic runtime integration testing.
 * @author Prarthona Paul
 * @author emartins
 */
public class BasicRuntimeIT {

    private static final String DEFAULT_SERVER_HOST = "https://localhost:8443/helloworld-mutual-ssl-secured";
    private static final String DEFAULT_SERVER_DIR = System.getProperty("user.dir") + "/target/server";

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, URISyntaxException, KeyStoreException {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        String serverDir  = System.getProperty("server.dir");
        if (serverDir == null) {
            serverDir = DEFAULT_SERVER_DIR;
        }
        String serverConfigDir = serverDir + "/standalone/configuration";
        HttpGet request = new HttpGet(new URI(serverHost+"/"));
        KeyStore trustStore = createTrustStore(serverConfigDir, "application.keystore", "password", "server", "PKCS12");
        final HttpClient client = getHttpClientWithSSL(new File(serverConfigDir + "/client.keystore.P12"), "secret", "PKCS12", new File(serverConfigDir + "/client.truststore"), "password", "PKCS12");
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
//                    .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setConnectionManager(new PoolingHttpClientConnectionManager(registry))
                    .setSchemePortResolver(new DefaultSchemePortResolver())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Creating HttpClient with customized SSL failed. We are returning the default one instead.", e);
        }
    }
}