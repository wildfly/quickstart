package org.jboss.as.quickstarts.ejb.multi.server;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class BasicRuntimeIT {
    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080/ejb-multi-server-app-main-web/";

    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        String serverHost = System.getProperty("server.host");
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(serverHost+"/"))
                .GET()
                .build();
        final HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        final String[] bodyLines = response.body().toString().split(System.lineSeparator());
        assertEquals("<meta http-equiv=\"Refresh\" content=\"0; URL=ejbinvoke.jsf\">", bodyLines[bodyLines.length-3].strip());
    }
}
