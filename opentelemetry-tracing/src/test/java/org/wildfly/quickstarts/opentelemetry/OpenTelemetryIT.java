/*
 * Copyright 2023 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.quickstarts.opentelemetry;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.Test;

public class OpenTelemetryIT {
    @Test
    public void testImplicit() throws IOException, InterruptedException, URISyntaxException {
        String applicationUrl = BasicRuntimeIT.getApplicationUrl();

        final HttpClient client = BasicRuntimeIT.getHttpClient();
        final HttpRequest implicit = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/implicit-trace")).GET().build();
        assertEquals(200, client.send(implicit, HttpResponse.BodyHandlers.ofString()).statusCode());
    }

    @Test
    public void testExplicit() throws IOException, InterruptedException, URISyntaxException {
        String applicationUrl = BasicRuntimeIT.getApplicationUrl();

        final HttpClient client = BasicRuntimeIT.getHttpClient();
        final HttpRequest implicit = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/explicit-trace")).GET().build();
        assertEquals(200, client.send(implicit, HttpResponse.BodyHandlers.ofString()).statusCode());
    }
}
