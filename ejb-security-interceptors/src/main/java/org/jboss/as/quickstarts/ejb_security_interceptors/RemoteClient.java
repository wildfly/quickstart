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

import static org.jboss.as.quickstarts.ejb_security_interceptors.EJBUtil.lookupIntermediateEJB;
import static org.jboss.as.quickstarts.ejb_security_interceptors.EJBUtil.lookupSecuredEJB;

import javax.ejb.EJBAccessException;

/**
 * The remote client responsible for making a number of calls to the server to demonstrate the capabilities of the interceptors.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class RemoteClient {

    /**
     * Perform the tests of this quick start using a thread local in the client-side interceptor to set the desired principal name.
     */
    private static void performTestingThreadLocal(final String user, final SecuredEJBRemote secured,
        final IntermediateEJBRemote intermediate) {
        try {
            if (user != null) {
                ClientSecurityInterceptor.delegateName.set(user);
            }

            System.out.println("-------------------------------------------------");
            System.out
                .println(String.format("* * About to perform test as %s * *\n\n", user == null ? "ConnectionUser" : user));

            makeCalls(secured, intermediate);
        } finally {
            ClientSecurityInterceptor.delegateName.remove();
            System.out.println("* * Test Complete * * \n\n\n");
            System.out.println("-------------------------------------------------");
        }
    }

    private static void makeCalls(final SecuredEJBRemote secured, final IntermediateEJBRemote intermediate) {
        System.out.println("* Making Direct Calls to the SecuredEJB\n");
        System.out.println(String.format("* getSecurityInformation()=%s", secured.getSecurityInformation()));

        boolean roleMethodSuccess;
        try {
            secured.roleOneMethod();
            roleMethodSuccess = true;
        } catch (EJBAccessException e) {
            roleMethodSuccess = false;
        }
        System.out.println(String.format("* Can call roleOneMethod()=%b", roleMethodSuccess));

        try {
            secured.roleTwoMethod();
            roleMethodSuccess = true;
        } catch (EJBAccessException e) {
            roleMethodSuccess = false;
        }
        System.out.println(String.format("* Can call roleTwoMethod()=%b", roleMethodSuccess));

        System.out.println("\n* Calling the IntermediateEJB to repeat the test server to server \n");
        System.out.println(intermediate.makeTestCalls());
    }

    public static void main(String[] args) throws Exception {
        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");

        SecuredEJBRemote secured = lookupSecuredEJB();
        IntermediateEJBRemote intermediate = lookupIntermediateEJB();

        System.out
            .println("This first round of tests is using the (PicketBox) SecurityContextAssociation API to set the desired Principal.\n\n");

        performTestingThreadLocal(null, secured, intermediate);
        performTestingThreadLocal("AppUserOne", secured, intermediate);
        performTestingThreadLocal("AppUserTwo", secured, intermediate);
        try {
            performTestingThreadLocal("AppUserThree", secured, intermediate);
            System.err.println("ERROR - We did not expect the switch to 'AppUserThree' to work.");
        } catch (Exception e) {
            System.out.println("Call as 'AppUserThree' correctly rejected.\n");
        }

        System.out.println("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n\n");
    }

}
