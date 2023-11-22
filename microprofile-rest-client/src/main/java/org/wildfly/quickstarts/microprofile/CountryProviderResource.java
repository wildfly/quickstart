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

package org.wildfly.quickstarts.microprofile;

import java.util.Map;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class CountryProviderResource {

    private static final Map<String, Country> countries = Map.ofEntries(
            Map.entry("Czechia", new Country("Czechia", "Prague", "CZK")),
            Map.entry("France", new Country("France", "Paris", "EUR")),
            Map.entry("Slovakia", new Country("Slovakia", "Bratislava", "EUR")),
            Map.entry("Switzerland", new Country("Switzerland", "Bern", "CHF")),
            Map.entry("Netherlands", new Country("Netherlands", "Amsterdam", "EUR")),
            Map.entry("USA", new Country("USA", "Washington, D.C.", "USD"))
    );

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@PathParam("name") String name) {
        final Country country = countries.get(name);
        if (country != null) {
            return Response.ok(country).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
