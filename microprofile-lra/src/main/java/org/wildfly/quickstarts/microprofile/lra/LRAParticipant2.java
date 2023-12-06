/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.quickstarts.microprofile.lra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.jboss.logging.Logger;

import java.net.URI;

@Path("/participant2")
@ApplicationScoped
public class LRAParticipant2 {

    private static final Logger LOGGER = Logger.getLogger(LRAParticipant2.class);

    private String workLRAId;
    private String workRecoveryId;
    private String completeLRAId;
    private String completeRecoveryId;
    private String compensateLRAId;
    private String compensateRecoveryId;

    @LRA(end = false)
    @GET
    @Path("/work")
    public Response work(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                         @HeaderParam(LRA.LRA_HTTP_RECOVERY_HEADER) URI participantId) {
        LOGGER.infof("Executing action of Participant 2 enlisted in LRA %s " +
            "that was assigned %s participant Id.", lraId, participantId);

        workLRAId = lraId.toASCIIString();
        workRecoveryId = participantId.toASCIIString();
        compensateLRAId = null;
        compensateRecoveryId = null;
        completeLRAId = null;
        completeRecoveryId = null;

        return Response.ok().build();
    }

    @Compensate
    @PUT
    @Path("/compensate")
    public Response compensateWork(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                   @HeaderParam(LRA.LRA_HTTP_RECOVERY_HEADER) URI participantId) {
        LOGGER.infof("Compensating action for Participant 2 (%s) in LRA %s.", participantId, lraId);

        compensateLRAId = lraId.toASCIIString();
        compensateRecoveryId = participantId.toASCIIString();

        return Response.ok().build();
    }

    @Complete
    @PUT
    @Path("/complete")
    public Response completeWork(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) URI lraId,
                                 @HeaderParam(LRA.LRA_HTTP_RECOVERY_HEADER) URI participantId) {
        LOGGER.infof("Complete action for Participant 2 (%s) in LRA %s.", participantId, lraId);

        completeLRAId = lraId.toASCIIString();
        completeRecoveryId = participantId.toASCIIString();

        return Response.ok().build();
    }

    @GET
    @Path("/result")
    @Produces(MediaType.APPLICATION_JSON)
    public ParticipantResult getParticipantResult() {
        return new ParticipantResult(workLRAId, workRecoveryId,
            completeLRAId, completeRecoveryId,
            compensateLRAId, compensateRecoveryId);
    }

}
