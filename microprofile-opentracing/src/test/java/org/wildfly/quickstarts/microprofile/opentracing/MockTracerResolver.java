package org.wildfly.quickstarts.microprofile.opentracing;

import io.opentracing.Tracer;
import io.opentracing.contrib.tracerresolver.TracerResolver;
import io.opentracing.mock.MockTracer;

public class MockTracerResolver extends TracerResolver {

    @Override
    protected Tracer resolve() {
        return new MockTracer();
    }
}
