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

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.jboss.as.quickstart.deltaspike.beanbuilder.model.Person;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
public class ByIdExtension implements Extension {

    // All nicks that needs to be found
    private final List<String> ids = new LinkedList<>();

    /**
     * This method is fired for every component class supporting injection that may be instantiated by the container at runtime.
     *
     * It will look for all injection target and collect all nicks that were used and needs to be found on {@link EntityManager}
     *
     * @param pit
     */
    public void processInjectionTarget(@Observes ProcessInjectionTarget<?> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            ById idValue = ip.getAnnotated().getAnnotation(ById.class);
            if (idValue != null) {
                // Store the value
                ids.add(idValue.value());
            }
        }
    }

    /**
     * This method is fired when CDI has fully completed the bean discovery process
     *
     */
    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        for (final String idValue : ids) {
            // Create a Bean using the ById Qualifier with the right nick value and a new contextualLifecycle
            // We naively assume we can list all entities in the app here!
            BeanBuilder<Person> beanBuilder = new BeanBuilder<Person>(bm)
                .beanClass(Person.class)
                .types(Person.class, Object.class)
                // The qualifier with its value
                .qualifiers(new ByIdLiteral(idValue))
                // Create a ContextualLifecyle for each id found
                .beanLifecycle(new PersonContextualLifecycle(idValue));
            // Create and add the Bean
            abd.addBean(beanBuilder.create());
        }
    }

}
