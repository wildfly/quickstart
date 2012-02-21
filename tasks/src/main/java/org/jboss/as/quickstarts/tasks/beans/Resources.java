package org.jboss.as.quickstarts.tasks.beans;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
public class Resources {

   @PersistenceContext(type=PersistenceContextType.EXTENDED)
   private EntityManager em;
   
   @Produces
   public EntityManager getEm() {
      return em;
   }
   
}
