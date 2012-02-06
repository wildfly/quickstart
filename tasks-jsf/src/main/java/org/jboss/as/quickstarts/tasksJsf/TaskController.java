package org.jboss.as.quickstarts.tasksJsf;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * <p>
 * Basic operations for tasks owned by current user - additions, deletions/
 * </p>
 *
 * @author Lukas Fryc
 *
 */
@RequestScoped
@Named
public class TaskController {

    @Inject
    TaskDao taskDao;

    @Inject
    TaskList taskList;

    /**
     * Injects current user, which is provided by {@link AuthController}.
     */
    @Inject
    @CurrentUser
    Instance<User> currentUser;

    /**
     * Injects current user stored in the conversation scope
     */
    @Inject
    CurrentTaskStore currentTaskStore;

    /**
     * Set the current task to the context
     *
     * @param task current task to be set to context
     */
    public void setCurrentTask(Task task) {
        currentTaskStore.set(task);
    }

    /**
     * Creates new task and, if no task is selected as current, selects it.
     *
     * @param taskTitle
     */
    public void createTask(String taskTitle) {
        taskList.invalidate();
        Task task = new Task(taskTitle);
        taskDao.createTask(currentUser.get(), task);
        if (currentTaskStore.get() == null) {
            currentTaskStore.set(task);
        }
    }

    /**
     * Deletes given task
     *
     * @param task to delete
     */
    public void deleteTask(Task task) {
        taskList.invalidate();
        if (task.equals(currentTaskStore.get())) {
            currentTaskStore.unset();
        }
        taskDao.deleteTask(task);
    }

    /**
     * Deletes given task
     *
     * @param task to delete
     */
    public void deleteCurrentTask() {
        deleteTask(currentTaskStore.get());
    }
}
