package org.jboss.as.quickstarts.tasks.beans;

import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * <p>
 * Repository for entities acts as mediator between DAO objects and persistence layer.
 * </p>
 *
 * <p>
 * Provides basic operations for operations on current persistence context.
 * </p>
 *
 * @author Lukas Fryc
 *
 */
public interface Repository {

    void create(Serializable entity);

    void delete(Serializable entity);

    <T> T retrieveById(Class<T> type, Long id);

    <T extends Serializable> TypedQuery<T> query(Class<T> type, String query, Serializable... params);

    void update(Serializable entity);

    void saveNonManaged(Serializable entity);

    boolean isManaging(Serializable entity);

    <T extends Serializable> T attach(T entity);

    public <T extends Serializable> T attachAndRefresh(T entity);

    void close();
}