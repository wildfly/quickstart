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

import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@ApplicationScoped
public class UserMessagingBean {

    @Inject
    @Channel("user")
    private Emitter<String> emitter;

    private BroadcastPublisher<String> broadcastPublisher;

    public UserMessagingBean() {
        //Needed for CDI spec compliance
        //The @Inject annotated one will be used
    }

    @Inject
    public UserMessagingBean(@Channel("user") Publisher<String> receiver) {
        this.broadcastPublisher = new BroadcastPublisher(receiver);
    }

    @PreDestroy
    public void destroy() {
        broadcastPublisher.close();
    }

    public Response send(String value) {
        System.out.println("Sending " + value);
        emitter.send(value);
        return Response.accepted().build();
    }

    public Publisher<String> getPublisher() {
        return broadcastPublisher;
    }

    private class BroadcastPublisher<T> implements Publisher<T> {
        private final Publisher basePublisher;
        private volatile Subscription baseSubscription;
        private final CopyOnWriteArraySet<Subscriber<? super T>> subscribers = new CopyOnWriteArraySet<>();

        BroadcastPublisher(Publisher<T> publisher) {
            this.basePublisher = publisher;
            publisher.subscribe(new Subscriber() {
                @Override
                public void onSubscribe(Subscription subscription) {
                    baseSubscription = subscription;
                    subscription.request(1);
                }

                @Override
                public void onNext(Object o) {
                    System.out.println("Received " + o + " - forwarding it to all the subscribers");
                    for (Subscriber s : subscribers) {
                        s.onNext(o);
                    }
                    baseSubscription.request(1);
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    for (Subscriber s : subscribers) {
                        s.onNext(throwable);
                    }
                }

                @Override
                public void onComplete() {
                    for (Subscriber s : subscribers) {
                        s.onComplete();
                    }
                    baseSubscription.cancel();
                }
            });
        }

        @Override
        public void subscribe(Subscriber<? super T> subscriber) {
            subscribers.add(subscriber);
            subscriber.onSubscribe(new Subscription() {
                @Override
                public void request(long l) {
                    if (l != 1) {
                        throw new IllegalArgumentException("You can only request one entry. Instead you requested " + l);
                    }
                }

                @Override
                public void cancel() {
                    subscribers.remove(subscriber);
                }
            });
        }

        void close() {
            baseSubscription.cancel();
            subscribers.removeAll(subscribers);
        }
    }
}
