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

import java.io.IOException;
import java.util.Properties;

import org.apache.deltaspike.core.spi.config.ConfigSource;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ProjectStageConfig implements ConfigSource {

    private Properties properties = new Properties();

    private String projectStage;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.deltaspike.core.spi.config.ConfigSource#getOrdinal()
     */
    @Override
    public int getOrdinal() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.deltaspike.core.spi.config.ConfigSource#getPropertyValue(java.lang.String)
     */
    @Override
    public String getPropertyValue(String key) {
        if ("org.apache.deltaspike.ProjectStage".equals(key)) {
            if (projectStage == null) {
                try {
                    properties.load(this.getClass().getResourceAsStream("/projectstage.properties"));
                    projectStage = properties.getProperty(key);
                } catch (IOException e) {
                    throw new RuntimeException("Exception reading /projectstage.properties", e);
                }
            }
            return projectStage;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.deltaspike.core.spi.config.ConfigSource#getConfigName()
     */
    @Override
    public String getConfigName() {
        return "project-stage";
    }

}
