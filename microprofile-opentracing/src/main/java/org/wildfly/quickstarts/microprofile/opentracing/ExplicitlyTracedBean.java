package org.wildfly.quickstarts.microprofile.opentracing;

import io.opentracing.Span;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExplicitlyTracedBean {

    @Inject
    private Tracer tracer;

    @Traced
    public String getHello() {
        Span prepareHelloSpan = tracer.buildSpan("prepare-hello").start();

        String hello = "hello";

        Span processHelloSpan = tracer.buildSpan("process-hello").start();

        hello = hello.toUpperCase();

        processHelloSpan.finish();
        prepareHelloSpan.finish();

        return hello;
    }
}
