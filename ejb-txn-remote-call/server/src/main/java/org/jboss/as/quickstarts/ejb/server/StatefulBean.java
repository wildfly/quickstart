/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.as.quickstarts.ejb.server;

import org.jboss.as.quickstarts.ejb.entity.CalleeUser;
import org.jboss.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Stateful bean to be called.
 */
@Stateful
@Remote(RemoteBeanInterface.class)
public class StatefulBean implements RemoteBeanInterface {
    private static final Logger log = Logger.getLogger(StatelessBean.class);

    @PersistenceContext
    private EntityManager em;

    /**
     * Stateful bean remote method to be called from the client side.
     *
     * @return information about the host that this EJB resides on
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String successOnCall() {
        log.debugf("Called '%s.successOnCall()' with transaction status %s",
                this.getClass().getName(), InfoUtils.getTransactionStatus());

        em.persist(new CalleeUser("Thorin", "Oakenshield"));

        return InfoUtils.getHostInfo();
    }

    @Override
    public String failOnCall() {
        throw new UnsupportedOperationException("not implemented");
    }
}