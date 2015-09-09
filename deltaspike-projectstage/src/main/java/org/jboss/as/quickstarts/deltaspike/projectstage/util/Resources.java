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
package org.jboss.as.quickstarts.deltaspike.projectstage.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.projectstage.ProjectStage;
import org.apache.deltaspike.core.util.ProjectStageProducer;
import org.jboss.as.quickstarts.deltaspike.projectstage.bean.MessageProvider;

/**
 *
 * This class uses CDI to produce the Current {@link ProjectStage} and get the available list of {@link MessageProvider}
 * implementations
 *
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
public class Resources {

    @Inject
    @Any
    // Allows the application to dynamically obtain instances of MessageProvider instances
    private Instance<MessageProvider> mesageProviders;

    /**
     * Return the current {@link ProjectStage}
     *
     * @return
     */
    @Produces
    @Named
    public ProjectStage currentProjectStage() {
        return ProjectStageProducer.getInstance().getProjectStage();
    }

    /**
     * This will create a {@link MessageProvider} {@link List} to be exposed as #{availableMessageProvidersImplementations} expression
     *
     * @return Return all available instances of {@link MessageProvider} implementations.
     */
    @Produces
    @Named
    public List<MessageProvider> availableMessageProvidersImplementations() {
        // * Since {@link Instance} only provides an {@link Iterator}, we need to convert it to a {@link List} so we can display
        // it on the JSF page
        return convertToList(mesageProviders.iterator());
    }

    /**
     * This utility method will convert any {@link Iterator} to a {@link List}
     *
     * @param the {@link Iterator}
     * @return the {@link List}
     */
    private <T> List<T> convertToList(Iterator<T> i) {
        List<T> list = new ArrayList<>();
        while (i.hasNext()) {
            list.add(i.next());
        }
        return list;
    }
}
