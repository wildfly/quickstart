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

import com.arjuna.mw.wst11.client.JaxWSHeaderContextProcessor;

import org.jboss.as.quickstarts.wsat.simple.jaxws.RestaurantServiceAT;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A Client stub to the restaurant service.
 *
 * @author paul.robinson@redhat.com, 2012-01-04
 */
@ClientStub
public class Client implements RestaurantServiceAT {
    private RestaurantServiceAT restaurant;

    /**
     * Default constructor with hard-coded values for the RestaurantServiceAT endpoint details (wsdl url, service name & port
     * name)
     *
     * @throws MalformedURLException if the WSDL url is malformed.
     */
    public Client() throws MalformedURLException {
        URL wsdlLocation = new URL("http://localhost:8080/wsat-simple/RestaurantServiceAT?wsdl");
        QName serviceName = new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant",
            "RestaurantServiceATService");
        QName portName = new QName("http://www.jboss.org/jboss-jdf/jboss-as-quickstart/wsat/simple/Restaurant",
            "RestaurantServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        restaurant = service.getPort(portName, RestaurantServiceAT.class);

        /*
         * Add client handler chain
         */
        BindingProvider bindingProvider = (BindingProvider) restaurant;
        @SuppressWarnings("rawtypes")
        List<Handler> handlers = new ArrayList<>(1);
        handlers.add(new JaxWSHeaderContextProcessor());
        bindingProvider.getBinding().setHandlerChain(handlers);
    }

    /**
     * Create a new booking
     */
    @Override
    public void makeBooking() throws RestaurantException {
        restaurant.makeBooking();
    }

    /**
     * obtain the number of existing bookings
     *
     * @return the number of current bookings
     */
    @Override
    public int getBookingCount() {
        return restaurant.getBookingCount();
    }

    /**
     * Reset the booking count to zero
     */
    @Override
    public void reset() {
        restaurant.reset();
    }
}
