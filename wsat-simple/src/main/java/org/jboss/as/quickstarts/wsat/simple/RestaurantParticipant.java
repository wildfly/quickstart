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
package org.jboss.as.quickstarts.wsat.simple;

import java.io.Serializable;

import com.arjuna.wst.Aborted;
import com.arjuna.wst.Durable2PCParticipant;
import com.arjuna.wst.Prepared;
import com.arjuna.wst.SystemException;
import com.arjuna.wst.Vote;
import com.arjuna.wst.WrongStateException;

/**
 * An adapter class that exposes the RestaurantManager as a WS-T Atomic Transaction participant.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 */
public class RestaurantParticipant implements Durable2PCParticipant, Serializable {
    private static final long serialVersionUID = 1L;

    // The back-end resource for managing bookings
    private MockRestaurantManager mockRestaurantManager = MockRestaurantManager.getSingletonInstance();

    // The transaction ID for this transaction
    private String txID;

    /**
     * Creates a new participant for this transaction. Participants and transaction instances have a one-to-one mapping.
     *
     * @param txID the ID of the transaction tht this participant will be enlisted within.
     */
    public RestaurantParticipant(String txID) {
        this.txID = txID;
    }

    /**
     * Invokes the prepare step of the business logic, reporting activity and outcome.
     *
     * @return Prepared where possible, Aborted where necessary.
     * @throws WrongStateException
     * @throws SystemException
     */
    public Vote prepare() throws WrongStateException, SystemException {
        // Log the event and invoke the prepare operation
        // on the back-end logic.
        System.out.println("[SERVICE] Prepare called on participant, about to prepare the back-end resource");
        boolean success = mockRestaurantManager.prepare(txID);

        // Map the return value from
        // the business logic to the appropriate Vote type.
        if (success) {
            System.out.println("[SERVICE] back-end resource prepared, participant votes prepared");
            return new Prepared();
        } else {
            System.out.println("[SERVICE] back-end resource failed to prepare, participant votes aborted");
            return new Aborted();
        }
    }

    /**
     * Invokes the commit step of the business logic.
     *
     * @throws WrongStateException
     * @throws SystemException
     */
    public void commit() throws WrongStateException, SystemException {
        // Log the event and invoke the commit operation
        // on the backend business logic.
        System.out.println("[SERVICE] all participants voted 'prepared', so coordinator tells the participant to commit");
        mockRestaurantManager.commit(txID);
    }

    /**
     * Invokes the rollback operation on the business logic.
     *
     * @throws WrongStateException
     * @throws SystemException
     */
    public void rollback() throws WrongStateException, SystemException {
        // Log the event and invoke the rollback operation
        // on the backend business logic.
        System.out
                .println("[SERVICE] one or more participants voted 'aborted' or a failure occurred, so coordinator tells the participant to rollback");
        mockRestaurantManager.rollback(txID);
    }

    public void unknown() throws SystemException {
        System.out.println("RestaurantParticipantAT.unknown");
    }

    public void error() throws SystemException {
        System.out.println("RestaurantParticipantAT.error");
    }

}
