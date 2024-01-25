package org.wildfly.quickstarts.micrometer;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

import java.util.List;

@Path("/")
@ApplicationScoped
public class RootResource {
    static final String METER_HIGHEST_SO_FAR = "prime_highestSoFar";
    static final String METER_PERFORMED_CHECKS = "prime_performedChecks";
    static final String METER_DUPLICATED_COUNTER = "prime_duplicatedCounter";
    private long highestPrimeNumberSoFar = 2;

    @Inject
    private MeterRegistry registry;

    private Counter performCheckCounter;
    private Counter originalCounter;
    private Counter duplicatedCounter;

    @PostConstruct
    private void createMeters() {
        Gauge.builder(METER_HIGHEST_SO_FAR, () -> highestPrimeNumberSoFar)
                .description("Highest prime number so far.")
                .register(registry);
        performCheckCounter = Counter
                .builder(METER_PERFORMED_CHECKS)
                .description("How many prime checks have been performed.")
                .register(registry);
        originalCounter = Counter
                .builder(METER_DUPLICATED_COUNTER)
                .tags(List.of(Tag.of("type", "original")))
                .register(registry);
        duplicatedCounter = Counter
                .builder(METER_DUPLICATED_COUNTER)
                .tags(List.of(Tag.of("type", "copy")))
                .register(registry);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRootResponse() {
        return "Micrometer quickstart deployed successfully. You can find the available operations in the included README file.";
    }

    @GET
    @Path("/prime/{number}")
    public String checkIfPrime(@PathParam("number") long number) throws Exception {
        performCheckCounter.increment();

        Timer timer = registry.timer("prime.timer");

        return timer.recordCallable(() -> {

            if (number < 1) {
                return "Only natural numbers can be prime numbers.";
            }

            if (number == 1) {
                return "1 is not prime.";
            }

            if (number == 2) {
                return "2 is prime.";
            }

            if (number % 2 == 0) {
                return number + " is not prime, it is divisible by 2.";
            }

            for (int i = 3; i < Math.floor(Math.sqrt(number)) + 1; i = i + 2) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //
                }
                if (number % i == 0) {
                    return number + " is not prime, is divisible by " + i + ".";
                }
            }

            if (number > highestPrimeNumberSoFar) {
                highestPrimeNumberSoFar = number;
            }

            return number + " is prime.";
        });
    }

    @GET
    @Path("/prime/highest")
    public String highestPrimeNumberSoFar() {
        return "The highest prime number so far is " + highestPrimeNumberSoFar + ".";
    }

    @GET
    @Path("/duplicates")
    @Produces(MediaType.TEXT_PLAIN)
    public String duplicates() {
        originalCounter.increment();
        return "duplicated metrics";
    }

    @GET
    @Path("/duplicates2")
    @Produces(MediaType.TEXT_PLAIN)
    public String duplicates2() {
        duplicatedCounter.increment();
        return "duplicated metrics";
    }
}
