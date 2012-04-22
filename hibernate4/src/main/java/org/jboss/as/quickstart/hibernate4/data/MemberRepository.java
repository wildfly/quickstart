package org.jboss.as.quickstart.hibernate4.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import org.jboss.as.quickstart.hibernate4.model.Member;

@ApplicationScoped
public class MemberRepository {

   @Inject
   private EntityManager em;

   public Member findById(Long id) {
      return em.find(Member.class, id);
   }

   /*public Member findByEmail(String email) {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
      Root<Member> member = criteria.from(Member.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
      criteria.select(member).where(cb.equal(member.get("email"), email));
      return em.createQuery(criteria).getSingleResult();
   }*/

   public List<Member> findAllOrderedByName() {

         
      //using Hibernate Session and Criteria Query via Hibernate Native API 
      Session session = (Session) em.getDelegate();
      Criteria cb = session.createCriteria(Member.class);
      cb.addOrder(Order.asc("name"));
      return (List<Member>)cb.list();
      //return members;
   }
}
