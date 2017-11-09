/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.quickstarts.security_domain_to_domain.ejb;

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * A simple EJB that can be called to obtain the current caller principal and to check the role membership for
 * that principal.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@Stateless
@SecurityDomain("BusinessDomain")
public class ManagementBean {

    @Resource
    private SessionContext sessionContext;

    @PermitAll
    public Principal getCallerPrincipal() {
        return sessionContext.getCallerPrincipal();
    }

    @PermitAll
    public boolean userHasRole(final String roleName) {
        return sessionContext.isCallerInRole(roleName);
    }

}
