/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.dynamic_client_ssl_context;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

/**
 * This class uses RESTEasy client that is configured with default SSL Context.
 * Server in this example is configured with dynamic ssl context, so it will be dynamically switched according to the peer's hostname and port.
 */
@Path("/")
public class Endpoint {

    static final String DEFAULT_SERVER_HOST_FIRST_PORT = "https://localhost:9443";
    static final String DEFAULT_SERVER_HOST_SECOND_PORT = "https://localhost:10443";
    private static final String ARTIFACT_ID = "/dynamic-client-ssl-context/";

    private ResteasyClientBuilder builder = (ResteasyClientBuilder) ClientBuilder.newBuilder();
    private ResteasyClient client = builder.hostnameVerifier((s, sslSession) -> true).sslContext(SSLContext.getDefault()).build(); // resteasy client must set default ssl context otherwise it uses null

    public Endpoint() throws NoSuchAlgorithmException {
    }

    @GET
    @Path("/port9443request")
    @Produces(MediaType.TEXT_HTML)
    public String pingFirstServer() {
        Response response = client.target(getServerHost(DEFAULT_SERVER_HOST_FIRST_PORT, 9443)).request().get();
        return "HTTP status of result is " +  response.getStatus();
    }

    @GET
    @Path("/port10443request")
    @Produces(MediaType.TEXT_HTML)
    public String pingSecondServer() throws IOException {
        Response response = client.target(getServerHost(DEFAULT_SERVER_HOST_SECOND_PORT, 10443)).request().get();
        return "HTTP status of result is " +  response.getStatus();
    }

    static String getServerHost(String defaultServerHost, int port) {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = defaultServerHost + ARTIFACT_ID;
        } else {
            serverHost = validateServerHost(serverHost, port);
        }
        return serverHost;
    }

    private static String validateServerHost(String host, int port) {
        URI uri = URI.create(host);
        return String.format("%s://%s:%d%s",
                uri.getScheme(),
                uri.getHost(),
                port,
                ARTIFACT_ID);
    }
}
