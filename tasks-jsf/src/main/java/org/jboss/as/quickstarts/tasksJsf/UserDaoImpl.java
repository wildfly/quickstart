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

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Provides functionality for manipulation with users using persistence context from {@link Resources}.
 *
 * @author Lukas Fryc
 * @author Oliver Kiss
 *
 */
@Stateful
public class UserDaoImpl implements UserDao {

    @Inject
    private EntityManager em;

    public User getForUsername(String username) {
        List<User> result = em.createQuery("select u from User u where u.username = ?1", User.class).setParameter(1, username)
            .getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public void createUser(User user) {
        em.persist(user);
    }
}
