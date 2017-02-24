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

import org.jboss.as.quickstarts.threadracing.Race;
import org.jboss.as.quickstarts.threadracing.stage.RaceStage;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
import java.util.UUID;

/**
 * The JMS race stage is a JMS client which sends a request containing a text message, and waits for a response with same text, using the request/response trough a temporary queue pattern.
 *
 * @author Eduardo Martins
 */
public class JMSRaceStage implements RaceStage {

    /**
     * injection of JMS message listener's queue, through JNDI lookup
     */
    @Resource(lookup = JMSRaceStageMessageListener.REQUEST_QUEUE)
    private Queue requestQueue;
    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private ConnectionFactory cf;

    @Override
    public void run(Race.Registration registration) throws Exception {
        try (JMSContext jmsContext = cf.createContext()) {
            // create response tmp queue
            final TemporaryQueue responseQueue = jmsContext.createTemporaryQueue();
            // send request
            final String request = UUID.randomUUID().toString();
            jmsContext.createProducer()
                    .setJMSReplyTo(responseQueue)
                    .send(requestQueue, request);
            // receive response
            try (JMSConsumer consumer = jmsContext.createConsumer(responseQueue)) {
                String response = consumer.receiveBody(String.class);
                if (response == null) {
                    registration.aborted(new IllegalStateException("Message processing timed out"));
                } else if (!response.equals(request)) {
                    registration.aborted(new IllegalStateException("Response content does not match the request. Response: " + response + ", request: " + request));
                }
            }
        }
    }
}

