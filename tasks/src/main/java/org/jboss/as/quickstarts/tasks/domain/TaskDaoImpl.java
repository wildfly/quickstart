package org.jboss.as.quickstarts.tasks.domain;

import org.jboss.as.quickstarts.tasks.beans.Repository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Provides functionality for manipulation with tasks using persistence operations using {@link Repository}.
 *
 * @author Lukas Fryc
 *
 */
@Stateless
@Local(TaskDao.class)
public class TaskDaoImpl implements TaskDao {

    @Inject
    Repository repository;

    @Override
    public void createTask(User user, Task task) {
        user = repository.attach(user);
        user.getTasks().add(task);
        task.setOwner(user);
        repository.create(task);
    }

    @Override
    public List<Task> getAll(User user) {
        TypedQuery<Task> query = querySelectAllTasksFromUser(user);
        return query.getResultList();
    }

    @Override
    public List<Task> getRange(User user, int offset, int count) {
        TypedQuery<Task> query = querySelectAllTasksFromUser(user);
        query.setMaxResults(count);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public List<Task> getForTitle(User user, String title) {
        String lowerCaseTitle = "%" + title.toLowerCase() + "%";
        return repository.query(Task.class, "SELECT t FROM Task t WHERE t.owner = ? AND LOWER(t.title) LIKE ?", user,
                lowerCaseTitle).getResultList();
    }

    @Override
    public void deleteTask(Task task) {
        task = repository.attach(task);
        repository.delete(task);
    }

    private TypedQuery<Task> querySelectAllTasksFromUser(User user) {
        return repository.query(Task.class, "SELECT t FROM Task t WHERE t.owner = ?", user);
    }
}