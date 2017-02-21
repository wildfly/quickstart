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

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * <p>
 * Holds current task in context of conversation
 * </p>
 *
 * @author Lukas Fryc
 *
 */
@SuppressWarnings("serial")
@ConversationScoped
public class CurrentTaskStore implements Serializable {

    private Task currentTask;

    /**
     * <p>
     * Provides current task to the context available for injection using:
     * </p>
     *
     * <p>
     * <code>@Inject @CurrentTask currentTask;</code>
     * </p>
     *
     * <p>
     * or from the Expression Language context using an expression <code>#{currentTask}</code>.
     * </p>
     *
     * @return current authenticated user
     */
    @Produces
    @CurrentTask
    @Named("currentTask")
    public Task get() {
        return currentTask;
    }

    /**
     * Setup current task
     *
     * @param currentTask task to setup as current
     */
    public void set(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void unset() {
        this.currentTask = null;
    }
}
