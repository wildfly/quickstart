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
package org.jboss.as.quickstarts.shrinkwrap.resolver;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test demonstrates how to use Shrinkwrap-resolver to resolve artifacts using G:A:V (GroupId, ArtifactId and Version) and
 * also shows how to use G:A:P:C:V (GroupId, ArtifactId, Packaging, Classifier and Version) dependencies as a file array.
 *
 * The Shrinkwrap-resolver also was customized to use a custom maven settings xml file and disable the Maven Central repository.
 *
 * @author Rafael Benevides
 *
 */
@RunWith(Arquillian.class)
public class ShrinkwrapResolveGAPCVCustomRepoWithoutCentralIT {

    @Deployment
    public static Archive<?> createTestArchive() {

        String[] deps = {
            // GroupId, ArtifactId and Version
            "org.apache.commons:commons-lang3:3.1",
            // GroupId, ArtifactId, Packaging, Classifier and Version can also be used when necessary
            "org.apache.commons:commons-lang3:jar:sources:3.1"
        };

        File[] libs = Maven.configureResolver()
            // disabled Maven Central repository
            .withMavenCentralRepo(false)
            // use repository specified on a maven custom settings file
            .fromClassloaderResource("custom-settings.xml")
            .resolve(deps)
            .withoutTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(MyBean.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(libs)
            // Deploy our test datasource
            .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    private MyBean myBean;

    // Basic test to demonstrate that the Arquillian is working with the Shrinkwrap resolver use case in this class
    @Test
    public void test() {
        // toString uses commons-lang that was resolved by Shrinkwrap-resolver
        assertNotNull(myBean.toString());
    }
}
