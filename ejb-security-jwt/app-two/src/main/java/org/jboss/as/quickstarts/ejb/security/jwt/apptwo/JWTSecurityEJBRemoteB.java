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

/**
 * An EJB to returns current caller identity information and if it has a specific role.
 *
 * @author <a href="mailto:aoingl@gmail.com">Lin Gao</a>
 */
public interface JWTSecurityEJBRemoteB {

    /**
     * The security information contains the principal and if it has role of user and admin.
     *
     * @return the security information in the current security context
     */
    String securityInfo();

    /**
     * @return the current caller principal.
     */
    String principal();

    /**
     * @param role the role to check
     * @return true if current identity has the role specified in the <code>role</code> parameter, false otherwise.
     */
    boolean inRole(String role);
}
