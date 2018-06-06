/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.ejb_security_programmatic_auth;

import java.util.Hashtable;
import java.util.concurrent.Callable;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.wildfly.security.auth.client.AuthenticationConfiguration;
import org.wildfly.security.auth.client.AuthenticationContext;
import org.wildfly.security.auth.client.MatchRule;
import org.wildfly.security.sasl.SaslMechanismSelector;

/**
 * The remote client responsible for making calls to the secured EJB.
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class RemoteClient {

    public static void main(String[] args) throws Exception {
        AuthenticationConfiguration common = AuthenticationConfiguration.empty().setSaslMechanismSelector(SaslMechanismSelector.NONE.addMechanism("DIGEST-MD5"));
        AuthenticationConfiguration quickstartUser = common.useName("quickstartUser").usePassword("quickstartPwd1!");
        final AuthenticationContext authCtx1 = AuthenticationContext.empty().with(MatchRule.ALL, quickstartUser);

        System.out.println(authCtx1.runCallable(callable));

        AuthenticationConfiguration superUser = common.useName("quickstartAdmin").usePassword("adminPwd1!");
        final AuthenticationContext authCtx2 = AuthenticationContext.empty().with(MatchRule.ALL, superUser);

        System.out.println(authCtx2.runCallable(callable));

    }

    /**
     * A {@code Callable} that looks up the remote EJB and invokes its methods.
     */
    static final Callable<String> callable = () -> {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
        final Context context = new InitialContext(jndiProperties);

        SecuredEJBRemote reference = (SecuredEJBRemote) context.lookup("ejb:/ejb-security-programmatic-auth/SecuredEJB!"
                + SecuredEJBRemote.class.getName());

        StringBuilder builder = new StringBuilder();
        builder.append("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");
        builder.append("Called secured bean, caller principal " + reference.getSecurityInformation());
        boolean hasAdminPermission = false;
        try {
            hasAdminPermission = reference.administrativeMethod();
        } catch (EJBAccessException e) {
        }
        builder.append("\n\nPrincipal has admin permission: " + hasAdminPermission);
        builder.append("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");
        return builder.toString();
    };

}
