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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents the back-end resource for managing Restaurant bookings.
 *
 * This is a mock implementation that just keeps a counter of how many bookings have been made.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 */
public class MockRestaurantManager {

    // The singleton instance of this class.
    private static MockRestaurantManager singletonInstance;

    // A thread safe booking counter
    private AtomicInteger bookings = new AtomicInteger(0);

    /**
     * Accessor to obtain the singleton restaurant manager instance.
     *
     * @return the singleton RestaurantManager instance.
     */
    public static synchronized MockRestaurantManager getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new MockRestaurantManager();
        }

        return singletonInstance;
    }

    /**
     * Make a booking with the restaurant.
     *
     * @param txID The transaction identifier
     */
    public synchronized void makeBooking(Object txID) {
        System.out.println("[SERVICE] makeBooking called on backend resource.");
    }

    /**
     * Prepare local state changes for the supplied transaction. This method should persist any required information to ensure
     * that it can undo (rollback) or make permanent (commit) the work done inside this transaction, when told to do so.
     *
     * @param txID The transaction identifier
     * @return true on success, false otherwise
     */
    public boolean prepare(Object txID) {
        System.out.println("[SERVICE] prepare called on backend resource.");
        return true;
    }

    /**
     * commit local state changes for the supplied transaction
     *
     * @param txID The transaction identifier
     */
    public void commit(Object txID) {
        System.out.println("[SERVICE] commit called on backend resource.");
        bookings.getAndIncrement();
    }

    /**
     * roll back local state changes for the supplied transaction
     *
     * @param txID The transaction identifier
     */
    public void rollback(Object txID) {
        System.out.println("[SERVICE] rollback called on backend resource.");
    }

    /**
     * Returns the number of bookings
     *
     * @return the number of bookings.
     */
    public int getBookingCount() {
        return bookings.get();
    }

    /**
     * Reset the booking counter to zero
     */
    public void reset() {
        bookings.set(0);
    }
}
