package org.jboss.as.quickstarts.tasksJsf;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasksJsf.Task;
import org.jboss.as.quickstarts.tasksJsf.TaskDao;
import org.jboss.as.quickstarts.tasksJsf.TaskList;
import org.jboss.as.quickstarts.tasksJsf.TaskListBean;
import org.jboss.as.quickstarts.tasksJsf.User;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
public class TaskListBeanTest {

    public static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment().withPersistence().withImportedData().getArchive()
                .addClasses(User.class, Task.class, TaskList.class, TaskListBean.class, TaskDao.class);
    }

    @Inject
    TaskDaoStub taskDaoStub;

    @Inject
    TaskList taskList;

    @Test
    public void dao_method_getAll_should_be_called_only_once_on() {
        taskList.getAll();
        taskList.getAll();
        taskList.getAll();
        assertEquals(1, taskDaoStub.getGetAllCallsCount());
    }

    @Test
    public void dao_method_getAll_should_be_called_after_invalidation() {
        taskList.getAll();
        taskList.getAll();
        assertEquals(1, taskDaoStub.getGetAllCallsCount());
        taskList.invalidate();
        assertEquals(1, taskDaoStub.getGetAllCallsCount());
        taskList.getAll();
        taskList.getAll();
        assertEquals(2, taskDaoStub.getGetAllCallsCount());
    }

    @RequestScoped
    public static class TaskDaoStub implements TaskDao {

        private int getAllCallsCount = 0;

        @Override
        public void createTask(User user, Task task) {
        }

        @Override
        public List<Task> getAll(User user) {
            getAllCallsCount += 1;
            return Arrays.asList(new Task[] {});
        }

        @Override
        public List<Task> getRange(User user, int offset, int count) {
            return null;
        }

        @Override
        public List<Task> getForTitle(User user, String title) {
            return null;
        }

        @Override
        public void deleteTask(Task task) {
        }

        public int getGetAllCallsCount() {
            return getAllCallsCount;
        }
    }
}
