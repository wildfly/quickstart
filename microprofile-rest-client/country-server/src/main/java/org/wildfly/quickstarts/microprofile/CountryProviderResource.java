package org.wildfly.quickstarts.microprofile;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class CountryProviderResource {

    private static final Map<String, Country> countries = new HashMap<>();

    public CountryProviderResource() {
        countries.put("Czechia", new Country("Czechia", "Prague", "CZK"));
        countries.put("France", new Country("France", "Paris", "EUR"));
        countries.put("Slovakia", new Country("Slovakia", "Bratislava", "EUR"));
        countries.put("Switzerland", new Country("Switzerland", "Bern", "CHF"));
        countries.put("Netherlands", new Country("Netherlands", "Amsterdam", "EUR"));
        countries.put("USA", new Country("USA", "Washington, D.C.", "USD"));
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@PathParam("name") String name) {
        Country country = countries.get(name);

        if (country != null) {
            return Response.ok(country).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
