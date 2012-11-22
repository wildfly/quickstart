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

import java.lang.annotation.Annotation;

import javax.ejb.Stateless;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.AuditContact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.Contact;
import org.jboss.as.quickstarts.deltaspike.beanmanagerprovider.model.OPERATION;

/**
 * This Listener is not managed by CDI lifecycle so we cannot inject {@link AuditRepository}.
 * 
 * In this case, we use {@link BeanManagerProvider} to access a {@link AuditRepository} instance
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class AuditContactListener {

    // Injection does not work because this class is not managed by CDI
    // NOTE: JPA 2.1 will support CDI Injection in EntityListener - in Java EE 7
    private AuditRepository auditRepository;

    /**
     * This methos gets the {@link BeanManager} from the {@link BeanManagerProvider} and uses it to get a instance of
     * {@link AuditRepository} {@link Stateless} EJB
     * 
     * @return EJB reference
     */
    @SuppressWarnings({ "rawtypes" })
    public AuditRepository getAuditRepositoryInstance() {
        BeanManager bm = BeanManagerProvider.getInstance().getBeanManager();
        Bean<?> bean = bm.resolve(bm.getBeans(AuditRepository.class, new Annotation[] {}));
        CreationalContext cc = bm.createCreationalContext(bean);
        return (AuditRepository) bm.getReference(bean, AuditRepository.class, cc);
    }

    /**
     * Method called after {@link Contact} is persisted
     * 
     * @param contact
     */
    @PostPersist
    public void created(Contact contact) {
        auditRepository = getAuditRepositoryInstance();
        AuditContact a = new AuditContact();
        a.setContactEmail(contact.getEmail());
        a.setContactName(contact.getName());
        a.setContactPhone(contact.getPhoneNumber());
        a.setOperation(OPERATION.INSERT);
        auditRepository.persist(a);
    }

    /**
     * Method called after {@link Contact} is updated
     * 
     * @param contact
     */
    @PostUpdate
    public void updated(Contact contact) {
        auditRepository = getAuditRepositoryInstance();
        AuditContact a = new AuditContact();
        a.setContactEmail(contact.getEmail());
        a.setContactName(contact.getName());
        a.setContactPhone(contact.getPhoneNumber());
        a.setOperation(OPERATION.UPDATE);
        auditRepository.persist(a);
    }

    /**
     * Method called after {@link Contact} is removed
     * 
     * @param contact
     */

    @PostRemove
    public void removed(Contact contact) {
        auditRepository = getAuditRepositoryInstance();
        AuditContact a = new AuditContact();
        a.setContactEmail(contact.getEmail());
        a.setContactName(contact.getName());
        a.setContactPhone(contact.getPhoneNumber());
        a.setOperation(OPERATION.DELETE);
        auditRepository.persist(a);
    }
}
