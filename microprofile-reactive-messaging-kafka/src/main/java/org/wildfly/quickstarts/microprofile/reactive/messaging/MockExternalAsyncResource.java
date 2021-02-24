package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

/**
 * Class simulating external data being made available asynchronously.
 * In the real world this could be for example a REST client making
 * asynchronous calls. The implementation details of this class are
 * not important. The key point is that the {@link #getNextValue()}
 * method returns a CompletionStage which returns a string. These
 * strings are 'emitted' every two seconds.
 *
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@ApplicationScoped
public class MockExternalAsyncResource {
    private static final int TICK = 2000;

    private final String[] values = new String[]{
            "Hello",
            "world",
            "Reactive",
            "Messaging",
            "with",
            "Kafka"
    };

    private ScheduledExecutorService delayedExecutor = Executors.newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
    private final AtomicInteger count = new AtomicInteger(0);
    private long last = System.currentTimeMillis();

    @PreDestroy
    public void stop() {
        delayedExecutor.shutdown();
    }

    public CompletionStage<String> getNextValue() {
        synchronized (this) {
            CompletableFuture<String> cf = new CompletableFuture<>();
            long now = System.currentTimeMillis();
            long next = TICK + last;
            long delay = next - now;
            last = next;
            NextOrRandom nor = new NextOrRandom(cf);
            delayedExecutor.schedule(nor, delay , TimeUnit.MILLISECONDS);
            return cf;
        }
    }

    private class NextOrRandom implements Runnable {
        private final CompletableFuture<String> cf;

        public NextOrRandom(CompletableFuture<String> cf) {
            this.cf = cf;
        }

        @Override
        public void run() {
            final int index = count.getAndIncrement();
            if (index < values.length) {
                cf.complete(values[index]);
            } else {
                int random = (int)(Math.random() * values.length);
                cf.complete(values[random]);
            }
        }
    }
}
