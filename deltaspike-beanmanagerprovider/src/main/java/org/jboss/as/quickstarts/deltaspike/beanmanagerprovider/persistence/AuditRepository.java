/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.persistence;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.AuditContact;

/**
 * 
 * This class is responsible for dealing with the persistence of {@link AuditContact} entities
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
// This class is {@link Stateless} because there is no need to hold a state
@Stateless
public class AuditRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * The persistence of AuditContact is made on a new transaction since this happens after the Contact entity has been
     * persisted
     * 
     * @param audit
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void persist(AuditContact audit) {
        entityManager.persist(audit);
        entityManager.flush();
    }

    /**
     * Retrieve all AudittRecords from the {@link EntityManager}
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AuditContact> getAllAuditRecords() {
        return entityManager.createQuery("SELECT a FROM AuditContact a").getResultList();
    }
}
