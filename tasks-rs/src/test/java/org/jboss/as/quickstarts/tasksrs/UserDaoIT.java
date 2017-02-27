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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.tasksrs.model.Resources;
import org.jboss.as.quickstarts.tasksrs.model.Task;
import org.jboss.as.quickstarts.tasksrs.model.TaskDao;
import org.jboss.as.quickstarts.tasksrs.model.User;
import org.jboss.as.quickstarts.tasksrs.model.UserDao;
import org.jboss.as.quickstarts.tasksrs.model.UserDaoImpl;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 * @author Oliver Kiss
 */
@RunWith(Arquillian.class)
public class UserDaoIT {

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment().withPersistence().withImportedData().getArchive()
                .addClasses(Resources.class, User.class, UserDao.class, Task.class, TaskDao.class, UserDaoImpl.class);
    }

    @Inject
    private UserDao userDao;

    @Inject
    private EntityManager em;

    @Test
    public void userDao_should_create_user_so_it_could_be_retrieved_from_userDao_by_username() {
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
