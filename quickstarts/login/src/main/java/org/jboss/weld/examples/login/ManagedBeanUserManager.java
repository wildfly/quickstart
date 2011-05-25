package org.jboss.weld.examples.login;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

@Named("userManager") @RequestScoped
public class ManagedBeanUserManager implements UserManager
{
   
   @Inject
   private transient Logger logger;

   @Inject
   private EntityManager userDatabase;

   @Inject
   private UserTransaction utx;

   private User newUser = new User();

   @SuppressWarnings("unchecked")
   @Produces
   @Named
   @RequestScoped
   public List<User> getUsers() throws Exception
   {
      try
      {
         try
         {
            utx.begin();
            return userDatabase.createQuery("select u from User u").getResultList();
         }
         finally
         {
            utx.commit();
         }
      }
      catch (Exception e)
      {
         utx.rollback();
         throw e;
      }
   }

   public String addUser() throws Exception
   {
      try
      {
         try
         {
            utx.begin();
            userDatabase.persist(newUser);
            logger.info("Added " + newUser);
            return "/users.xhtml?faces-redirect=true";
         }
         finally
         {
            utx.commit();
         }
      }
      catch (Exception e)
      {
         utx.rollback();
         throw e;
      }
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
