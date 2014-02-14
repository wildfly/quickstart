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
package org.jboss.as.quickstarts.ejb_security_plus;

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
import org.jboss.security.SimplePrincipal;

/**
 * The server side security interceptor responsible for handling any security token propagated from the client.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ServerSecurityInterceptor {

    private static final Logger logger = Logger.getLogger(ServerSecurityInterceptor.class);

    static final String SECURITY_TOKEN_KEY = ServerSecurityInterceptor.class.getName() + ".SecurityToken";

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        Principal userPrincipal = null;
        RealmUser connectionUser = null;
        String authToken = null;

        Map<String, Object> contextData = invocationContext.getContextData();
        if (contextData.containsKey(SECURITY_TOKEN_KEY)) {
            authToken = (String) contextData.get(SECURITY_TOKEN_KEY);

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
                userPrincipal = new SimplePrincipal(connectionUser.getName());

            } else {
                throw new IllegalStateException("Token authentication requested but no user on connection found.");
            }
        }

        SecurityContext cachedSecurityContext = null;
        boolean contextSet = false;
        try {
            if (userPrincipal != null && connectionUser != null && authToken != null) {
                try {
                    // We have been requested to use an authentication token
                    // so now we attempt the switch.
                    cachedSecurityContext = SecurityActions.securityContextSetPrincipalCredential(userPrincipal,
                            new OuterUserPlusCredential(connectionUser, authToken));
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

}
