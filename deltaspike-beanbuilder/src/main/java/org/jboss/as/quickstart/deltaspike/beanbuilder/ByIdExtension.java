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

package org.jboss.as.quickstart.deltaspike.beanbuilder;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;
import org.jboss.as.quickstart.deltaspike.beanbuilder.model.Person;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ByIdExtension implements Extension {

    // All nicks that needs to be found
    private List<String> ids = new LinkedList<String>();

    /**
     * This method is fired for every component class supporting injection that may be instantiated by the container at runtime.
     * 
     * It will look for all injection target and collect all nicks that were used and needs to be found on {@link EntityManager}
     * 
     * @param pit
     */
    public <X extends Object> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
        for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
            ById idValue = ip.getAnnotated().getAnnotation(ById.class);
            if (idValue != null) {
                // Store the nick value
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
            // Create a Bean using the Nick Qualifier with the right id value and a new contextualLifecycle 
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



    /**
     * This class represents the {@link ById} annotation with its value. It is used by the {@link BeanBuilder} to set the
     * {@link Bean} {@link Qualifier}
     * 
     */
    public static class ByIdLiteral extends AnnotationLiteral<ById> implements ById {

        private static final long serialVersionUID = 1L;

        private String value;

        public ByIdLiteral(String v) {
            this.value = v;
        }

        @Override
        public String value() {
            return value;
        }

    }

    /**
     * This is the {@link ContextualLifecycle} that is used to by the {@link Bean} to create instances of {@link Person}.
     * 
     * It uses the {@link EntityManager#find(Class, Object)} to query the {@link Person} from Database
     * 
     */
    public static class PersonContextualLifecycle implements ContextualLifecycle<Person> {

        private String idValue;

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

}
