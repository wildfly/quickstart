package org.jboss.as.quickstarts.greeter;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class Resources {

   // Expose an entity manager using the resource producer pattern
   @SuppressWarnings("unused")
   @PersistenceContext
   @Produces
   private EntityManager em;

   @Produces
   Logger getLogger(InjectionPoint ip) {
      String category = ip.getMember().getDeclaringClass().getName();
      return Logger.getLogger(category);
   }

   @Produces
   FacesContext getFacesContext() {
      return FacesContext.getCurrentInstance();
   }
   
}
