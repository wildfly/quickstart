package org.jboss.as.quickstarts.tasksrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasksrs.model.*;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 * @author Oliver Kiss
 */
@RunWith(Arquillian.class)
public class TaskDaoTest {

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment().withPersistence().withImportedData().getArchive()
                .addClasses(Resources.class, User.class, UserDao.class, Task.class, TaskDao.class, TaskDaoImpl.class);
    }

    @Inject
    private EntityManager em;

    @Inject
    private TaskDao taskDao;

    private User detachedUser;

    @Before
    public void setUp() throws Exception {
        detachedUser = new User("jdoe");
        detachedUser.setId(1L);
    }

    @Test
    public void user_should_be_created_with_one_task_attached() throws Exception {
        // given
        User user = new User("New user");
        Task task = new Task("New task");

        // when
        em.persist(user);
        taskDao.createTask(user, task);
        List<Task> userTasks = em.createQuery("SELECT t FROM Task t WHERE t.owner = :owner", Task.class)
                .setParameter("owner", user).getResultList();

        // then
        assertEquals(1, userTasks.size());
        assertEquals(task, userTasks.get(0));
    }

    @Test
    public void all_tasks_should_be_obtained_from_detachedUser() {
        // when
        List<Task> userTasks = taskDao.getAll(detachedUser);

        // then
        assertEquals(2, userTasks.size());
    }

    @Test
    public void range_of_tasks_should_be_provided_by_taskDao() {
        // when
        List<Task> headOfTasks = taskDao.getRange(detachedUser, 0, 1);
        List<Task> tailOfTasks = taskDao.getRange(detachedUser, 1, 1);

        // then
        assertEquals(1, headOfTasks.size());
        assertEquals(1, tailOfTasks.size());
        assertTrue(headOfTasks.get(0).getTitle().contains("first"));
        assertTrue(tailOfTasks.get(0).getTitle().contains("second"));
    }

    @Test
    public void taskDao_should_provide_basic_case_insensitive_full_text_search() {
        // given
        String taskTitlePart = "FIRST";

        // when
        List<Task> titledTasks = taskDao.getForTitle(detachedUser, taskTitlePart);

        // then
        assertEquals(1, titledTasks.size());
        assertTrue(titledTasks.get(0).getTitle().contains("first"));
    }

    @Test
    public void taskDao_should_remove_task_from_detachedUser() {
        // given
        Task task = new Task();
        task.setId(1L);
        task.setOwner(detachedUser);
        assertEquals(2, taskDao.getAll(detachedUser).size());

        // when
        taskDao.deleteTask(task);

        // then
        assertEquals(1, taskDao.getAll(detachedUser).size());
    }
}
