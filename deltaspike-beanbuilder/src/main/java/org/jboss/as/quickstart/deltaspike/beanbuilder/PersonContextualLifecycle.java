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