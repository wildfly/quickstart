package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.reactivestreams.Publisher;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Path("/user")
@Produces(MediaType.TEXT_PLAIN)
public interface UserClient {

    @POST
    @Path("{value}")
    @Consumes(MediaType.TEXT_PLAIN)
    Response send(@PathParam("value") String value);

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    Publisher<String> get();
}
