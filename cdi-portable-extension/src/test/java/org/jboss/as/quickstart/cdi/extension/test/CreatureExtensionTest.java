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

package org.jboss.as.quickstart.cdi.extension.test;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstart.cdi.extension.CreatureExtension;
import org.jboss.as.quickstart.cdi.extension.model.Monster;
import org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Verification test.
 */
@RunWith(Arquillian.class)
public class CreatureExtensionTest {
    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "creature-creation.jar")
                .addPackages(true, CreatureExtension.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("creatures.xml")
                .addAsServiceProvider(Extension.class, CreatureExtension.class);
    }

    @Inject
    Monster m;

    @Inject
    NonPlayerCharacter npc;

    @Test
    public void assertFilledMonster() {
        assertThat(m.getName(), is("Cat"));
        assertThat(m.getHitPoints(), is(10));
        assertThat(m.getInitiative(), is(25));
    }

    @Test
    public void assertFilledNpc() {
        assertThat(npc.getName(), is("Drunkard"));
        assertThat(npc.getLocation(), is("Drunken Duck Tavern"));
    }
}
