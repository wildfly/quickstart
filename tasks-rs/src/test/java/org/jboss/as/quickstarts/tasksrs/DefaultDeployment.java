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

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Enables prepare project-specific {@link WebArchive} for deployment.
 *
 * @author Lukas Fryc
 *
 */
public class DefaultDeployment {

    private static final String WEBAPP_SRC = "src/main/webapp";

    private WebArchive webArchive;

    public DefaultDeployment() {
        webArchive = ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource(
            new File(WEBAPP_SRC, "WEB-INF/beans.xml"));
    }

    public DefaultDeployment withPersistence() {
        webArchive = webArchive.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(
            "test-ds.xml", "test-ds.xml");
        return this;
    }

    public DefaultDeployment withImportedData() {
        webArchive = webArchive.addAsResource("import.sql");
        return this;
    }

    public WebArchive getArchive() {
        return webArchive;
    }
}
