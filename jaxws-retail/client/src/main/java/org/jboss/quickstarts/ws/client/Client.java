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
package org.jboss.quickstarts.ws.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.Customer;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.DiscountRequest;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.DiscountResponse;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.ProfileMgmt;

/**
 * @author rsearls@redhat.com
 */
public class Client {

    public static void main(String[] args) {
        String endPointAddress = "http://localhost:8080/jaxws-retail/ProfileMgmtService/ProfileMgmt";
        QName serviceName = new QName("http://org.jboss.ws/samples/retail/profile", "ProfileMgmtService");

        try {
            URL wsdlURL = new URL(endPointAddress + "?wsdl");
            Service service = Service.create(wsdlURL, serviceName);
            ProfileMgmt proxy = service.getPort(ProfileMgmt.class);

            Customer customer = new Customer();
            customer.setFirstName("Jay");
            customer.setLastName("Boss");
            customer.setCreditCardDetails("newbie");
            DiscountRequest dRequest = new DiscountRequest();
            dRequest.setCustomer(customer);

            DiscountResponse dResponse = proxy.getCustomerDiscount(dRequest);
            Customer responseCustomer = dResponse.getCustomer();
            System.out.format("%s %s\'s discount is %.2f%n", responseCustomer.getFirstName(),
                    responseCustomer.getLastName(), dResponse.getDiscount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
