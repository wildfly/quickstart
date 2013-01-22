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


import static org.jboss.as.quickstarts.ejb_security_interceptors.EJBUtil.lookupSecuredEJB;

import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * An EJB for testing EJB to remote EJB calls.
 * 
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
@Stateless
@Remote(IntermediateEJBRemote.class)
@SecurityDomain("quickstart-domain")
@PermitAll
public class IntermediateEJB implements IntermediateEJBRemote {

    public String makeTestCalls() {
        try {
            StringBuilder sb = new StringBuilder("* * IntermediateEJB - Begin Testing * * \n");
            SecuredEJBRemote remote = lookupSecuredEJB();

            sb.append("SecuredEJBRemote.getSecurityInformation()=").append(remote.getSecurityInformation()).append("\n");

            try {
                sb.append("Can call roleOneMethod=");
                remote.roleOneMethod();
                sb.append("true\n");
            } catch (Exception e) {
                sb.append("false\n");
            }

            try {
                sb.append("Can call roleTwoMethod=");
                remote.roleTwoMethod();
                sb.append("true\n");
            } catch (Exception e) {
                sb.append("false\n");
            }

            sb.append("* * IntermediateEJB - End Testing * * ");
            return sb.toString();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Teasting failed.", e);
        }
    }

}
