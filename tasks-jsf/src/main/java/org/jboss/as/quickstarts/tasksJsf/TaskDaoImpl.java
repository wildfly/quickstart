/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.tasksJsf;

import java.util.List;

import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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
    public void createTask(Person user, Task task) {
        if (!em.contains(user)) {
            user = em.merge(user);
        }
        user.getTasks().add(task);
        task.setOwner(user);
        em.persist(task);
    }

    @Override
    public List<Task> getAll(Person user) {
        TypedQuery<Task> query = querySelectAllTasksFromUser(user);
        return query.getResultList();
    }

    @Override
    public List<Task> getRange(Person user, int offset, int count) {
        TypedQuery<Task> query = querySelectAllTasksFromUser(user);
        query.setMaxResults(count);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public List<Task> getForTitle(Person user, String title) {
        String lowerCaseTitle = "%" + title.toLowerCase() + "%";
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ?1 AND LOWER(t.title) LIKE ?2", Task.class)
            .setParameter(1, user).setParameter(2, lowerCaseTitle).getResultList();
    }

    @Override
    public void deleteTask(Task task) {
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }

    private TypedQuery<Task> querySelectAllTasksFromUser(Person user) {
        return em.createQuery("SELECT t FROM Task t WHERE t.owner = ?1", Task.class).setParameter(1, user);
    }
}
