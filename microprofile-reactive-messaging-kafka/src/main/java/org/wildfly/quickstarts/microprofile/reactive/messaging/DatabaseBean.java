package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */

@ApplicationScoped
public class DatabaseBean {

    @PersistenceContext(unitName = "test")
    EntityManager em;

    @Transactional
    public void store(Object entry) {
        em.persist(entry);
    }

    public List<TimedEntry> loadAllTimedEntries() {
        TypedQuery<TimedEntry> query = em.createQuery("SELECT t from TimedEntry t", TimedEntry.class);
        List<TimedEntry> result = query.getResultList();
        return result;
    }
}