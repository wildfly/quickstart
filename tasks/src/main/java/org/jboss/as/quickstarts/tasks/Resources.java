package org.jboss.as.quickstarts.tasks;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans. As it is a stateful bean, it
 * can produce extended persistence contexts.
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
@Stateful
@RequestScoped
public class Resources {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Produces
    public EntityManager getEm() {
        return em;
    }

}
