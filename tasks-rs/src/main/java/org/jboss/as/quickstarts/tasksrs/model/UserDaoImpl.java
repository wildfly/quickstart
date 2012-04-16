package org.jboss.as.quickstarts.tasksrs.model;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Provides functionality for manipulation with users using persistence context from {@link Resources}.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateful
public class UserDaoImpl implements UserDao {

    @Inject
    private EntityManager em;

    public User getForUsername(String username) {
        List<User> result = em.createQuery("select u from User u where u.username = ?", User.class).setParameter(1, username)
                .getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public void createUser(User user) {
        em.persist(user);
    }
}