package org.wildfly.quickstarts.microprofile.opentracing;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerFactory;
import io.opentracing.mock.MockTracer;

public class MockTracerFactory implements TracerFactory {

    @Override
    public Tracer getTracer() {
        return new MockTracer();
    }
}
