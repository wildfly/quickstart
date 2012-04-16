package org.jboss.as.quickstarts.tasksrs.model;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Provides functionality for manipulation with tasks using the persistence context from {@link Resources}.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateful
public class TaskDaoImpl implements TaskDao {

    @Inject
    private EntityManager em;

    @Override
    public void createTask(User user, Task task) {
        if (!em.contains(user)) {
            user = em.merge(user);
        }
        user.getTasks().add(task);
        task.setOwner(user);
        em.persist(task);
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
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ? AND LOWER(t.title) LIKE ?", Task.class)
                .setParameter(1, user).setParameter(2, lowerCaseTitle).getResultList();
    }

    @Override
    public void deleteTask(Task task) {
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }

    private TypedQuery<Task> querySelectAllTasksFromUser(User user) {
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ?", Task.class).setParameter(1, user);
    }
}