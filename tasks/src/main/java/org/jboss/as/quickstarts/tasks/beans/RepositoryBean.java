package org.jboss.as.quickstarts.tasks.beans;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * Provider functionality of {@link Repository} using extended persistence context.
 *
 * @author Lukas Fryc
 *
 */
@Stateful
@RequestScoped
@Local(Repository.class)
public class RepositoryBean implements Repository {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public void create(final Serializable entity) {
        em.persist(entity);
    }

    @Override
    public void delete(final Serializable entity) {
        em.remove(entity);
    }

    @Override
    public <T> T retrieveById(final Class<T> type, final Long id) {
        return (T) em.find(type, id);
    }

    @Override
    public <T extends Serializable> TypedQuery<T> query(Class<T> type, String query, Serializable... params) {
        TypedQuery<T> q = em.createQuery(query, type);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        return q;
    }

    @Override
    public void saveNonManaged(final Serializable entity) {
        em.merge(entity);
    }

    @Override
    public boolean isManaging(final Serializable entity) {
        return em.contains(entity);
    }

    @Override
    @Remove
    public void close() {
        em.flush();
        em.clear();
        em.close();
    }

    @Override
    public void update(Serializable entity) {
        // any transaction in which the entity manager is enlisted will flush the changes
    }

    @Override
    public <T extends Serializable> T attach(T entity) {
        if (em.contains(entity)) {
            return entity;
        } else {
            entity = em.merge(entity);
            return entity;
        }
    }

    @Override
    public <T extends Serializable> T attachAndRefresh(T entity) {
        if (em.contains(entity)) {
            return entity;
        } else {
            entity = em.merge(entity);
            em.refresh(entity);
            return entity;
        }
    }
}