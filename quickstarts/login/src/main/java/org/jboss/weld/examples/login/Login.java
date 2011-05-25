package org.jboss.weld.examples.login;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SessionScoped
@Named
public class Login implements Serializable
{

   private static final long serialVersionUID = 7965455427888195913L;

   @Inject
   private Credentials credentials;
   
   @PersistenceContext
   private EntityManager userDatabase;

   private User currentUser;

   @SuppressWarnings("unchecked")
   public void login()
   {

      List<User> results = userDatabase.createQuery("select u from User u where u.username=:username and u.password=:password").setParameter("username", credentials.getUsername()).setParameter("password", credentials.getPassword()).getResultList();

      if (!results.isEmpty())
      {
         currentUser = results.get(0);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome, " + currentUser.getName()));
      }

   }

   public void logout()
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Goodbye, " + currentUser.getName()));
      currentUser = null;
   }

   public boolean isLoggedIn()
   {
      return currentUser != null;
   }

   @Produces
   @LoggedIn
   public User getCurrentUser()
   {
      return currentUser;
   }

}