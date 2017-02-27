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
package org.jboss.as.quickstarts.tasksrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.as.quickstarts.tasksrs.model.Resources;
import org.jboss.as.quickstarts.tasksrs.model.Task;
import org.jboss.as.quickstarts.tasksrs.model.TaskDao;
import org.jboss.as.quickstarts.tasksrs.model.TaskDaoImpl;
import org.jboss.as.quickstarts.tasksrs.model.User;
import org.jboss.as.quickstarts.tasksrs.model.UserDao;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 * @author Oliver Kiss
 */
@RunWith(Arquillian.class)
public class TaskDaoIT {

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
    @InSequence(1)
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
    @InSequence(2)
    public void all_tasks_should_be_obtained_from_detachedUser() {
        // when
        List<Task> userTasks = taskDao.getAll(detachedUser);

        // then
        assertEquals(2, userTasks.size());
    }

    @Test
    @InSequence(3)
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
    @InSequence(4)
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
    @InSequence(5)
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
