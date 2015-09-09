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
package org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.arjuna.wst.BusinessAgreementWithCoordinatorCompletionParticipant;
import com.arjuna.wst.FaultedException;
import com.arjuna.wst.SystemException;
import com.arjuna.wst.WrongStateException;
import com.arjuna.wst11.ConfirmCompletedParticipant;

/**
 * An adapter class that exposes the SetManager as a WS-BA participant using the 'Coordinator Completion' protocol.
 * <p/>
 * The Set Service can be invoked multiple times to add many items to the set within a single BA. The service waits for the
 * coordinator to tell it to complete. This has the advantage that the client can continue calling methods on the service right
 * up until it calls 'close'. However, any resources held by the service need to be held for this duration, unless the service
 * decides to autonomously cancel the BA.
 *
 * @author Paul Robinson (paul.robinson@redhat.com)
 */
public class SetParticipantBA implements BusinessAgreementWithCoordinatorCompletionParticipant, ConfirmCompletedParticipant,
    Serializable {
    private static final long serialVersionUID = 1L;
    // The ID of the corresponding transaction
    private String txID;
    // A list of values added to the set. These are removed from the set at
    // compensation time.
    private List<String> values = new LinkedList<>();
    // table of currently active participants
    private static Map<String, SetParticipantBA> participants = new HashMap<>();

    /**
     * Participant instances are related to business method calls in a one to one manner.
     *
     * @param txID The ID of the current Business Activity
     * @param value the value to remove from the set during compensation
     */
    public SetParticipantBA(String txID, String value) {
        this.txID = txID;
        addValue(value);
    }

    /**
     * Notify the participant that another value is being added to the set. This is stored in case compensation is required.
     *
     * @param value the value being added to the set
     */
    public void addValue(String value) {
        values.add(value);
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
        removeParticipant(txID);
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
        doCompensate();
        removeParticipant(txID);
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
        doCompensate();
        removeParticipant(txID);
    }

    public String status() {
        return null;
    }

    public void unknown() throws SystemException {
        removeParticipant(txID);
    }

    public void error() throws SystemException {
        System.out.println("[SERVICE] Participant.error");
        doCompensate();
        removeParticipant(txID);
    }

    private void doCompensate() {
        System.out.println("[SERVICE] SetParticipantBA: Carrying out compensation action");
        for (String value : values) {
            MockSetManager.rollback(value);
        }
    }

    @Override
    public void complete() throws WrongStateException, SystemException {
        System.out
            .println("[SERVICE] Participant.complete (This tells the participant that the BA completed, but may be compensated later)");
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
            doCompensate();
        }
    }

    /************************************************************************/
    /* tracking active participants */
    /************************************************************************/
    /**
     * keep track of a participant
     *
     * @param txID the participant's transaction id
     * @param participant The participant associated with this BA
     */
    public static synchronized void recordParticipant(String txID, SetParticipantBA participant) {
        participants.put(txID, participant);
    }

    /**
     * forget about a participant
     *
     * @param txID the participant's transaction id
     */
    public static void removeParticipant(String txID) {
        participants.remove(txID);
    }

    /**
     * lookup a participant
     *
     * @param txID the participant's transaction id
     * @return the participant
     */
    public static synchronized SetParticipantBA getParticipant(String txID) {
        return participants.get(txID);
    }
}
