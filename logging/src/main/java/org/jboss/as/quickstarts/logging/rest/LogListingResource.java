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

package org.jboss.as.quickstarts.logging.rest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class LogListingResource {
    private static final String LOG_DIR = System.getProperty("jboss.server.log.dir");
    private static final PathMatcher MATCHER = FileSystems.getDefault().getPathMatcher("glob:**/quickstart*.log");
    private static final Logger LOGGER = Logger.getLogger(LogListingResource.class);

    @Inject
    private HttpServletRequest servletRequest;

    @GET
    public List<String> listLogFiles() throws IOException {
        final java.nio.file.Path dir = java.nio.file.Path.of(LOG_DIR);
        try (Stream<java.nio.file.Path> files = Files.list(dir)) {
            return files.filter(MATCHER::matches).map(f -> f.getFileName().toString()).collect(Collectors.toList());
        }
    }

    @GET
    @Path("read/{file}")
    public List<String> read(@PathParam("file") final String file) throws IOException {
        final java.nio.file.Path path = java.nio.file.Path.of(LOG_DIR, file);
        if (Files.notExists(path)) {
            throw new NotFoundException("Failed to find file " + file);
        }
        return Files.readAllLines(path);
    }

    @POST
    @Path("/trace")
    public Response trace() {
        LOGGER.tracef("This is an TRACE message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }

    @POST
    @Path("/debug")
    public Response debug() {
        LOGGER.debugf("This is an DEBUG message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }

    @POST
    @Path("/info")
    public Response info() {
        LOGGER.infof("This is an INFO message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }

    @POST
    @Path("/warn")
    public Response warn() {
        LOGGER.warnf("This is an WARN message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }

    @POST
    @Path("/error")
    public Response error() {
        LOGGER.errorf("This is an ERROR message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }

    @POST
    @Path("/fatal")
    public Response fatal() {
        LOGGER.fatalf("This is an FATAL message from %s:%d", servletRequest.getRemoteAddr(), servletRequest.getRemotePort());
        return Response.ok().build();
    }
}
