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

import java.util.Arrays;
import java.util.List;

@Testing
public class TaskDaoStub implements TaskDao {

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
