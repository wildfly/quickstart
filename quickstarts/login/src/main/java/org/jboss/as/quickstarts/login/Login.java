package org.jboss.as.quickstarts.login;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class Login implements Serializable {

   private static final long serialVersionUID = 7965455427888195913L;

   @Inject
   private Credentials credentials;

   @Inject
   private UserManager userManager;

   private User currentUser;

   public void login() throws Exception {
      User user = userManager.findUser(credentials.getUsername(), credentials.getPassword());
      if (user != null) {
         this.currentUser = user;
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome, " + currentUser.getName()));
      }
   }

   public void logout() {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Goodbye, " + currentUser.getName()));
      currentUser = null;
   }

   public boolean isLoggedIn() {
      return currentUser != null;
   }

   @Produces
   @LoggedIn
   public User getCurrentUser() {
      return currentUser;
   }

}