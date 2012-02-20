package org.jboss.as.quickstarts.tasks.domain;

import org.jboss.as.quickstarts.tasks.beans.Repository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Provides functionality for manipulation with tasks using persistence context from {@link Repository}.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateless
@Local(TaskDao.class)
public class TaskDaoImpl implements TaskDao {

    @Inject
    Repository repository;

    @Override
    public void createTask(User user, Task task) {
        EntityManager em = repository.getEntityManager();
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
        EntityManager em = repository.getEntityManager();
        String lowerCaseTitle = "%" + title.toLowerCase() + "%";
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ? AND LOWER(t.title) LIKE ?", Task.class)
                .setParameter(1, user)
                .setParameter(2, lowerCaseTitle)
                .getResultList();
    }

    @Override
    public void deleteTask(Task task) {
        EntityManager em = repository.getEntityManager();
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }

    private TypedQuery<Task> querySelectAllTasksFromUser(User user) {
        EntityManager em = repository.getEntityManager();
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ?", Task.class).setParameter(1, user);
    }
}