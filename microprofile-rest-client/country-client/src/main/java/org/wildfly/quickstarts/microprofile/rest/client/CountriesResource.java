package org.wildfly.quickstarts.microprofile.rest.client;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.wildfly.quickstarts.microprofile.rest.client.model.Country;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletionStage;

@Path("/country")
@ApplicationScoped
public class CountriesResource {

    @Inject
    @RestClient
    private CountriesServiceClient countriesServiceClient;

    @GET
    @Path("/cdi/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Country cdiName(@PathParam("name") String name) {
        try {
            return countriesServiceClient.getByName(name);
        } catch (NotFoundException e) {
            return null;
        }
    }

    @GET
    @Path("/programmatic/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Country programmaticName(@PathParam("name") String name) throws MalformedURLException {
        CountriesServiceClient client = RestClientBuilder.newBuilder()
            .baseUrl(new URL("http://localhost:8080/country-server"))
            .build(CountriesServiceClient.class);

        return client.getByName(name);
    }

    @GET
    @Path("/name-async/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Country> nameAsync(@PathParam("name") String name) {
        CompletionStage<Country> completionStage = countriesServiceClient.getByNameAsync(name);

        System.out.println("Async request happening now...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return completionStage;
    }
}
