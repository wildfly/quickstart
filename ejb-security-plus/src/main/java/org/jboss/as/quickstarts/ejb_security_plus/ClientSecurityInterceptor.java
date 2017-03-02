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
package org.jboss.as.quickstarts.ejb_security_plus;

import java.util.Map;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.jboss.ejb.client.annotation.ClientInterceptorPriority;

/**
 * Client side interceptor responsible for propagating the authentication token.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@ClientInterceptorPriority(ClientInterceptorPriority.APPLICATION + 10)
public class ClientSecurityInterceptor implements EJBClientInterceptor {

    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
        Object credential = SecurityActions.securityContextGetCredential();

        if (credential != null && credential instanceof PasswordPlusCredential) {
            PasswordPlusCredential ppCredential = (PasswordPlusCredential) credential;
            Map<String, Object> contextData = context.getContextData();
            contextData.put(ServerSecurityInterceptor.SECURITY_TOKEN_KEY, ppCredential.getAuthToken());
        }

        context.sendRequest();
    }

    public Object handleInvocationResult(EJBClientInvocationContext context) throws Exception {
        return context.getResult();
    }

}
