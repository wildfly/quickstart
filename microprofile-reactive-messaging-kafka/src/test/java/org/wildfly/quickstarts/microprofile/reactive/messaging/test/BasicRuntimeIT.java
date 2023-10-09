package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.wildfly.quickstarts.microprofile.reactive.messaging.test.TestUtils.getServerHost;

public class BasicRuntimeIT {

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    @Test
    public void testHTTPEndpointIsAvailable() throws IOException {
        HttpGet httpGet = new HttpGet(getServerHost());
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());

        httpResponse.close();

    }
}
