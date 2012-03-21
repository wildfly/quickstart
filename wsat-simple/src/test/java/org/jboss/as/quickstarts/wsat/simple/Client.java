/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
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
        QName serviceName = new QName("http://www.jboss.com/jbossas/quickstarts/wsat/simple/Restaurant",
                "RestaurantServiceATService");
        QName portName = new QName("http://www.jboss.com/jbossas/quickstarts/wsat/simple/Restaurant", "RestaurantServiceAT");

        Service service = Service.create(wsdlLocation, serviceName);
        restaurant = service.getPort(portName, RestaurantServiceAT.class);

        /*
         * Add client handler chain
         */
        BindingProvider bindingProvider = (BindingProvider) restaurant;
        List<Handler> handlers = new ArrayList<Handler>(1);
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
