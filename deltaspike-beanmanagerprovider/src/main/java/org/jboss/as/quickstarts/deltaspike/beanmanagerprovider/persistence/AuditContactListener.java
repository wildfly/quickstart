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
