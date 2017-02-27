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
package org.jboss.as.quickstarts.wsat.simple.jaxws;

import org.jboss.as.quickstarts.wsat.simple.RestaurantException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Interface to a simple Restaurant. Provides simple methods to manipulate bookings.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 */
@WebService(name = "RestaurantServiceAT", targetNamespace = "http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RestaurantServiceAT {

    /**
     * Create a new booking
     */
    @WebMethod
    void makeBooking() throws RestaurantException;

    /**
     * obtain the number of existing bookings
     *
     * @return the number of current bookings
     */
    @WebMethod
    int getBookingCount();

    /**
     * Reset the booking count to zero
     */
    @WebMethod
    void reset();

}
