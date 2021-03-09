/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.sql.Timestamp;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class MessagingBean {

    @Inject
    MockExternalAsyncResource producer;

    @Inject
    DatabaseBean dbBean;

    @Outgoing("source")
    public CompletionStage<String> sendInVm() {
        return producer.getNextValue();
    }

    @Incoming("source")
    @Outgoing("filter")
    public String logAllMessages(String message) {
        System.out.println("Received " + message);
        return message;
    }

    @Incoming("filter")
    @Outgoing("sender")
    public PublisherBuilder<String> filter(PublisherBuilder<String> messages) {
        return messages
                .filter(s -> s.equals("Hello") || s.equals("Kafka"));
    }

    @Incoming("sender")
    @Outgoing("to-kafka")
    public Publisher<TimedEntry> sendToKafka(Publisher<String> messages) {
        return ReactiveStreams.fromPublisher(messages)
                .map(s -> new TimedEntry(new Timestamp(System.currentTimeMillis()), s))
                .buildRs();
    }

    @Incoming("from-kafka")
    public void receiveFromKafka(TimedEntry message) {
        System.out.println("Received from Kafka, storing it in database: " + message);
        dbBean.store(message);
    }

}