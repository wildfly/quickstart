/*
 * JBoss, Home of Professional Open Source
 * Copyright 2023, Red Hat, Inc. and/or its affiliates, and individual
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
package org.wildfly.quickstarts.microprofile.lra;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.wildfly.quickstarts.microprofile.lra.TestUtils.getServerHost;

public class MicroProfileLRAIT {

    private Client client;

    @Before
    public void before() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void testLRAExecutionSuccess() {
        Response response = getResponse("/participant1/work");
        assertEquals(200, response.getStatus());
        String lraId = response.readEntity(String.class);

        response = getResponse("/participant1/result");
        assertEquals(200, response.getStatus());
        ParticipantResult participantResult1 = response.readEntity(ParticipantResult.class);

        response = getResponse("/participant2/result");
        assertEquals(200, response.getStatus());
        ParticipantResult participantResult2 = response.readEntity(ParticipantResult.class);

        assertEquals(lraId, participantResult1.getWorkLRAId());
        String recoveryId1 = participantResult1.getWorkRecoveryId();

        assertEquals(lraId, participantResult2.getWorkLRAId());
        String recoveryId2 = participantResult2.getWorkRecoveryId();

        // LRA closed successfully, Complete callbacks called
        assertEquals(lraId, participantResult1.getCompleteLRAId());
        assertEquals(recoveryId1, participantResult1.getCompleteRecoveryId());
        assertEquals(lraId, participantResult2.getCompleteLRAId());
        assertEquals(recoveryId2, participantResult2.getCompleteRecoveryId());

        // Compensate callbacks should not be called
        assertNull(participantResult1.getCompensateLRAId());
        assertNull(participantResult1.getCompensateRecoveryId());
        assertNull(participantResult2.getCompensateLRAId());
        assertNull(participantResult2.getCompensateRecoveryId());
    }

    @Test
    public void testLRAExecutionFailure() {
        Response response = getResponse("/participant1/work",
            webTarget -> webTarget.queryParam("failLRA", "true"));
        assertEquals(500, response.getStatus());
        String lraId = response.readEntity(String.class);

        response = getResponse("/participant1/result");
        assertEquals(200, response.getStatus());
        ParticipantResult participantResult1 = response.readEntity(ParticipantResult.class);

        response = getResponse("/participant2/result");
        assertEquals(200, response.getStatus());
        ParticipantResult participantResult2 = response.readEntity(ParticipantResult.class);

        assertEquals(lraId, participantResult1.getWorkLRAId());
        String recoveryId1 = participantResult1.getWorkRecoveryId();

        assertEquals(lraId, participantResult2.getWorkLRAId());
        String recoveryId2 = participantResult2.getWorkRecoveryId();

        // LRA canceled on failure, Compensate callbacks called
        assertEquals(lraId, participantResult1.getCompensateLRAId());
        assertEquals(recoveryId1, participantResult1.getCompensateRecoveryId());
        assertEquals(lraId, participantResult2.getCompensateLRAId());
        assertEquals(recoveryId2, participantResult2.getCompensateRecoveryId());

        // Complete callbacks should not be called
        assertNull(participantResult1.getCompleteLRAId());
        assertNull(participantResult1.getCompleteRecoveryId());
        assertNull(participantResult2.getCompleteLRAId());
        assertNull(participantResult2.getCompleteRecoveryId());
    }

    private Response getResponse(String path) {
        return getResponse(path, null);
    }

    private Response getResponse(String path, Function<WebTarget, WebTarget> weTargetProcessor) {
        WebTarget target = client.target(getServerHost())
            .path(path);

        if (weTargetProcessor != null) {
            target = weTargetProcessor.apply(target);
        }

        return target.request().get();
    }
}
