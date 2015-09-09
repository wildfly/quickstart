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
package org.jboss.as.quickstart.deltaspike.beanbuilder;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.jboss.as.quickstart.deltaspike.beanbuilder.model.Person;

/**
 * This is the {@link ContextualLifecycle} that is used to by the {@link Bean} to create instances of {@link Person}.
 *
 * It uses the {@link EntityManager#find(Class, Object)} to query the {@link Person} from Database
 *
 */
public class PersonContextualLifecycle implements ContextualLifecycle<Person> {

    private final String idValue;

    public PersonContextualLifecycle(String idValue) {
        this.idValue = idValue;
    }

    @Override
    public void destroy(Bean<Person> bean, Person instance, CreationalContext<Person> creationalContext) {
        creationalContext.release();
    }

    @Override
    public Person create(Bean<Person> bean, CreationalContext<Person> creationalContext) {
        // Here we use the entityManager to get the Person Instance
        EntityManager em = BeanProvider.getContextualReference(EntityManager.class);
        return em.find(Person.class, idValue);
    }
}
