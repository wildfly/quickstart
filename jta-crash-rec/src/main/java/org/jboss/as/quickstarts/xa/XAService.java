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
    /**
     *  Ask the container to inject a persistence context corresponding to the database that will hold
     *  our key/value pair table.
     *  The unit names corresponds to the one defined in the war archives' persistence.xml file
     */
    @PersistenceContext(unitName = "pctx1")
    EntityManager em;

    // Inject a UserTransaction for manual transaction demarcation (we could have used an EJB and asked the container
    // to manager transactions but we'll manage them ourselves so it's clear what's going on.
    @Inject
    private UserTransaction userTransaction;

    @Resource(mappedName = "java:/JmsXA")
    private XAConnectionFactory xaConnectionFactory; // we want to deliver JMS messages withing an XA transaction

    // use the default JMS queue. Note that messages must be persistent in order for them to survive an AS restart
    // (at the time of writing the following entry in standalone-full.xml <persistence-enabled>true</persistence-enabled>
    // indicates that this is indeed the case).
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    private final static Logger LOGGER = Logger.getLogger(XAService.class.getName());

    /**
     *
     * @param queue
     * @param msg
     * @throws Exception
     */
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

    // must called inside a transaction
    private String listPairs() {
        StringBuilder result = new StringBuilder();

        // list all key value pairs
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
                // insert a new entry into the the key/value table
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
            result.append(e.getCause() != null ? e.getCause().getMessage() :  e.getMessage());
        } finally {
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE || userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
                    userTransaction.rollback();
            } catch (Throwable e) {
                result.append(" Transaction did not finish: ").append(e.getMessage());
            }
        }

        return result.toString();
    }
}
