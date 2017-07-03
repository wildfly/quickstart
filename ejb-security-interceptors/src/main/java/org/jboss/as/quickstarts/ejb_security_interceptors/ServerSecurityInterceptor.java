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
package org.jboss.as.quickstarts.ejb_security_interceptors;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

/**
 * The server side security interceptor responsible for handling any security identity propagated from the client.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ServerSecurityInterceptor {

    static final String DELEGATED_USER_KEY = ServerSecurityInterceptor.class.getName() + ".DelegationUser";

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        SecurityDomain securityDomain = SecurityDomain.getCurrent();
        if (securityDomain != null) {
            Map<String, Object> contextData = invocationContext.getContextData();
            SecurityIdentity identity = securityDomain.getCurrentSecurityIdentity();
            if (identity != null && contextData.containsKey(DELEGATED_USER_KEY)) {
                String runAsUser = (String) contextData.get(DELEGATED_USER_KEY);
                if (!identity.getPrincipal().getName().equals(runAsUser)) {
                    identity = identity.createRunAsIdentity(runAsUser);
                    return identity.runAs((Callable<Object>) invocationContext::proceed);
                }
            }
        }
        return invocationContext.proceed();
    }
}
