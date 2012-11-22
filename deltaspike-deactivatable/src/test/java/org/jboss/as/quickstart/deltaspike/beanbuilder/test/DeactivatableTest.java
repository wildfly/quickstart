package org.jboss.as.quickstart.deltaspike.beanbuilder.test;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.deltaspike.deactivatable.MyBean;
import org.jboss.as.quickstart.deltaspike.deactivatable.ExcludeExtensionDeactivator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class DeactivatableTest {

    @Deployment
    public static Archive<?> getDeployment() {

        MavenDependencyResolver resolver = DependencyResolvers
                .use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

        Archive<?> archive = ShrinkWrap
                .create(WebArchive.class, "deactivator.war")
                .addPackages(true, ExcludeExtensionDeactivator.class.getPackage())
                .addAsLibraries(
                        resolver.artifacts("org.apache.deltaspike.core:deltaspike-core-api",
                                "org.apache.deltaspike.core:deltaspike-core-impl").resolveAsFiles())
                .addAsResource("META-INF/apache-deltaspike.properties")          
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }
    
    @Inject //The bean will be injected even using @Exclude because ExcludeExtension is deactivated.
    private MyBean myBean;

    @Test
    public void assertMyBeanInjected() {
        //Should not be null since ExcludeExtension was deactivated.
        assertThat(myBean, notNullValue());
    }
}
