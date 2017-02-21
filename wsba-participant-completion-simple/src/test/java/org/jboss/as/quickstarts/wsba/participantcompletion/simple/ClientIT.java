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
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import org.junit.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.quickstarts.wsba.participantcompletion.simple.jaxws.SetServiceBA;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;

@RunWith(Arquillian.class)
public class ClientIT {

    private static final String ManifestMF = "Manifest-Version: 1.0\n"
        + "Dependencies: org.jboss.xts\n";

    @Inject
    @ClientStub
    public SetServiceBA client;

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addPackages(true, SetServiceBAImpl.class.getPackage().getName())
            .addAsResource("context-handlers.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .setManifest(new StringAsset(ManifestMF));
    }

    /**
     * Test the simple scenario where an item is added to the set within a Business Activity which is closed successfully.
     *
     * @throws Exception if something goes wrong.
     */
    @Test
    public void testSuccess() throws Exception {
        System.out
            .println("\n\nStarting 'testSuccess'. This test invokes a WS within a BA. The BA is later closed, which causes the WS call to complete successfully.");
        System.out.println("[CLIENT] Creating a new Business Activity");
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value = "1";

            System.out
                .println("[CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA wil be included in this activity)");
            uba.begin();

            System.out.println("[CLIENT] invoking addValueToSet(1) on WS");
            client.addValueToSet(value);

            System.out.println("[CLIENT] Closing Business Activity (This will cause the BA to complete successfully)");
            uba.close();

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value));
        } finally {
            cancelIfActive(uba);
            client.clear();
        }
    }

    /**
     * Tests the scenario where an item is added to the set with in a business activity that is later cancelled. The test checks
     * that the item is in the set after invoking addValueToSet on the Web service. After cancelling the Business Activity, the
     * work should be compensated and thus the item should no longer be in the set.
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testCancel() throws Exception {
        System.out
            .println("\n\nStarting 'testCancel'. This test invokes a WS within a BA. The BA is later cancelled, which causes these WS call to be compensated.");
        System.out.println("[CLIENT] Creating a new Business Activity");
        UserBusinessActivity uba = UserBusinessActivityFactory.userBusinessActivity();
        try {
            String value = "1";

            System.out
                .println("[CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA will be included in this activity)");
            uba.begin();

            System.out.println("[CLIENT] invoking addValueToSet(1) on WS");
            client.addValueToSet(value);

            Assert.assertTrue("Expected value to be in the set, but it wasn't", client.isInSet(value));

            System.out.println("[CLIENT] Cancelling Business Activity (This will cause the work to be compensated)");
            uba.cancel();

            Assert.assertTrue("Expected value to not be in the set, but it was", !client.isInSet(value));

        } finally {
            cancelIfActive(uba);
            client.clear();
        }

    }

    /**
     * Utility method for cancelling a Business Activity if it is currently active.
     *
     * @param uba The User Business Activity to cancel.
     */
    private void cancelIfActive(UserBusinessActivity uba) {
        try {
            uba.cancel();
        } catch (Throwable th2) {
            // do nothing, already closed
        }
    }
}
