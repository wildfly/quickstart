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
package org.jboss.as.quickstarts.payment.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.qualifiers.Credit;
import org.jboss.as.quickstarts.payment.qualifiers.Debit;

@SessionScoped
public class PaymentHandler implements Serializable, ICreditEventObserver, IDebitEventObserver {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private Logger logger;

    List<PaymentEvent> payments = new ArrayList<>();

    @Produces
    @Named
    public List<PaymentEvent> getPayments() {
        return payments;
    }

    public void onCreditPaymentEvent(@Observes @Credit PaymentEvent event) {

        logger.info("Processing the credit operation " + event);
        payments.add(event);
    }

    @Override
    public void onDebitPaymentEvent(@Observes @Debit PaymentEvent event) {
        logger.info("Processing the debit operation " + event);
        payments.add(event);

    }

}
