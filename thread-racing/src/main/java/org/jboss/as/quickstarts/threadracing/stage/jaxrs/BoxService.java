/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.threadracing.stage.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Random;

/**
 * A REST service which racers may use to do a pit stop.
 *
 * The {@link javax.ws.rs.Path} annotation defines the path relative to the {@link org.jboss.as.quickstarts.threadracing.stage.jaxrs.BoxApplication} path. A value of "/" means the service will use the parent path.
 *
 * @author Eduardo Martins
 */
@Path("/")
public class BoxService {

    private static final Random random = new Random();

    /**
     * A method to handle GET requests on pitStop path (relative to the one defined at class level).
     *
     * The path may include segments that will match any value, {racer} is an example of such path param. The actual param value may be provided as a method param, by annotation the method param with {@link javax.ws.rs.PathParam}, as done in this method signature.
     * @param racer
     * @return
     */
    @GET
    @Path("pitStop/{racer}")
    public Response pitStop(@PathParam("racer") String racer) {
        // a pit stop simulation, a random 1-5ms thread sleep
        try {
            Thread.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // the response may include payload, such as JSON data, here we just return the basic "OK" response.
        return Response.ok().build();
    }
}
