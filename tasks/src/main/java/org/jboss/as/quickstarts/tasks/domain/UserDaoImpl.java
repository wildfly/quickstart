package org.jboss.as.quickstarts.tasks.domain;

import org.jboss.as.quickstarts.tasks.beans.Repository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Provides functionality for manipulation with users using persistence operations using {@link Repository}.
 *
 * @author Lukas Fryc
 *
 */
@Stateless
@Local(UserDao.class)
public class UserDaoImpl implements UserDao {

    @Inject
    private Repository repository;

    public User getForUsername(String username) {
        List<User> result = repository.query(User.class, "select u from User u where u.username = ?", username).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public void createUser(User user) {
        repository.create(user);
    }
}