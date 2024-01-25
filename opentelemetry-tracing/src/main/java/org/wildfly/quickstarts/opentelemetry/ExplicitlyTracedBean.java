package org.wildfly.quickstarts.opentelemetry;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RequestScoped
public class ExplicitlyTracedBean {

    @Inject
    private Tracer tracer;

    public String getHello() {
        Span prepareHelloSpan = tracer.spanBuilder("prepare-hello").startSpan();
        prepareHelloSpan.makeCurrent();

        String hello = "hello";

        Span processHelloSpan = tracer.spanBuilder("process-hello").startSpan();
        processHelloSpan.makeCurrent();

        hello = hello.toUpperCase();

        processHelloSpan.end();
        prepareHelloSpan.end();

        return hello;
    }
}
