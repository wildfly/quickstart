package org.wildfly.quickstarts.microprofile.opentracing;

import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.smallrye.opentracing.contrib.resolver.TracerFactory;

public class MockTracerFactory implements TracerFactory {

    @Override
    public Tracer getTracer() {
        return new MockTracer();
    }
}
