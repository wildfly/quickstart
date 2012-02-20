package org.jboss.as.quickstarts.tasks.domain;

import org.jboss.as.quickstarts.tasks.beans.Repository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Provides functionality for manipulation with users using persistence context from {@link Repository}.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateless
@Local(UserDao.class)
public class UserDaoImpl implements UserDao {

    @Inject
    Repository repository;

    public User getForUsername(String username) {
        EntityManager em = repository.getEntityManager();
        List<User> result = em.createQuery("select u from User u where u.username = ?", User.class)
                .setParameter(1, username)
                .getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public void createUser(User user) {
        EntityManager em = repository.getEntityManager();
        em.persist(user);
    }
}