package org.wildfly.quickstarts.mpjwt;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.wildfly.quickstarts.mpjwt.TestUtils.HELLO_WORLD;
import static org.wildfly.quickstarts.mpjwt.TestUtils.ROOT_PATH;
import static org.wildfly.quickstarts.mpjwt.TestUtils.getServerHost;

public class BasicRuntimeIT {

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    @Test
    public void testHTTPEndpointIsAvailable() throws IOException, InterruptedException, URISyntaxException {
        HttpGet httpGet = new HttpGet(getServerHost() + ROOT_PATH + HELLO_WORLD);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        assertEquals("Successful call", 200, httpResponse.getStatusLine().getStatusCode());

        httpResponse.close();

    }
}
