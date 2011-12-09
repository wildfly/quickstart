package org.jboss.as.quickstarts.greeter.web;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.greeter.domain.User;
import org.jboss.as.quickstarts.greeter.domain.UserDao;

@Named
@RequestScoped
public class CreateController {

   @Inject
   private FacesContext facesContext;
   
   @Inject
   private UserDao userDao;
   
   @Named
   @Produces
   @RequestScoped
   private User newUser = new User();

   public void create() {
      try {
         userDao.createUser(newUser);
         facesContext.addMessage(null, new FacesMessage("A new user with id " + newUser.getId() + " has been created successfully"));
      } catch (Exception e) {
         facesContext.addMessage(null, new FacesMessage("An error has occured while creating the user (see log for details)"));
      }
   }
}
