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
package org.jboss.as.quickstarts.payment.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.payment.events.PaymentEvent;
import org.jboss.as.quickstarts.payment.events.PaymentTypeEnum;
import org.jboss.as.quickstarts.payment.qualifiers.Credit;
import org.jboss.as.quickstarts.payment.qualifiers.Debit;

@Named
@SessionScoped
public class PaymentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    // Events producers
    @Inject
    @Credit
    Event<PaymentEvent> creditEventProducer;

    @Inject
    @Debit
    Event<PaymentEvent> debitEventProducer;

    private BigDecimal amount = new BigDecimal(0);

    private PaymentTypeEnum paymentOption = PaymentTypeEnum.DEBIT;

    @PostConstruct
    private void init() {
        amount = new BigDecimal(0);
        paymentOption = PaymentTypeEnum.DEBIT;
    }

    // Pay Action
    public String pay() {

        PaymentEvent currentEvtPayload = new PaymentEvent();
        currentEvtPayload.setType(paymentOption);
        currentEvtPayload.setAmount(amount);
        currentEvtPayload.setDatetime(new Date());

        switch (currentEvtPayload.getType()) {
            case DEBIT:

                debitEventProducer.fire(currentEvtPayload);

                break;
            case CREDIT:
                creditEventProducer.fire(currentEvtPayload);

                break;

            default:
                log.severe("invalid payment option");
                break;
        }

        // paymentAction

        return "index";
    }

    // Reset Action
    public void reset() {
        init();

    }

    public Event<PaymentEvent> getCreditEventLauncher() {
        return creditEventProducer;
    }

    public void setCreditEventLauncher(Event<PaymentEvent> creditEventLauncher) {
        this.creditEventProducer = creditEventLauncher;
    }

    public Event<PaymentEvent> getDebitEventLauncher() {
        return debitEventProducer;
    }

    public void setDebitEventLauncher(Event<PaymentEvent> debitEventLauncher) {
        this.debitEventProducer = debitEventLauncher;
    }

    public PaymentTypeEnum getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentTypeEnum paymentOption) {
        this.paymentOption = paymentOption;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
