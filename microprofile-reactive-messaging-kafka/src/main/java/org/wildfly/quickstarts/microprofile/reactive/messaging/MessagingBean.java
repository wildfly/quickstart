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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;


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
    public Message<TimedEntry> sendToKafka(String msg) {
        try {
            System.out.println("TEMP (sendToKafka) " + msg);
            TimedEntry te = new TimedEntry(new Timestamp(System.currentTimeMillis()), msg);
            Message<TimedEntry> m = Message.of(te);
            // Just use the hash as the Kafka key for this example
            int key = te.getMessage().hashCode();

            // Create Metadata containing the Kafka key
            OutgoingKafkaRecordMetadata<Integer> md = OutgoingKafkaRecordMetadata
                    .<Integer>builder()
                    .withKey(key)
                    .build();

            // The returned message will have the metadata added
            return KafkaMetadataUtil.writeOutgoingKafkaMetadata(m, md);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Incoming("from-kafka")
    public CompletionStage<Void> receiveFromKafka(Message<TimedEntry> message) {
        try {
            TimedEntry payload = message.getPayload();
            System.out.println("TEMP (receiveFromKafka) " + payload);

            IncomingKafkaRecordMetadata<Integer, TimedEntry> md = KafkaMetadataUtil.readIncomingKafkaMetadata(message).get();
            String msg =
                    "Received from Kafka, storing it in database\n" +
                    "\t%s\n" +
                    "\tkey: %d; partition: %d, topic: %s";
            msg = String.format(msg, payload, md.getKey(), md.getPartition(), md.getTopic());
            System.out.println(msg);
            dbBean.store(payload);
            return message.ack();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}