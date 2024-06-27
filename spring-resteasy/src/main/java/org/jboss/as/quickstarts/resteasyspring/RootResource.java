package org.jboss.as.quickstarts.resteasyspring;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.UriInfo;

@Path("/")
public class RootResource {

    @GET
    @Produces("text/html")
    public String getRootResponse(@Context UriInfo uriInfo) {
        String baseURI = uriInfo.getBaseUri().toString();
        if (!baseURI.endsWith("/")) baseURI += '/';
        baseURI = baseURI + "main/";
        String responsemsg = "Hello There! Welcome to WildFly!" +
                "<br>Spring-resteasy quickstart has been deployed and running successfully. " +
                "<br>You can find the available operations <a href='"+baseURI+"'>here</a> or in the included README file.";
        return responsemsg;
    }
}
