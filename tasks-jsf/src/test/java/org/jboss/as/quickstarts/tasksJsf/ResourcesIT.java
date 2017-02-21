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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
public class ResourcesIT {

    public static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment().withPersistence().withFaces().getArchive()
            .addClasses(Resources.class, FacesContextStub.class);
    }

    @Inject
    private Instance<FacesContext> facesContextInstance;

    @Inject
    private Instance<Logger> loggerInstance;

    @Test
    public void facesContext_should_be_provided_from_current_context() {
        FacesContextStub.setCurrentInstance(new FacesContextStub("stub"));

        FacesContext facesContext = facesContextInstance.get();
        assertNotNull(facesContext);
        assertTrue(facesContext instanceof FacesContextStub);

        FacesContextStub.setCurrentInstance(null);

        facesContext = facesContextInstance.get();
        assertNull(facesContext);
    }

    @Test
    public void logger_should_be_provided_and_be_able_to_log_information_message() {
        Logger logger = loggerInstance.get();
        assertNotNull(logger);
        assertTrue(logger instanceof Logger);
        logger.log(Level.INFO, "test message");
    }

}
