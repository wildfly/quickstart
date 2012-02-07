package org.jboss.as.quickstarts.tasks;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasks.beans.Repository;
import org.jboss.as.quickstarts.tasks.domain.User;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
public class RepositoryTest {

    private static final long FIRST_USER_ID = -1L;
    private static final String FIRST_USER_NAME = "jdoe";
    private static final int FIRST_USER_NUMBER_OF_TASKS = 2;

    @Deployment
    public static JavaArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return DefaultDeployment.deployment();
    }

    @Inject
    Repository repository;

    @Test
    public void entity_should_be_managed_outside_of_active_ejb() {
        User user = repository.retrieveById(User.class, FIRST_USER_ID);
        assertTrue(repository.isManaging(user));
    }

    @Test
    public void lazy_load_should_succeed_outside_of_active_ejb() {
        User user = repository.retrieveById(User.class, FIRST_USER_ID);
        assertEquals(FIRST_USER_NUMBER_OF_TASKS, user.getTasks().size());
    }

    @Test
    public void reattaching_entity_should_succeed() {
        User user = new User();
        user.setId(FIRST_USER_ID);

        user = repository.attachAndRefresh(user);

        assertEquals(FIRST_USER_NAME, user.getUsername());
        assertEquals(FIRST_USER_NUMBER_OF_TASKS, user.getTasks().size());
    }

    @Test
    public void dirty_change_should_be_flushed_by_transactional_method_on_managing_ejb() {
        User user = repository.retrieveById(User.class, FIRST_USER_ID);
        user.setUsername("renamed");
        repository.update(user);
        List<User> results = repository.query(User.class, "select u from User u where u.username = ?1", "renamed")
                .getResultList();
        assertEquals(1, results.size());
    }
}
