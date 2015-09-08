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
package org.jboss.as.quickstarts.bmt;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

import java.util.List;

/**
 * A class for updating a database table within a JTA transaction. Since the class is only a simple CDI bean the developer is
 * responsible for both controlling the life cycle of the Entity Manager and for transaction demarcation.
 *
 * @author Mike Musgrove
 */
public class UnManagedComponent {
    /*
     * Inject an entity manager factory. The reason we do not inject an entity manager (as we do in ManagedComponent) is that
     * the factory is thread safe whereas the entity manager is not.
     *
     * Specify a persistence unit name (perhaps the application may want to interact with multiple databases).
     */
    @PersistenceUnit(unitName = "primary")
    private EntityManagerFactory entityManagerFactory;

    /*
     * Inject a UserTransaction for manual transaction demarcation (this object is thread safe)
     */
    @Inject
    private UserTransaction userTransaction;

    public String updateKeyValueDatabase(String key, String value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            userTransaction.begin();

            /*
             * Since the Entity Manager (EM) is not managed by the container the developer must explicitly tell the EM to join
             * the transaction. Compare this with ManagedComponent where the container automatically enlists the EM with the
             * transaction. The persistence context managed by the EM will then be scoped to the JTA transaction which means
             * that all entities will be detached when the transaction commits.
             */
            entityManager.joinTransaction();

            // make some transactional changes
            String result = updateKeyValueDatabase(entityManager, key, value);

            /*
             * Note that the default scope of entities managed by the EM is transaction. Thus once the transaction commits the
             * entity will be detached from the EM. See also the comment in the finally block below.
             */
            userTransaction.commit();

            return result;
        } catch (RollbackException e) {
            // We tried to commit the transaction but it has already been rolled back (adding duplicate keys would
            // generate this condition although the example does check for duplicates).
            Throwable t = e.getCause();

            return t != null ? t.getMessage() : e.getMessage();
        } catch (Exception e) {
            /*
             * An application cannot handle any of the other exceptions raised by begin and commit so we just catch the generic
             * exception. The meaning of the other exceptions is:
             *
             * NotSupportedException - the thread is already associated with a transaction HeuristicRollbackException - should
             * not happen since the example is interacting with a single database HeuristicMixedException - should not happen
             * since the example is interacting with a single database SystemException - the TM raised an unexpected error.
             * There is no standard way of handling this error (another reason why CMT are preferable to managing them
             * ourselves)
             */
            return e.getMessage();
        } finally {
            /*
             * Since the EM is transaction scoped it will not detach its objects even when calling close on it until the
             * transaction completes. Therefore we must roll back any active transaction before returning.
             */
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
                    userTransaction.rollback();
            } catch (Throwable e) {
                // ignore
            }

            entityManager.close();
        }
    }

    /**
     * Utility method for updating a key value database.
     *
     * @param entityManager an open JPA entity manager
     * @param key if null or zero length then list all pairs
     * @param value if key exists then associate value with it, otherwise create a new pair
     * @return the new value of the key value pair or all pairs if key was null (or zero length).
     */
    public String updateKeyValueDatabase(EntityManager entityManager, String key, String value) {
        StringBuilder sb = new StringBuilder();

        if (key == null || key.length() == 0) {
            // list all key value pairs
            @SuppressWarnings("unchecked")
            final List<KVPair> list = entityManager.createQuery("select k from KVPair k").getResultList();

            for (KVPair kvPair : list)
                sb.append(kvPair.getKey()).append("=").append(kvPair.getValue()).append(',');

        } else {
            KVPair kvPair;

            if (value == null) {
                // retrieve the value associated with key
                kvPair = new KVPair(key, value);

                entityManager.refresh(kvPair);
            } else {
                kvPair = entityManager.find(KVPair.class, key);

                if (kvPair == null) {
                    // insert into the key/value table
                    kvPair = new KVPair(key, value);
                    entityManager.persist(kvPair);
                } else {
                    // update an existing row in the key/value table
                    kvPair.setValue(value);
                    entityManager.persist(kvPair);
                }
            }

            sb.append(kvPair.getKey()).append("=").append(kvPair.getValue());
        }

        return sb.toString();
    }
}
