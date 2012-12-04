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

package org.jboss.as.quickstarts.deltaspike.projectstage.controller.util;

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
import org.jboss.as.quickstarts.deltaspike.projectstage.controller.bean.MyBean;

/**
 * 
 * This class uses CDI to produce the Current {@link ProjectStage} and get the available list of {@link MyBean} implementations
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class Resources {

    @Inject
    @Any
    //Allows the application to dynamically obtain instances of MyBean instances
    private Instance<MyBean> myBeans; 

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
     * Return all available instances of {@link MyBean} implementations
     * 
     * @return
     */
    @Produces
    @Named
    public List<MyBean> availableBeansImplementation() {
        List<MyBean> beans = new ArrayList<MyBean>();
        Iterator<MyBean> it = myBeans.iterator();
        while (it.hasNext()) {
            MyBean mb = it.next();
            beans.add(mb);
        }
        return beans;
    }

}
