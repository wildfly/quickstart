package org.jboss.as.quickstart.hibernate4.data;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.jboss.as.quickstart.hibernate4.model.Member;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 * @author Madhumita Sadhukhan
 */

@RequestScoped
public class MemberListProducer {
   @Inject
   private EntityManager em;
   
   private List<Member> members;

   // @Named provides access the return value via the EL variable name "members" in the UI (e.g.,
   // Facelets or JSP view)
   @Produces
   @Named
   public List<Member> getMembers() {
      return members;
   }

   public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Member member) {
      retrieveAllMembersOrderedByName();
   }

   @PostConstruct
   public void retrieveAllMembersOrderedByName() {
	   
      //using Hibernate Session and Criteria Query via Hibernate Native API 
      Session session = (Session) em.getDelegate();
      Criteria cb = session.createCriteria(Member.class);
      cb.addOrder(Order.asc("name"));
      members = (List<Member>)cb.list();

   }
}
