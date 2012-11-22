/*
* JBoss, Home of Professional Open Source
* Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.as.quickstart.cdi.extension.test;

import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.cdi.parameterlogger.ParameterLoggerExtension;
import org.jboss.as.quickstart.cdi.parameterlogger.model.MyBean;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class ParameterLoggerTest {
    @Deployment
    public static Archive<?> getDeployment() {
        
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
        
        Archive<?> archive = ShrinkWrap.create(WebArchive.class, "parameter-logger.war")
                .addPackages(true, ParameterLoggerExtension.class.getPackage())
                .addClass(TestableParameterLog.class)
                .addAsWebInfResource("beans.xml")
                .addAsServiceProvider(Extension.class, ParameterLoggerExtension.class)
                .addAsLibraries(resolver.artifacts(
                        "org.apache.deltaspike.core:deltaspike-core-api", 
                        "org.apache.deltaspike.core:deltaspike-core-impl").resolveAsFiles());
        return archive;
    }

    @Inject MyBean myBean;
    
    @Inject TestableParameterLog testableParameterLog;

    @Test
    public void assertFilledMonster() {
        myBean.sayHello("Pete", "Hardy");
        assertTrue(testableParameterLog.contains("Logging parameter value for parameter 1 of method sayHello: Hardy"));
    }
}
