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

import com.arjuna.mw.wst11.TransactionManager;
import com.arjuna.mw.wst11.TransactionManagerFactory;
import com.arjuna.mw.wst11.UserTransactionFactory;

import org.jboss.as.quickstarts.wsat.simple.jaxws.RestaurantServiceAT;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.annotation.WebServlet;
import java.util.UUID;

/**
 * An adapter class that exposes the RestaurantManager business API as a transactional Web Service.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 *
 */
@WebService(serviceName = "RestaurantServiceATService", portName = "RestaurantServiceAT", name = "RestaurantServiceAT",
    targetNamespace = "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant")
@HandlerChain(file = "/context-handlers.xml", name = "Context Handlers")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebServlet("/RestaurantServiceAT")
public class RestaurantServiceATImpl implements RestaurantServiceAT {

    private MockRestaurantManager mockRestaurantManager = MockRestaurantManager.getSingletonInstance();

    /**
     * Book a number of seats in the restaurant. Enrols a Participant, then passes the call through to the business logic.
     */
    @WebMethod
    public void makeBooking() throws RestaurantException {

        System.out.println("[SERVICE] Restaurant service invoked to make a booking");
        String transactionId;
        try {
            // get the transaction ID associated with this thread
            transactionId = UserTransactionFactory.userTransaction().toString();

            // enlist the Participant for this service:
            RestaurantParticipant restaurantParticipant = new RestaurantParticipant(transactionId);
            TransactionManager transactionManager = TransactionManagerFactory.transactionManager();
            System.out.println("[SERVICE] Enlisting a Durable2PC participant into the AT");
            transactionManager.enlistForDurableTwoPhase(restaurantParticipant, "restaurantServiceAT:" + UUID.randomUUID());
        } catch (Exception e) {
            throw new RestaurantException("Error when enlisting participant", e);
        }

        // invoke the backend business logic:
        System.out.println("[SERVICE] Invoking the back-end business logic");
        mockRestaurantManager.makeBooking(transactionId);
    }

    /**
     * obtain the number of existing bookings
     *
     * @return the number of current bookings
     */
    @Override
    public int getBookingCount() {
        return mockRestaurantManager.getBookingCount();
    }

    /**
     * Reset the booking count to zero
     *
     * Note: To simplify this example, this method is not part of the compensation logic, so will not be undone if the AT is
     * compensated. It can also be invoked outside of an active AT.
     */
    @Override
    public void reset() {
        mockRestaurantManager.reset();
    }
}
