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

import static org.jboss.as.quickstarts.ejb_security_interceptors.EJBUtil.lookupIntermediateEJB;
import static org.jboss.as.quickstarts.ejb_security_interceptors.EJBUtil.lookupSecuredEJB;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBAccessException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.security.ClientLoginModule;
import org.jboss.security.SimplePrincipal;

/**
 * The remote client responsible for making a number of calls to the server to demonstrate the capabilities of the interceptors.
 * 
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class RemoteClient {

    /**
     * Perform the tests of this quick start using the SecurityContextAssociation API to set the desired Principal.
     */
    private static void performTestingSecurityContext(final String user, final SecuredEJBRemote secured,
            final IntermediateEJBRemote intermediate) {
        try {
            if (user != null) {
                SecurityActions.securityContextSetPrincpal(new SimplePrincipal(user));
            }

            System.out.println("-------------------------------------------------");
            System.out
                    .println(String.format("* * About to perform test as %s * *\n\n", user == null ? "ConnectionUser" : user));

            makeCalls(secured, intermediate);
        } finally {
            SecurityActions.securityContextClear();
            System.out.println("* * Test Complete * * \n\n\n");
            System.out.println("-------------------------------------------------");
        }
    }

    /**
     * Perform the tests of this quick start using the ClientLoginModule and LoginContext API to set the desired Principal.
     */
    private static void performTestingClientLoginModule(final String user, final SecuredEJBRemote secured,
            final IntermediateEJBRemote intermediate) throws Exception {
        LoginContext loginContext = null;
        try {
            if (user != null) {
                loginContext = getCLMLoginContext(user, "");
                loginContext.login();
            }

            System.out.println("-------------------------------------------------");
            System.out
                    .println(String.format("* * About to perform test as %s * *\n\n", user == null ? "ConnectionUser" : user));

            makeCalls(secured, intermediate);
        } finally {
            if (loginContext != null) {
                loginContext.logout();
            }
            System.out.println("* * Test Complete * * \n\n");
            System.out.println("-------------------------------------------------");
        }
    }

    public static LoginContext getCLMLoginContext(final String username, final String password) throws LoginException {
        final String configurationName = "Testing";

        CallbackHandler cbh = new CallbackHandler() {
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (Callback current : callbacks) {
                    if (current instanceof NameCallback) {
                        ((NameCallback) current).setName(username);
                    } else if (current instanceof PasswordCallback) {
                        ((PasswordCallback) current).setPassword(password.toCharArray());
                    } else {
                        throw new UnsupportedCallbackException(current);
                    }
                }
            }
        };
        Configuration config = new Configuration() {

            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                if (configurationName.equals(name) == false) {
                    throw new IllegalArgumentException("Unexpected configuration name '" + name + "'");
                }
                Map<String, String> options = new HashMap<String, String>();
                options.put("multi-threaded", "true");
                options.put("restore-login-identity", "true");

                AppConfigurationEntry clmEntry = new AppConfigurationEntry(ClientLoginModule.class.getName(),
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);

                return new AppConfigurationEntry[] { clmEntry };
            }
        };

        return new LoginContext(configurationName, new Subject(), cbh, config);
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

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\n\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n");

        SecuredEJBRemote secured = lookupSecuredEJB();
        IntermediateEJBRemote intermediate = lookupIntermediateEJB();

        System.out
                .println("This first round of tests is using the (PicketBox) SecurityContextAssociation API to set the desired Principal.\n\n");

        performTestingSecurityContext(null, secured, intermediate);
        performTestingSecurityContext("AppUserOne", secured, intermediate);
        performTestingSecurityContext("AppUserTwo", secured, intermediate);
        try {
            performTestingSecurityContext("AppUserThree", secured, intermediate);
            System.err.println("ERROR - We did not expect the switch to 'AppUserThree' to work.");
        } catch (Exception e) {
            System.out.println("Call as 'AppUserThree' correctly rejected.\n");
        }

        System.out
                .println("This second round of tests is using the (PicketBox) ClientLoginModule with LoginContext API to set the desired Principal.\n\n");

        performTestingClientLoginModule(null, secured, intermediate);
        performTestingClientLoginModule("AppUserOne", secured, intermediate);
        performTestingClientLoginModule("AppUserTwo", secured, intermediate);
        try {
            performTestingClientLoginModule("AppUserThree", secured, intermediate);
            System.err.println("ERROR - We did not expect the switch to 'AppUserThree' to work.");
        } catch (Exception e) {
            System.out.println("Call as 'AppUserThree' correctly rejected.\n");
        }

        System.out.println("\n\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n\n\n");
    }

}
