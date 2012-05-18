package org.jboss.as.quickstart.hibernate4.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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

    @SuppressWarnings("unchecked")
    public List<Member> findAllOrderedByName() {
        // using Hibernate Session and Criteria Query via Hibernate Native API
        Session session = (Session) em.getDelegate();
        Criteria cb = session.createCriteria(Member.class);
        cb.addOrder(Order.asc("name"));
        return (List<Member>) cb.list();
        // return members;
    }
}
