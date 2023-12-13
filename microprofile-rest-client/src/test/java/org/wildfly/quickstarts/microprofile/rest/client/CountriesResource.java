/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.quickstarts.microprofile.rest.client;

import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.wildfly.quickstarts.microprofile.Country;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

@Path("/country")
@ApplicationScoped
public class CountriesResource {

    @Inject
    @RestClient
    private CountriesServiceClient countriesServiceClient;

    @Inject
    @ConfigProperty(name = "server.host")
    private String serverHost;

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
                .baseUrl(new URL(serverHost))
                .build(CountriesServiceClient.class);
        return client.getByName(name);
    }

    @GET
    @Path("/name-async/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Country> nameAsync(@PathParam("name") String name) {
        CompletionStage<Country> completionStage = countriesServiceClient.getByNameAsync(name);
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            throw new WebApplicationException(e);
        }
        return completionStage;
    }
}
