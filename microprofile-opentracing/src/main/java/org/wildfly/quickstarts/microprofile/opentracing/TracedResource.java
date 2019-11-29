package org.wildfly.quickstarts.microprofile.opentracing;

import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@ApplicationScoped
public class TracedResource {

    @GET
    @Path("/traced")
    @Produces(MediaType.TEXT_PLAIN)
    @Traced(operationName = "hello-operation")
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/notTraced")
    @Produces(MediaType.TEXT_PLAIN)
    @Traced(false)
    public String notTraced() {
        return "notTraced";
    }

    @Inject
    private ExplicitlyTracedBean tracedBean;

    @GET
    @Path("/cdi-trace")
    @Produces(MediaType.TEXT_PLAIN)
    public String cdiHello() {
        return tracedBean.getHello();
    }
}
