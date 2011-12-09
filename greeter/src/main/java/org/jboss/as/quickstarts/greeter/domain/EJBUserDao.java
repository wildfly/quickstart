package org.jboss.as.quickstarts.greeter.domain;

import javax.ejb.Stateful;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Stateful
@Alternative
public class EJBUserDao implements UserDao {

   @Inject
   private EntityManager entityManager;

   public User getForUsername(String username) {
      try {
         Query query = entityManager.createQuery("select u from User u where u.username = ?");
         query.setParameter(1, username);
         return (User) query.getSingleResult();
      } catch (NoResultException e) {
         return null;
      }
   }

   public void createUser(User user) {
      entityManager.persist(user);
   }

}