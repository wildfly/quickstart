package org.jboss.as.quickstarts.tasks.beans;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * Provider functionality of {@link Repository} using extended persistence context.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateful
@RequestScoped
@Local(Repository.class)
public class RepositoryBean implements Repository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}