package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
