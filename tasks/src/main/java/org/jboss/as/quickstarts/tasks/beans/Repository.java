package org.jboss.as.quickstarts.tasks.beans;

import javax.persistence.EntityManager;

/**
 * <p>
 * Provides persistence context.
 * </p>
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
public interface Repository {

    EntityManager getEntityManager();

}