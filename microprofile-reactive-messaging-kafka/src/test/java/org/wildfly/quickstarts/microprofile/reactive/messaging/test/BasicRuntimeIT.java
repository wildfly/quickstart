package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.wildfly.quickstarts.microprofile.reactive.messaging.test.TestUtils.getServerHost;

public class BasicRuntimeIT {

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    @Test
    public void testHTTPEndpointIsAvailable() throws IOException {
        HttpGet httpGet = new HttpGet(getServerHost());
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals(200, httpResponse.getStatusLine().getStatusCode(), "Successful call");

        httpResponse.close();

    }
}
