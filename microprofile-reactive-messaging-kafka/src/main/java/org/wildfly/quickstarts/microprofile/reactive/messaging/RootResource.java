package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RootResource {

    @Inject
    DatabaseBean dbBean;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRootResponse() {
        List<TimedEntry> entries = dbBean.loadAllTimedEntries();
        StringBuffer sb = new StringBuffer();
        for (TimedEntry t : entries) {
            sb.append(t);
            sb.append("\n");
        }
        return sb.toString();
    }
}
