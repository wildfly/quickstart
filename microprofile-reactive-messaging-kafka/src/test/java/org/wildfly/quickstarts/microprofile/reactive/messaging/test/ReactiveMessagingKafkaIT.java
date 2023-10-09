/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2020, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.wildfly.quickstarts.microprofile.reactive.messaging.test.TestUtils.getServerHost;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ReactiveMessagingKafkaIT {


    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    private static final long TIMEOUT = 30000;

    @Test
    public void testDbEntries() throws Throwable {
        HttpGet httpGet = new HttpGet(getServerHost() + "/db");
        long end = System.currentTimeMillis() + TIMEOUT;
        boolean done = false;
        while (!done) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                done = checkResponse(httpResponse, System.currentTimeMillis() > end);
                Thread.sleep(1000);
            }
        }
    }

    @Test
    public void testUserApi() throws Throwable {
        final UserClient client = RestClientBuilder.newBuilder()
                .baseUrl(new URL(getServerHost()))
                .build(UserClient.class);

        final ListSubscriber taskA = new ListSubscriber(new CountDownLatch(3));
        client.get().subscribe(taskA);
        final ListSubscriber taskB = new ListSubscriber(new CountDownLatch(3));
        client.get().subscribe(taskB);

        client.send("one");
        client.send("two");
        client.send("three");

        taskA.latch.await();
        taskB.latch.await();

        long end = System.currentTimeMillis() + TIMEOUT;
        while (System.currentTimeMillis() < end) {
            Thread.sleep(200);
            if (taskA.lines.size() == 3) {
                break;
            }
        }

        checkAsynchTask(taskA, "one", "two", "three");
        checkAsynchTask(taskB, "one", "two", "three");
    }

    private void checkAsynchTask(ListSubscriber task, String... values) {
        Assert.assertEquals(3, task.lines.size());
        for (int i = 0; i < values.length; i++) {
            Assert.assertTrue("Line " + i + ": " + task.lines.get(i), task.lines.get(i).contains(values[i]));
        }
    }

    private boolean checkResponse(CloseableHttpResponse response, boolean fail) throws Throwable {
        String s;
        List<String> lines = new ArrayList<>();
        try {
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            }
            Assert.assertTrue("Expected >= 3 lines in:\n" + lines, lines.size() >= 3);
        } catch (Throwable throwable) {
            if (fail) {
                throw throwable;
            }
            return false;
        }

        Assert.assertNotEquals("Expected to find 'Hello' on line 0 of:\n" + lines, -1, lines.get(0).indexOf("Hello"));
        Assert.assertNotEquals("Expected to find 'Kafka' on line 1 of:\n" + lines, -1, lines.get(1).indexOf("Kafka"));
        for (int i = 2; i < lines.size(); i++) {
            Assert.assertNotEquals(
                    "Expected to find 'Hello' or 'Kafka' on line " + i +
                            " of:\n" + lines, -2, lines.get(i).indexOf("Hello") + lines.get(i).indexOf("Kafka"));
        }
        return true;
    }

    private static class ListSubscriber implements Subscriber<String> {
        private final CountDownLatch latch;
        final List<String> lines;

        private ListSubscriber(final CountDownLatch latch) {
            this.latch = latch;
            lines = new ArrayList<>();
        }

        @Override
        public void onSubscribe(final Subscription s) {
            s.request(latch.getCount());
        }

        @Override
        public void onNext(final String s) {
            lines.add(s);
            latch.countDown();
        }

        @Override
        public void onError(final Throwable t) {

        }

        @Override
        public void onComplete() {

        }
    }
}
