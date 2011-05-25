package org.jboss.weld.examples.login;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named("userManager") @RequestScoped @Alternative @Stateful
public class EJBUserManager implements UserManager
{
   
   @Inject
   private transient Logger logger;

   @Inject
   private EntityManager userDatabase;

   private User newUser = new User();

   @SuppressWarnings("unchecked")
   @Produces
   @Named
   @RequestScoped
   public List<User> getUsers() throws Exception
   {
      return userDatabase.createQuery("select u from User u").getResultList();
   }

   public String addUser() throws Exception
   {
      userDatabase.persist(newUser);
      logger.info("Added " + newUser);
      return "/users.xhtml?faces-redirect=true";
   }

   public User getNewUser()
   {
      return newUser;
   }

   public void setNewUser(User newUser)
   {
      this.newUser = newUser;
   }

}
