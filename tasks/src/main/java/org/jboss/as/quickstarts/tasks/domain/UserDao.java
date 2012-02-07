package org.jboss.as.quickstarts.tasks.domain;

/**
 * Basic operations for manipulation with users
 *
 * @author Lukas Fryc
 *
 */
public interface UserDao {

    public User getForUsername(String username);

    public void createUser(User user);
}
