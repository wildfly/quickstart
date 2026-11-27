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
package org.jboss.as.quickstarts.ejb.security.jwt.appone;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Implementation of {@link JWTSecurityEJBRemoteA}
 * @author <a href="mailto:aoingl@gmail.com">Lin Gao</a>
 */
@Stateless
@Remote(JWTSecurityEJBRemoteA.class)
@SecurityDomain("jwt-app-domain")
@PermitAll
public class JWTSecurityEJBA implements JWTSecurityEJBRemoteA {

    @Resource
    private SessionContext context;

    private InitialContext ctx;

    @PostConstruct
    public void setup() {
        try {
            final Hashtable<String, String> p = new Hashtable<>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
        } catch (NamingException e) {
            throw new RuntimeException("Could not initialize context", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        try {
            if (this.ctx != null) this.ctx.close();
        } catch (NamingException e) {
            throw new RuntimeException("Failed to close the context", e);
        }
    }

    @Override
    public String securityInfo(boolean recursive) {
        StringBuilder sb = new StringBuilder("Security Info in JWTSecurityEJBA: \n\tCaller: [");
        String caller = principal();
        sb.append(caller).append("]\n");
        sb.append("\t\t").append(caller).append(" has user role: (").append(inRole("user")).append(")\n");
        sb.append("\t\t").append(caller).append(" has admin role: (").append(inRole("admin")).append(")\n");
        System.out.println("\nSecurity Info(A): \n" + sb + "\n");
        if (recursive) {
            sb.append("\n===========  Below are invocation from remote EJB in app-two  ===========\n");
            sb.append(securityInfoFromB());
        }
        return sb.toString();
    }

    private String securityInfoFromB() {
        try {
            Object remote = lookupEJBB(false);
            Method securityInfoMethod = remote.getClass().getDeclaredMethod("securityInfo");
            return securityInfoMethod.invoke(remote).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object lookupEJBB(boolean local) throws NamingException {
        String app = local ? "ejb-security-jwt-app-one" : "";
        String lookup = "ejb:" + app + "/ejb-security-jwt-app-two/JWTSecurityEJBB!org.jboss.as.quickstarts.ejb.security.jwt.apptwo.JWTSecurityEJBRemoteB";
        return ctx.lookup(lookup);
    }

    @Override
    public String principal() {
        return context.getCallerPrincipal() != null ? context.getCallerPrincipal().getName() : null;
    }

    @Override
    public boolean inRole(String role) {
        return context.isCallerInRole(role);
    }

    @Override
    public String principalFromB(boolean local) {
        try {
            Object remote = lookupEJBB(local);
            Method securityInfoMethod = remote.getClass().getDeclaredMethod("principal");
            return securityInfoMethod.invoke(remote).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean inRoleFromB(String role, boolean local) {
        try {
            Object remote = lookupEJBB(local);
            Method securityInfoMethod = remote.getClass().getDeclaredMethod("inRole", String.class);
            return (Boolean)securityInfoMethod.invoke(remote, role);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
