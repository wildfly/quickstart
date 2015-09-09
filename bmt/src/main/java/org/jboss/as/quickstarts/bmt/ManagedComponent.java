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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;

/**
 * A session bean for updating a database table within a JTA transaction
 *
 * @author Mike Musgrove
 */

/*
 * Mark the component as managed by the container. In the context of the example this has 3 consequences: - it becomes eligible
 * for injection into other components (eg the {@linkplain TransactionServlet}): - it becomes eligible for other components to
 * be injected; - it becomes eligible for Container Managed Transactions (although this example does not use CMT)
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
// tell the container not to manage transactions
public class ManagedComponent {
    /**
     * Ask the container to inject an Entity Manager (EM). As a consequence the EM will be automatically enlisted into any new
     * transactions started by the managed component.
     *
     */
    @PersistenceContext
    private EntityManager entityManager;

    // Inject a UserTransaction for manual transaction demarcation.
    @Inject
    private UserTransaction userTransaction;

    // Inject a utility class for updating JPA entities
    @Inject
    private UnManagedComponent helper;

    /**
     * Maintain a simple key value store using JPA. The method uses a Container managed Entity Manager with manual transaction
     * demarcation.
     *
     * @param key the key. If the key does not exist then a new key/value pair is entered into the database. If the key already
     *        exists then the associated value is updated.
     * @param value the value
     *
     * @return a string representing the keys values pairs if no key is provided, or the key value pair if one is provided, or
     *         the error if anything went wrong
     */
    public String updateKeyValueDatabase(String key, String value) {
        /*
         * Since this is a session bean method we are guaranteed to be thread safe so it is OK to use the injected Entity
         * Manager. Contrast this with UnManagedComponent class where the developer must create an EM for the duration of the
         * method call
         */
        try {
            userTransaction.begin();

            /*
             * Since the bean is managed by the container the Entity Manager (EM) and JTA transaction manager (TM) cooperate so
             * there is no need to tell the EM about the transaction. Compare this with the UnManagedComponent class where the
             * developer is managing the EM himself and therefore must explicitly tell the EM to join the transaction
             */
            String result = helper.updateKeyValueDatabase(entityManager, key, value);

            userTransaction.commit();

            return result;
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            /*
             * Clean up
             */
            try {
                if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
                    userTransaction.rollback();
            } catch (Throwable e) {
                // ignore
            }
        }
    }
}
