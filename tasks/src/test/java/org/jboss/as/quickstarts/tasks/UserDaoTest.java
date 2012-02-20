package org.jboss.as.quickstarts.tasks;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasks.beans.Repository;
import org.jboss.as.quickstarts.tasks.domain.User;
import org.jboss.as.quickstarts.tasks.domain.UserDao;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Lukas Fryc
 * @author Oliver Kiss
 */
@RunWith(Arquillian.class)
public class UserDaoTest {

    @Deployment
    public static JavaArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return DefaultDeployment.deployment();
    }

    @Inject
    UserDao userDao;

    @Inject
    Repository repository;

    @Test
    public void userDao_should_create_user_so_it_could_be_retrieved_from_userDao_by_username() {
        EntityManager em = repository.getEntityManager();
        // given
        User created = new User("username1");

        // when
        userDao.createUser(created);
        User retrieved = userDao.getForUsername("username1");

        // then
        assertTrue(em.contains(created));
        assertTrue(em.contains(retrieved));
        Assert.assertEquals(created, retrieved);
    }

    @Test
    public void user_should_be_retrievable_from_userDao_by_username() {
        // given
        String username = "jdoe";

        // when
        User retrieved = userDao.getForUsername(username);

        // then
        Assert.assertEquals(username, retrieved.getUsername());
    }

    @Test
    public void userDao_should_return_null_when_searching_for_non_existent_user() {
        // given
        String nonExistent = "nonExistent";

        // when
        User retrieved = userDao.getForUsername(nonExistent);

        // then
        assertNull(retrieved);
    }
}
