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
package org.jboss.as.quickstarts.xa;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

/**
 * <p>
 * A Message Driven Bean that asynchronously receives and processes
 * messages of the form key=value and updates a database table with
 * those values using the services of an injected bean.
 * </p>
 *
 * @author Serge Pagop (spagop@redhat.com)
 * @author Mike Musgrove (mmusgrov@redhat.com)
 *
 */
@MessageDriven(name = "DbUpdaterMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/jta-crash-rec-quickstart"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class DbUpdaterMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(DbUpdaterMDB.class.getName());

    // A helper service for persisting key value pairs
    @Inject
    private XAService xaService;

    public void onMessage(Message rcvMessage) {
        LOGGER.info("Received Message: " + rcvMessage.toString());

        try {
            if (rcvMessage instanceof TextMessage) {
                TextMessage msg = (TextMessage) rcvMessage;
                String sm = msg.getText();
                String[] kvPair = sm.split("=");

                // only process messages of the form key=value
                if (kvPair.length == 2 && kvPair[0].length() != 0) {
                    xaService.modifyKeyValueTable(false, kvPair[0], kvPair[1] + " updated via JMS");
                    LOGGER.info("JTA Crash Record Quickstart: key value pair updated via JMS");
                }
            } else {
                LOGGER.warning("JTA Crash Record Quickstart: Unexpected message. Type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
