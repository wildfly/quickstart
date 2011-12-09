package org.jboss.as.quickstarts.kitchensinkjsp.controller;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.as.quickstarts.kitchensinkjsp.model.Member;

// The @Stateful annotation eliminates the need for manual transaction demarcation
@Stateful
// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class MemberRegistration {

   @Inject
   private Logger log;

   @Inject
   private EntityManager em;

   @Inject
   private Event<Member> memberEventSrc;

   private Member newMember;

   @Produces
   @Named
   public Member getNewMember() {

	   log.info("getNewMember: called"+newMember);
	   return newMember;
  
   }
   
   
   public void register() throws Exception {
	   
	  try{
		  
      log.info("Registering " + newMember.getName());
      em.persist(newMember);
      memberEventSrc.fire(newMember);
      initNewMember();
	  }
	  catch (Exception e) {
		  Throwable t=e;
			while((t.getCause())!=null){ t=t.getCause();}
			log.info("Exception:"+t.getMessage());
	  throw ((Exception)t);
	}
	  
   }
   

   
   @PostConstruct
   public void initNewMember() {
      newMember = new Member();
      log.info("@PostConstruct:initNewMember called");
   }
}
