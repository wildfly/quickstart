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

import com.arjuna.mw.wst11.BusinessActivityManager;
import com.arjuna.mw.wst11.BusinessActivityManagerFactory;
import com.arjuna.wst11.BAParticipantManager;
import org.jboss.as.quickstarts.wsba.participantcompletion.simple.jaxws.SetServiceBA;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.annotation.WebServlet;
import java.util.UUID;

/**
 * An adapter class that exposes a set as a transactional Web Service.
 *
 * @author Paul Robinson (paul.robinson@redhat.com)
 */
@WebService(serviceName = "SetServiceBAService", portName = "SetServiceBA", name = "SetServiceBA",
    targetNamespace = "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/helloworld/wsba/participantcompletion/set")
@HandlerChain(file = "/context-handlers.xml", name = "Context Handlers")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebServlet("/SetServiceBA")
public class SetServiceBAImpl implements SetServiceBA {
    /**
     * Add an item to a set Enrolls a Participant if necessary and passes the call through to the business logic.
     *
     * @param value the value to add to the set.
     * @throws AlreadyInSetException if value is already in the set
     * @throws SetServiceException if an error occurred when attempting to add the item to the set.
     */
    @WebMethod
    public void addValueToSet(String value) throws AlreadyInSetException, SetServiceException {

        System.out.println("[SERVICE] invoked addValueToSet('" + value + "')");

        BAParticipantManager participantManager;

        try {
            // enlist the Participant for this service:
            SetParticipantBA participant = new SetParticipantBA(value);
            BusinessActivityManager activityManager = BusinessActivityManagerFactory.businessActivityManager();
            System.out.println("[SERVICE] Enlisting a participant into the BA");
            participantManager = activityManager.enlistForBusinessAgreementWithParticipantCompletion(participant,
                "SetServiceBAImpl:" + UUID.randomUUID());
        } catch (Exception e) {
            System.err.println("Participant enlistment failed");
            e.printStackTrace(System.err);
            throw new SetServiceException("Error enlisting participant", e);
        }

        // invoke the back-end business logic
        System.out.println("[SERVICE] Invoking the back-end business logic");
        MockSetManager.add(value);

        /*
         * this service employs the participant completion protocol which means it decides when it wants to commit local
         * changes. If the local changes (adding the item to the set) succeeded, we notify the coordinator that we have
         * completed. Otherwise, we notify the coordinator that we cannot complete. If any other participant fails or the client
         * decides to cancel we can rely upon being told to compensate.
         */
        System.out
            .println("[SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work");
        if (MockSetManager.prepare()) {
            try {
                // tell the coordinator manager we have finished our work
                System.out.println("[SERVICE] Prepare successful, notifying coordinator of completion");
                participantManager.completed();
            } catch (Exception e) {
                /*
                 * Failed to notify the coordinator that we have finished our work. Compensate the work and throw an Exception
                 * to notify the client that the add operation failed.
                 */
                MockSetManager.rollback(value);

                System.err.println("[SERVICE]  'completed' callback failed");
                throw new SetServiceException("Error when notifying the coordinator that the work is completed", e);
            }
        } else {
            try {
                /*
                 * tell the participant manager we cannot complete. this will force the activity to fail
                 */
                System.out.println("[SERVICE] Prepare failed, notifying coordinator that we cannot complete");
                participantManager.cannotComplete();
            } catch (Exception e) {
                System.err.println("'cannotComplete' callback failed");
                throw new SetServiceException("Error when notifying the coordinator that the work is cannot be completed", e);
            }
            throw new SetServiceException("Unable to prepare the back-end resource");
        }

    }

    /**
     * Query the set to see if it contains a particular value.
     *
     * @param value the value to check for.
     * @return true if the value was present, false otherwise.
     */
    @WebMethod
    public boolean isInSet(String value) {
        return MockSetManager.isInSet(value);
    }

    /**
     * Empty the set
     * <p/>
     * Note: To simplify this example, this method is not part of the compensation logic, so will not be undone if the BA is
     * compensated. It can also be invoked outside of an active BA.
     */
    @WebMethod
    public void clear() {
        MockSetManager.clear();
    }
}
