package org.jboss.as.quickstarts.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.model.Registrant;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class RegistrantRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Registrant> registrantEventSrc;

   private Registrant newRegistrant;

   @Produces
   @Named
   public Registrant getNewRegistrant() {
      return newRegistrant;
   }

   public void register() throws Exception {
      log.info("Registering " + newRegistrant.getName());
      em.persist(newRegistrant);
      registrantEventSrc.fire(newRegistrant);
      initNewRegistrant();
   }

   @PostConstruct
   public void initNewRegistrant() {
      newRegistrant = new Registrant();
   }
}
