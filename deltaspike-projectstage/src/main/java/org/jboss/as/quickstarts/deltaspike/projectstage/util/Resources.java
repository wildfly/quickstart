/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
        List<T> list = new ArrayList<T>();
        while (i.hasNext()) {
            list.add(i.next());
        }
        return list;
    }
}
