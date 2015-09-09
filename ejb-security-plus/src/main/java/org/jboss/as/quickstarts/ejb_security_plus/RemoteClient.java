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

import static org.jboss.as.quickstarts.ejb_security_plus.EJBUtil.lookupSecuredEJB;

import org.jboss.security.SimplePrincipal;

/**
 * The remote client responsible for making a number of calls to the server to demonstrate the capabilities of the interceptors.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class RemoteClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");

        SimplePrincipal principal = new SimplePrincipal("quickstartUser");
        Object credential = new PasswordPlusCredential("quickstartPwd1!".toCharArray(), "7f5cc521-5061-4a5b-b814-bdc37f021acc");

        SecurityActions.securityContextSetPrincipalCredential(principal, credential);

        SecuredEJBRemote secured = lookupSecuredEJB();

        System.out.println(secured.getPrincipalInformation());

        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");
    }

}
