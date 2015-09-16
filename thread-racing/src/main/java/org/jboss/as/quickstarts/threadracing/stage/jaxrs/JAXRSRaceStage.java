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

import org.jboss.as.quickstarts.threadracing.EnvironmentProperties;
import org.jboss.as.quickstarts.threadracing.Race;
import org.jboss.as.quickstarts.threadracing.stage.RaceStage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * The JAX-RS 2.0 race stage implements the race's boxes, which a racer uses to do a pit stop.
 *
 * @author Eduardo Martins
 */
public class JAXRSRaceStage implements RaceStage {

    @Override
    public void run(Race.Registration registration) throws Exception {
        // build the REST service uri from race's environment
        final Map<String, String> environment = registration.getEnvironment();
        final String pitStopURI = new StringBuilder("http://")
            .append(environment.get(EnvironmentProperties.SERVER_NAME))
            .append(':')
            .append(environment.get(EnvironmentProperties.SERVER_PORT))
            .append(environment.get(EnvironmentProperties.ROOT_PATH))
            .append('/')
            .append(BoxApplication.PATH)
            .append("/pitStop")
            .toString();
        // create and setup the new standard JAX-RS client (and its web target)
        final Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(pitStopURI);
        // get current time
        long now = System.currentTimeMillis();
        // box box box, i.e. send a request to the Box rest service, with the racers name provided as param 'racer'
        final Response response = target.path("{racer}").resolveTemplate("racer", registration.getRacer().getName()).request().get();
        if (response.getStatus() != 200) {
            throw new IllegalStateException("PIT STOP failure trouble " + response.getStatus());
        } else {
            // broadcast a msg indicating the duration of the pit stop operation
            registration.broadcast("PIT STOP in " + (System.currentTimeMillis() - now) + "ms");
        }
    }
}
