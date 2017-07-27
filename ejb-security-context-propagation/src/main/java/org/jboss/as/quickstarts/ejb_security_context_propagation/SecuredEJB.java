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
package org.jboss.as.quickstarts.ejb_security_context_propagation;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * A secured EJB which is used to test the identity and roles of the current user during a request.
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
@Stateless
@Remote(SecuredEJBRemote.class)
@SecurityDomain("quickstart-domain")
public class SecuredEJB implements SecuredEJBRemote {

    @Resource
    private SessionContext context;

    @PermitAll
    public String getSecurityInformation() {
        StringBuilder sb = new StringBuilder("[");
        sb.append("Principal=[").append(context.getCallerPrincipal().getName()).append("], ");
        userInRole("guest", sb).append(", ");
        userInRole("user", sb).append(", ");
        userInRole("admin", sb).append("]");

        return sb.toString();
    }

    @RolesAllowed("guest")
    public String guestMethod() {
        return "Principal=[" + context.getCallerPrincipal() + "] has guest permissions";
    }

    @RolesAllowed("user")
    public String userMethod() {
        return "Principal=[" + context.getCallerPrincipal() + "] has user permissions";
    }

    @RolesAllowed("admin")
    public String adminMethod() {
        return "Principal=[" + context.getCallerPrincipal() + "] has admin permissions";
    }

    private StringBuilder userInRole(final String role, final StringBuilder sb) {
        sb.append("In role [").append(role).append("]=").append(context.isCallerInRole(role));
        return sb;
    }

}
