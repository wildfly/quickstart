/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.as.quickstarts.wsba.participantcompletion.simple;

import com.arjuna.mw.wst11.UserBusinessActivity;
import com.arjuna.mw.wst11.UserBusinessActivityFactory;
import junit.framework.Assert;
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
public class ClientTest {
    @Inject
    @ClientStub
    public SetServiceBA client;

    @Deployment
    public static WebArchive createTestArchive() {

        WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsba.war")
                .addPackages(true, "org.jboss.as.quickstarts.wsba.participantcompletion").addAsResource("context-handlers.xml")
                .addAsWebInfResource("web.xml", "web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

        /*
         * Remove the default MANIFEST.MF and replace with one that contains the required dependencies.
         */
        archive.delete(ArchivePaths.create("META-INF/MANIFEST.MF"));
        String ManifestMF = "Manifest-Version: 1.0\n"
                + "Dependencies: org.jboss.xts,deployment.arquillian-service,org.jboss.jts\n";
        archive.setManifest(new StringAsset(ManifestMF));

        return archive;
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
