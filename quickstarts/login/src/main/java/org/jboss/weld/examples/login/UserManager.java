package org.jboss.weld.examples.login;

import java.util.List;

public interface UserManager
{

   public List<User> getUsers() throws Exception;

   public String addUser() throws Exception;

   public User getNewUser();

   public void setNewUser(User newUser);

}