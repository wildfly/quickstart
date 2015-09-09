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
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

import com.arjuna.wst.BusinessAgreementWithParticipantCompletionParticipant;
import com.arjuna.wst.FaultedException;
import com.arjuna.wst.SystemException;
import com.arjuna.wst.WrongStateException;
import com.arjuna.wst11.ConfirmCompletedParticipant;

import java.io.Serializable;

/**
 * An adapter class that exposes the SetManager as a WS-BA participant using the 'Participant Completion' protocol.
 *
 * The Set Service only allows a single item to be added to the set in any given transaction. So, this means it can complete at
 * the end of the addValueToSet call, rather than having to wait for the coordinator to tell it to do so. Hence it uses a
 * participant which implements the 'participant completion' protocol.
 *
 * @author Paul Robinson (paul.robinson@redhat.com)
 */
public class SetParticipantBA implements BusinessAgreementWithParticipantCompletionParticipant, ConfirmCompletedParticipant,
    Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    /**
     * Participant instances are related to business method calls in a one to one manner.
     *
     * @param value the value to remove from the set during compensation
     */
    public SetParticipantBA(String value) {
        this.value = value;
    }

    /**
     * The transaction has completed successfully. The participant previously informed the coordinator that it was ready to
     * complete.
     *
     * @throws WrongStateException never in this implementation.
     * @throws SystemException never in this implementation.
     */

    public void close() throws WrongStateException, SystemException {
        // nothing to do here as the item has already been added to the set
        System.out
            .println("[SERVICE] Participant.close (The participant knows that this BA is now finished and can throw away any temporary state)");
    }

    /**
     * The transaction has canceled, and the participant should undo any work. The participant cannot have informed the
     * coordinator that it has completed.
     *
     * @throws WrongStateException never in this implementation.
     * @throws SystemException never in this implementation.
     */

    public void cancel() throws WrongStateException, SystemException {
        System.out.println("[SERVICE] Participant.cancel (The participant should compensate any work done within this BA)");

        // Compensate work
        MockSetManager.rollback(value);
    }

    /**
     * The transaction has cancelled. The participant previously informed the coordinator that it had finished work but could
     * compensate later if required, and it is now requested to do so.
     *
     * @throws WrongStateException never in this implementation.
     * @throws SystemException if unable to perform the compensating transaction.
     */

    public void compensate() throws FaultedException, WrongStateException, SystemException {
        System.out.println("[SERVICE] Participant.compensate");

        // Compensate work done by the service
        MockSetManager.rollback(value);
    }

    public String status() {
        return null;
    }

    public void unknown() throws SystemException {
    }

    public void error() throws SystemException {
        System.out.println("[SERVICE] Participant.error");

        // Compensate work done by the service
        MockSetManager.rollback(value);
    }

    /**
     * method called to perform commit or rollback of prepared changes to the underlying manager state after the participant
     * recovery record has been written
     *
     * @param confirmed true if the log record has been written and changes should be rolled forward and false if it has not
     *        been written and changes should be rolled back
     */
    public void confirmCompleted(boolean confirmed) {
        if (confirmed) {
            System.out
                .println("[SERVICE] Participant.confirmCompleted('"
                    + confirmed
                    + "') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)");
            MockSetManager.commit();
        } else {
            MockSetManager.rollback(value);
        }
    }
}
