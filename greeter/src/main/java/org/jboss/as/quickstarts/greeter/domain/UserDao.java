package org.jboss.as.quickstarts.greeter.domain;

public interface UserDao {
   User getForUsername(String username);

   void createUser(User user);
}