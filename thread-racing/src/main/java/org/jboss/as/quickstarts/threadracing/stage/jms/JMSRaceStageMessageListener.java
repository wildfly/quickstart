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
package org.jboss.as.quickstarts.threadracing.stage.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * The JMS race stage {@link javax.jms.MessageListener}, which simply returns back the received message's text.
 *
 * This class, being a container managed class, creates a JMS destination through annotation {@link javax.jms.JMSDestinationDefinition}.
 *
 * @author Eduardo Martins
 */
@JMSDestinationDefinition(name = JMSRaceStageMessageListener.REQUEST_QUEUE,
    interfaceName = "javax.jms.Queue",
    destinationName = "JMSThreadRacingQueue")
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
        propertyValue = JMSRaceStageMessageListener.REQUEST_QUEUE),
        @ActivationConfigProperty(propertyName = "destinationType",
        propertyValue = "javax.jms.Queue"),
        }
)
public class JMSRaceStageMessageListener implements MessageListener {

    public static final String REQUEST_QUEUE = "java:global/threadRacing/stages/jms/requestQueue";

    @Inject
    private JMSContext jmsContext;

    @Override
    public void onMessage(Message message) {
        // just echo the msg
        try {
            jmsContext.createProducer().send(message.getJMSReplyTo(), ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
