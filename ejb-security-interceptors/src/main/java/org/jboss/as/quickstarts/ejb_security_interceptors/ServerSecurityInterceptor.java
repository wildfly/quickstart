/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.ejb_security_interceptors;

import java.security.Principal;
import java.util.Map;

import javax.ejb.EJBAccessException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.resource.spi.IllegalStateException;

import org.jboss.as.controller.security.SubjectUserInfo;
import org.jboss.as.domain.management.security.RealmUser;
import org.jboss.logging.Logger;
import org.jboss.remoting3.Connection;
import org.jboss.remoting3.security.UserInfo;
import org.jboss.security.SecurityContext;

/**
 * The server side security interceptor responsible for handling any security identity propagated from the client.
 * 
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ServerSecurityInterceptor {

    private static final Logger logger = Logger.getLogger(ServerSecurityInterceptor.class);

    static final String DELEGATED_USER_KEY = ServerSecurityInterceptor.class.getName() + ".DelegationUser";

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        Principal desiredUser = null;
        RealmUser connectionUser = null;

        Map<String, Object> contextData = invocationContext.getContextData();
        if (contextData.containsKey(DELEGATED_USER_KEY)) {
            desiredUser = new SimplePrincipal((String) contextData.get(DELEGATED_USER_KEY));

            Connection con = SecurityActions.remotingContextGetConnection();

            if (con != null) {
                UserInfo userInfo = con.getUserInfo();
                if (userInfo instanceof SubjectUserInfo) {
                    SubjectUserInfo sinfo = (SubjectUserInfo) userInfo;
                    for (Principal current : sinfo.getPrincipals()) {
                        if (current instanceof RealmUser) {
                            connectionUser = (RealmUser) current;
                            break;
                        }
                    }
                }

            } else {
                throw new IllegalStateException("Delegation user requested but no user on connection found.");
            }
        }

        SecurityContext cachedSecurityContext = null;
        boolean contextSet = false;
        try {
            if (desiredUser != null && connectionUser != null
                    && (desiredUser.getName().equals(connectionUser.getName()) == false)) {
                // The final part of this check is to verify that the change does actually indicate a change in user.
                try {
                    // We have been requested to switch user and have successfully identified the user from the connection
                    // so now we attempt the switch.
                    cachedSecurityContext = SecurityActions.securityContextSetPrincipalInfo(desiredUser,
                            new OuterUserCredential(connectionUser));
                    // keep track that we switched the security context
                    contextSet = true;
                    SecurityActions.remotingContextClear();
                } catch (Exception e) {
                    logger.error("Failed to switch security context for user", e);
                    // Don't propagate the exception stacktrace back to the client for security reasons
                    throw new EJBAccessException("Unable to attempt switching of user.");
                }
            }

            return invocationContext.proceed();
        } finally {
            // switch back to original security context
            if (contextSet) {
                SecurityActions.securityContextSet(cachedSecurityContext);
            }
        }
    }

    private static class SimplePrincipal implements Principal {

        private final String name;

        private SimplePrincipal(final String name) {
            if (name == null) {
                throw new IllegalArgumentException("name can not be null.");
            }
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof SimplePrincipal && equals((SimplePrincipal) other);
        }

        public boolean equals(SimplePrincipal other) {
            return this == other || other != null && name.equals(other.name);
        }

    }
}
