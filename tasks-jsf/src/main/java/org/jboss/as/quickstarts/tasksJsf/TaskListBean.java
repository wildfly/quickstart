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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * <p>
 * Operations with cached list of tasks for current user.
 * </p>
 *
 * <p>
 * Delegates to {@link TaskDao} for persistence operations.
 * </p>
 *
 * <p>
 * This bean ensures that task list will be obtained at most once per request or additionally after each invalidation (
 * {@link #invalidate()}).
 * </p>
 *
 * <p>
 * This behavior prevents unnecessary delegations to the persistence layer, since {{@link #getAll()} can be called several times
 * per request when used in view layer.
 * </p>
 *
 * @author Lukas Fryc
 */
@Named("taskList")
@RequestScoped
public class TaskListBean implements TaskList {

    private List<Task> tasks;

    @Inject
    private TaskDao taskDao;

    @Inject
    @CurrentUser
    private User currentUser;

    @Override
    public List<Task> getAll() {
        if (tasks == null) {
            tasks = taskDao.getAll(currentUser);
        }
        return tasks;
    }

    @Override
    public void invalidate() {
        tasks = null;
    }
}
