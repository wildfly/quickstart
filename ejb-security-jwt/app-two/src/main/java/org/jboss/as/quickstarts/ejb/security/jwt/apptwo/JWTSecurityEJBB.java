/*
 *  Copyright (c) 2023 The original author or authors
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of Apache License v2.0 which
 *  accompanies this distribution.
 *
 *       The Apache License v2.0 is available at
 *       http://www.opensource.org/licenses/apache2.0.php
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package org.jboss.as.quickstarts.ejb.security.jwt.apptwo;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Implementation of {@link JWTSecurityEJBRemoteB}.
 *
 * @author <a href="mailto:aoingl@gmail.com">Lin Gao</a>
 */
@Stateless
@Remote(JWTSecurityEJBRemoteB.class)
@SecurityDomain("jwt-app-domain")
@PermitAll
public class JWTSecurityEJBB implements JWTSecurityEJBRemoteB {

    @Resource
    private SessionContext context;

    @Override
    public String securityInfo() {
        StringBuilder sb = new StringBuilder("Security Info in JWTSecurityEJBB: \n\tCaller: [");
        String caller = principal();
        sb.append(caller).append("]\n");
        sb.append("\t\t").append(caller).append(" has user role: (").append(inRole("user")).append(")\n");
        sb.append("\t\t").append(caller).append(" has admin role: (").append(inRole("admin")).append(")\n");
        System.out.println("\nSecurity Info(B): \n" + sb + "\n");
        return sb.toString();
    }

    @Override
    public String principal() {
        return context.getCallerPrincipal() != null ? context.getCallerPrincipal().getName() : null;
    }

    @Override
    public boolean inRole(String role) {
        return context.isCallerInRole(role);
    }
}
