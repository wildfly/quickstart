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

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;
import javax.jms.XASession;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * A bean for updating a database and sending a JMS message within a single JTA transaction
 *
 * @author Mike Musgrove
 */
public class XAService {

    private static final Logger LOGGER = Logger.getLogger(XAService.class.getName());

    /*
     * Ask the container to inject a persistence context corresponding to the database that will hold our key/value pair table.
     * The unit names corresponds to the one defined in the war archives' persistence.xml file
     */
    @PersistenceContext
    private EntityManager em;

    /*
     * Inject a UserTransaction for manual transaction demarcation (we could have used an EJB and asked the container to manager
     * transactions but we'll manage them ourselves so it's clear what's going on.
     */
    @Inject
    private UserTransaction userTransaction;

    @Resource(mappedName = "java:/JmsXA")
    private XAConnectionFactory xaConnectionFactory; // we want to deliver JMS messages withing an XA transaction

    // use our JMS queue. Note that messages must be persistent in order for them to survive an AS restart
    @Resource(mappedName = "java:/queue/jta-crash-rec-quickstart")
    private Queue queue;

    private void notifyUpdate(Queue queue, String msg) throws Exception {
        XAConnection connection = null;

        try {
            connection = xaConnectionFactory.createXAConnection();
            XASession session = connection.createXASession();
            MessageProducer messageProducer = session.createProducer(queue);

            connection.start();
            TextMessage message = session.createTextMessage();
            message.setText(msg);

            messageProducer.send(message);
            messageProducer.close();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    LOGGER.info("Error closing JMS connection: " + e.getMessage());
                }
            }
        }
    }

    // must be called inside a transaction
    private String listPairs() {
        StringBuilder result = new StringBuilder();

        // list all key value pairs
        @SuppressWarnings("unchecked")
        final List<KVPair> list = em.createQuery("select k from KVPair k").getResultList();

        result.append("<table><caption>Database Table Contents</caption><tr><th>Key</th><th>Value</th></tr>");

        for (KVPair kvPair : list) {
            result.append("<tr><td>");
            result.append(kvPair.getKey());
            result.append("</td><td>");
            result.append(kvPair.getValue());
            result.append("</td></tr>");
        }
        result.append("</table>");

        return result.toString();
    }

    /**
     * Update a key value database. The method must be called within a transaction.
     *
     * @param entityManager an open JPA entity manager
     * @param delete if true then delete rows. If key is empty all rows are deleted.
     * @param key if not null then a pair is inserted into the database
     * @param value the value to be associated with the key
     *
     * @return true if a key was inserted or a value modified
     */
    public boolean modifyKeyValueTable(EntityManager entityManager, boolean delete, String key, String value) {
        boolean keyIsValid = (key != null && key.length() != 0);

        if (delete) {
            if (keyIsValid) {
                KVPair pair = entityManager.find(KVPair.class, key);

                // delete the requested key
                if (pair != null)
                    entityManager.remove(pair);
            } else {
                // delete all entities
                Query query = entityManager.createQuery("DELETE FROM KVPair k");

                query.executeUpdate();
            }
        } else if (keyIsValid) {
            KVPair pair = entityManager.find(KVPair.class, key);

            if (pair == null) {
                // insert a new entry into the key/value table
                entityManager.persist(new KVPair(key, value));
            } else {
                // there is already a value for this key - update it with the new value
                pair.setValue(value);
                entityManager.merge(pair);
            }

            return true;
        }

        return false;
    }

    public void modifyKeyValueTable(boolean delete, String key, String value) {
        modifyKeyValueTable(em, delete, key, value);
    }

    /**
     * Update a key value database. The method must not be called within a transaction.
     *
     * @param delete if true then delete rows. If key is empty all rows are deleted.
     * @param key if not null then a pair is inserted into the database
     * @param value the value to be associated with the key
     *
     * @return The contents of the table after the update
     */
    public String updateKeyValueDatabase(boolean delete, String key, String value) {
        StringBuilder result = new StringBuilder();

        try {
            userTransaction.begin();

            // a row was inserted or modified notify the message consumer
            if (modifyKeyValueTable(em, delete, key, value))
                notifyUpdate(queue, key + "=" + value);

            userTransaction.commit();

            userTransaction.begin();

            result.append(listPairs());

            userTransaction.commit();
        } catch (Exception e) {
            result.append(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        } finally {
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE
                    || userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                    userTransaction.rollback();
            } catch (Throwable e) {
                result.append(" Transaction did not finish: ").append(e.getMessage());
            }
        }
        return result.toString();
    }
}
