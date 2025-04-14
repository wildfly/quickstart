/*
 * Copyright 2023 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.ejb.security.jwt;

import org.jboss.as.quickstarts.ejb.security.jwt.appone.JWTSecurityEJBRemoteA;
import org.jboss.as.quickstarts.ejb.security.jwt.client.RemoteEJBClient;
import org.junit.Assert;
import org.junit.Test;
import org.wildfly.security.auth.client.AuthenticationContext;

import javax.naming.NamingException;
import java.io.IOException;

/**
 * Tests that
 *
 * @author <a href="mailto:aoingl@gmail.com">Lin Gao</a>
 */
public class EJBSecurityJWTIT {
    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private String getProviderURl() {
        final String serverHost = System.getProperty("server.host");
        return "remote+" + (serverHost != null ? serverHost : DEFAULT_SERVER_HOST);
    }

    @Test
    public void testRegular() throws NamingException {
        AuthenticationContext.getContextManager().setThreadDefault(null);
        JWTSecurityEJBRemoteA remoteA = RemoteEJBClient.lookupEJBRemoteA(true, getProviderURl());
        Assert.assertEquals("quickstartuser", remoteA.principal().toLowerCase());
        Assert.assertTrue(remoteA.inRole("user"));
        Assert.assertFalse(remoteA.inRole("admin"));
        Assert.assertTrue(remoteA.inRoleFromB("user", true));
        Assert.assertFalse(remoteA.inRoleFromB("admin", true));
    }

    @Test
    public void testAdmin() throws IOException, NamingException {
        RemoteEJBClient.switchToAdmin();
        JWTSecurityEJBRemoteA remoteA = RemoteEJBClient.lookupEJBRemoteA(true, getProviderURl());
        Assert.assertEquals("admin", remoteA.principal().toLowerCase());
        Assert.assertTrue(remoteA.inRole("user"));
        Assert.assertTrue(remoteA.inRole("admin"));
        Assert.assertTrue(remoteA.inRoleFromB("user", true));
        Assert.assertTrue(remoteA.inRoleFromB("admin", true));
    }

}
