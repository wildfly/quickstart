package org.jboss.as.quickstarts.kitchensinkrf.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import org.jboss.as.quickstarts.kitchensinkrf.model.Member;

@ApplicationScoped
public class MemberRepository {

   @Inject
   private EntityManager em;

   public Member findById(Long id) {
      return em.find(Member.class, id);
   }

   public Member findByEmail(String email) {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
      Root<Member> member = criteria.from(Member.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).where(cb.equal(member.get(Member_.name), email));
      criteria.select(member).where(cb.equal(member.get("email"), email));
      return em.createQuery(criteria).getSingleResult();
   }

   public List<Member> findAllOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
      Root<Member> member = criteria.from(Member.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
      criteria.select(member).orderBy(cb.asc(member.get("name")));
      return em.createQuery(criteria).getResultList();
   }
}
