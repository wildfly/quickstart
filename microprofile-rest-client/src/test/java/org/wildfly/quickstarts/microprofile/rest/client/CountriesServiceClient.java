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

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import org.wildfly.quickstarts.microprofile.Country;

import java.util.concurrent.CompletionStage;

@Path("/")
@RegisterRestClient
@RegisterProvider(NotFoundResponseExceptionMapper.class)
public interface CountriesServiceClient {

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    Country getByName(@PathParam("name") String name);

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    CompletionStage<Country> getByNameAsync(@PathParam("name") String name);
}
