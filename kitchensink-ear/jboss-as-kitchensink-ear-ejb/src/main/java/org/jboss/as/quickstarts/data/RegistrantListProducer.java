package org.jboss.as.quickstarts.data;

import org.jboss.as.quickstarts.model.Registrant;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class RegistrantListProducer {
   @Inject
   private EntityManager em;

   private List<Registrant> registrants;

   // @Named provides access the return value via the EL variable name "registrants" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Registrant> getRegistrants() {
      return registrants;
   }

   public void onRegistrantListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Registrant registrant) {
      retrieveAllRegistrantsOrderedByName();
   }

   @PostConstruct
   public void retrieveAllRegistrantsOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Registrant> criteria = cb.createQuery(Registrant.class);
      Root<Registrant> registrant = criteria.from(Registrant.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(registrant).orderBy(cb.asc(registrant.get(Registrant_.name)));
      criteria.select(registrant).orderBy(cb.asc(registrant.get("name")));
      registrants = em.createQuery(criteria).getResultList();
   }
}
