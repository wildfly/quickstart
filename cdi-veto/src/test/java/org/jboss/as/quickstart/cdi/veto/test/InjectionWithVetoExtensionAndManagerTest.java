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

package org.jboss.as.quickstart.cdi.veto.test;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.cdi.veto.CarManager;
import org.jboss.as.quickstart.cdi.veto.EntityManagerProducer;
import org.jboss.as.quickstart.cdi.veto.VetoExtension;
import org.jboss.as.quickstart.cdi.veto.model.Car;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Demonstrate Car is now injectable via the producer even though it has been marked as vetoed.
 */
@RunWith(Arquillian.class)
public class InjectionWithVetoExtensionAndManagerTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, InjectionWithVetoExtensionAndManagerTest.class.getSimpleName() + ".war")
                .addClasses(Car.class, EntityManagerProducer.class, VetoExtension.class, CarManager.class)
                .addAsServiceProvider(Extension.class, VetoExtension.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                        // Using this to get the datasource xml file
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory("src/main/webapp").as(GenericArchive.class),
                        "/", Filters.include(".*ds\\.xml$"));
    }

    @Inject
    private Instance<Car> carInstance;

    @Inject
    private CarManager carManager;

    /*
        Also check the log for messages from the manager class
     */
    @Test
    public void assertCarNotUnsatisfied() {
        assertThat(carInstance.isUnsatisfied(), is(false));
        assertThat(carInstance.isAmbiguous(), is(false));
        assertThat(carInstance.get().getId(), is(nullValue()));

    }

    @Test
    public void assertSavingANewCarFunctions() {
        final Car car = carInstance.get();
        car.setColor("blue");
        car.setMake("Ford");
        car.setModel("Mustang");
        carManager.save(car);
        assertThat(car.getId(), notNullValue());
    }
}
