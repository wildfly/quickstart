package org.wildfly.quickstarts.microprofile.metrics;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CountDownLatch;

@Path("/")
@ApplicationScoped
public class PrimeNumberChecker {

    private static final long COUNTER_INCREMENT = 42;
    private long highestPrimeNumberSoFar = 2;

    @GET
    @Path("/prime/{number}")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "performedChecks", displayName="Performed Checks", description = "How many prime checks have been performed.")
    @Timed(name = "checksTimer", absolute = true, description = "A measure of how long it takes to perform the primality test.", unit = MetricUnits.MILLISECONDS)
    @Metered(name = "checkIfPrimeFrequency", absolute = true)
    public String checkIfPrime(@PathParam("number") long number) {
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
            if (number % i == 0) {
                return number + " is not prime, is divisible by " + i + ".";
            }
        }

        if (number > highestPrimeNumberSoFar) {
            highestPrimeNumberSoFar = number;
        }

        return number + " is prime.";
    }

    @Gauge(name = "highestPrimeNumberSoFar", unit = MetricUnits.NONE, description = "Highest prime number so far.")
    public Long highestPrimeNumberSoFar() {
        return highestPrimeNumberSoFar;
    }

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @GET
    @Path("/parallel")
    @ConcurrentGauge(name = "parallelAccess", description = "Number of parallel accesses")
    public void parallelAccess() throws InterruptedException {
        countDownLatch.await();
        System.out.println("DONE");
    }

    @GET
    @Path("/parallel-finish")
    public void parallelFinish() {
        countDownLatch.countDown();
        System.out.println("Finished parallel execution");
    }

    @Inject
    @Metric(name = "injectedCounter", absolute = true)
    private Counter injectedCounter;

    @GET
    @Path("/injected-metric")
    @Produces(MediaType.TEXT_PLAIN)
    public String injectedMetric() {
        injectedCounter.inc();
        return "counter invoked";
    }

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    private MetricRegistry applicationRegistry;

    @GET
    @Path("/registry")
    public void registry() {
        // register a new application scoped metric
        Counter programmaticCounter = applicationRegistry.counter(Metadata.builder()
            .withName("programmaticCounter")
            .withDescription("Programmatically created counter")
            .build());

        programmaticCounter.inc(COUNTER_INCREMENT);
    }

    @GET
    @Path("duplicates")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "duplicatedCounter", absolute = true, tags = {"type=original"})
    public String duplicates() {
        return "duplicated metrics";
    }

    @GET
    @Path("duplicates2")
    @Produces(MediaType.TEXT_PLAIN)
    @Counted(name = "duplicatedCounter", absolute = true, tags = {"type=copy"})
    public String duplicates2() {
        return "duplicated metrics";
    }
}
