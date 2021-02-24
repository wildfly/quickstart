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