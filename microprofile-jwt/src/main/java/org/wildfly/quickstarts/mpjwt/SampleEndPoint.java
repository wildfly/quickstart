/*
 * JBoss, Home of Professional Open Source
 * Copyright 2020 Red Hat, Inc., and individual contributors
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

package org.wildfly.quickstarts.mpjwt;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/Sample")
public class SampleEndPoint {

    @GET
    @Path("/helloworld")
    public String helloworld(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String caller = principal == null ? "anonymous" : principal.getName();

        return "Hello " + caller;
    }

    @Inject
    JsonWebToken jwt;

    @GET()
    @Path("/subscription")
    @RolesAllowed({ "Subscriber" })
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        String helloReply = String.format("hello + %s, hasJWT: %s", name, hasJWT);
        return helloReply;
    }

    @Inject
    @Claim(standard = Claims.birthdate)
    Optional<String> birthdate;

    @GET()
    @Path("/birthday")
    @RolesAllowed({ "Subscriber" })
    public String birthday() {
        if (birthdate.isPresent()) {
            LocalDate birthdate = LocalDate.parse(this.birthdate.get().toString());
            LocalDate today = LocalDate.now();
            LocalDate next = birthdate.withYear(today.getYear());
            if (today.equals(next)) {
                return "Happy Birthday";
            }
            if (next.isBefore(today)) {
                next = next.withYear(next.getYear() + 1);
            }
            Period wait = today.until(next);
            return String.format("%d months and %d days until your next birthday.", wait.getMonths(), wait.getDays());
        }
        return "Sorry, we don't know your birthdate.";
    }

}