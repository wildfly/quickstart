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

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;

/**
 * Client side interceptor responsible for propagating the local identity.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ClientSecurityInterceptor implements EJBClientInterceptor {

    static final ThreadLocal<String> delegateName = new ThreadLocal<>();

    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
        String delegateUser = null;
        SecurityDomain securityDomain = SecurityDomain.getCurrent();
        if (securityDomain != null) {
            SecurityIdentity currentIdentity = securityDomain.getCurrentSecurityIdentity();
            if (!currentIdentity.isAnonymous()) {
                delegateUser = currentIdentity.getPrincipal().getName();
            }
        } else {
            delegateUser = delegateName.get();
        }

        if (delegateUser != null) {
            context.getContextData().put(ServerSecurityInterceptor.DELEGATED_USER_KEY, delegateUser);
        }
        context.sendRequest();
    }

    public Object handleInvocationResult(EJBClientInvocationContext context) throws Exception {
        return context.getResult();
    }

}
