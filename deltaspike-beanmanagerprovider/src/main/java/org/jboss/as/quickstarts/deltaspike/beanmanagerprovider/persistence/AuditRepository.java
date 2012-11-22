/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
