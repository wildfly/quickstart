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
package org.wildfly.quickstarts.micrometer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.Test;

public class MicrometerIT {
    public static final int MAX_NUMBER = 15;

    @Test
    public void makeSeveralApplicationRequests() throws IOException, InterruptedException, URISyntaxException {
        String applicationUrl = BasicRuntimeIT.getApplicationUrl();

        final HttpClient client = BasicRuntimeIT.getHttpClient();
        final HttpRequest requestPrime = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/prime/13")).GET().build();
        final HttpRequest requestHighest = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/prime/highest")).GET().build();
        final HttpRequest requestDuplicates = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/duplicates")).GET().build();
        final HttpRequest requestDuplicates2 = HttpRequest.newBuilder().uri(new URI(applicationUrl + "/duplicates2")).GET().build();



        // Make several requests to make the metrics more interesting
        for (int i = 0; i < MAX_NUMBER; i++) {
            assertEquals(200, client.send(requestPrime, HttpResponse.BodyHandlers.ofString()).statusCode());
            assertEquals(200, client.send(requestHighest, HttpResponse.BodyHandlers.ofString()).statusCode());
            assertEquals(200, client.send(requestDuplicates, HttpResponse.BodyHandlers.ofString()).statusCode());
            assertEquals(200, client.send(requestDuplicates2, HttpResponse.BodyHandlers.ofString()).statusCode());
        }
    }
}
