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
package org.jboss.as.quickstarts.ejb.security.jwt.client;

import org.jboss.as.quickstarts.ejb.security.jwt.appone.JWTSecurityEJBRemoteA;
import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.credential.source.OAuth2CredentialSource;
import org.wildfly.security.sasl.SaslMechanismSelector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

/**
 * A Java client used to invoke remote EJB.
 */
public class RemoteEJBClient {

    private static final String CLIENT_ID = "app";
    private static final String CLIENT_SECRET = "secret";
    private static final String TOKEN_URL = "http://localhost:8180/realms/jwt-realm/protocol/openid-connect/token";
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASS = "admin";

    private static final String DEFAULT_JNDI_PROVIDER_URL = "remote+http://localhost:8080";

    public static void main(String[] args) throws Exception {
        // recursive is used to indicate if JWTSecurityEJBRemoteA should call JWTSecurityEJBRemoteB
        // it can only be set to true if JWTSecurityEJBRemoteA is deployed as an ear deployment.
        boolean recursive = Boolean.getBoolean("recursive");
        AuthenticationContext.getContextManager().setThreadDefault(null);
        invokeAppOne(recursive);

        System.out.println("\n\n* * * * * * *  Below are invoked using admin account  * * * * * *\n");
        switchToAdmin();
        invokeAppOne(recursive);
    }

    private static void invokeAppOne(boolean recursive) throws NamingException {
        JWTSecurityEJBRemoteA ejbRemoteA = lookupEJBRemoteA(recursive, DEFAULT_JNDI_PROVIDER_URL);
        System.out.println("\n* * * * * * * * * * * recursive: " + recursive + "  * * * * * * * * * * * * * * * * * * *\n");
        System.out.println(ejbRemoteA.securityInfo(recursive));
        System.out.println("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n");
    }

    public static void switchToAdmin() throws IOException {
        // now lets programmatically set up an authentication context to switch to admin user
        AuthenticationConfiguration superUser = AuthenticationConfiguration.empty()
                .setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("OAUTHBEARER"))
                .useCredentials(OAuth2CredentialSource
                        .builder(new URL(TOKEN_URL))
                        .clientCredentials(CLIENT_ID, CLIENT_SECRET)
                        .useResourceOwnerPassword(ADMIN_NAME, ADMIN_PASS)
                        .build());
        final AuthenticationContext authCtx = AuthenticationContext.empty().with(MatchRule.ALL, superUser);
        AuthenticationContext.getContextManager().setThreadDefault(authCtx);
    }

    public static JWTSecurityEJBRemoteA lookupEJBRemoteA(boolean ear, String providerURL) throws NamingException {
        InitialContext context = jndiContext(providerURL);
        try {
            final String jndiName = ear ?
                    "ejb:ejb-security-jwt-app-one/ejb-security-jwt-app-one-ejb/JWTSecurityEJBA!" + JWTSecurityEJBRemoteA.class.getName()
                    : "ejb:/ejb-security-jwt-app-one-ejb/JWTSecurityEJBA!" + JWTSecurityEJBRemoteA.class.getName();
            return (JWTSecurityEJBRemoteA)context.lookup(jndiName);
        } finally {
            context.close();
        }
    }

    private static InitialContext jndiContext(String providerURL) throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, providerURL);
        return new InitialContext(jndiProperties);
    }
}
