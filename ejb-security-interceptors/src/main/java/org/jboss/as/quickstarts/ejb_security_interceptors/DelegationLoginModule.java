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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.ObjectCallback;
import org.jboss.security.auth.spi.AbstractServerLoginModule;

/**
 * Login module to make the decision if one user can ask for the current request to be switched to an alternative specified
 * user.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class DelegationLoginModule extends AbstractServerLoginModule {

    private static final String DELEGATION_PROPERTIES = "delegationProperties";

    private static final String DEFAULT_DELEGATION_PROPERTIES = "delegation-mapping.properties";

    private Properties delegationMappings;

    private Principal identity;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        addValidOptions(new String[] { DELEGATION_PROPERTIES });
        super.initialize(subject, callbackHandler, sharedState, options);

        String propertiesName;
        if (options.containsKey(DELEGATION_PROPERTIES)) {
            propertiesName = (String) options.get(DELEGATION_PROPERTIES);
        } else {
            propertiesName = DEFAULT_DELEGATION_PROPERTIES;
        }
        try {
            delegationMappings = loadProperties(propertiesName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to load properties '%s'", propertiesName), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean login() throws LoginException {
        if (super.login() == true) {
            log.debug("super.login()==true");
            return true;
        }

        // Time to see if this is a delegation request.
        NameCallback ncb = new NameCallback("Username:");
        ObjectCallback ocb = new ObjectCallback("Password:");

        try {
            callbackHandler.handle(new Callback[] { ncb, ocb });
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            return false; // If the CallbackHandler can not handle the required callbacks then no chance.
        }

        String name = ncb.getName();
        Object credential = ocb.getCredential();

        if (credential instanceof OuterUserCredential) {
            // This credential type will only be seen for a delegation request, if not seen then the request is not for us.

            if (delegationAcceptable(name, (OuterUserCredential) credential)) {

                identity = new SimplePrincipal(name);
                if (getUseFirstPass()) {
                    String userName = identity.getName();
                    if (log.isDebugEnabled())
                        log.debug("Storing username '" + userName + "' and empty password");
                    // Add the username and an empty password to the shared state map
                    sharedState.put("javax.security.auth.login.name", identity);
                    sharedState.put("javax.security.auth.login.password", "");
                }
                loginOk = true;
                return true;
            }
        }

        return false; // Attempted login but not successful.
    }

    /**
     * Make a trust user to decide if the user switch is acceptable.
     *
     * The default implementation checks the Properties for the user that opened the connection looking for a match, the
     * property read is then used to check if the connection user can delegate to the user specified.
     *
     * The following entries will be checked in the Properties in this order: - user@realm - This is an exact match for the user
     * / realm combination of the connection user. user@* - This entry allows a match by username for any realm. *@realm - This
     * entry allows for any user in the realm specified. * - This matches all users.
     *
     * Once an entry has been found the Properties will not be read again, even if the entry loaded does not allow delegation.
     *
     * The value for the property is either '*' which means delegation to any user is allowed or a comma separate list of users
     * that can be delegated to.
     *
     * @param requestedUser - The user this request wants to be authorized as.
     * @param connectionUser - The use of the connection to the server.
     * @return true if a switch is acceptable, false otherwise.
     */
    protected boolean delegationAcceptable(String requestedUser, OuterUserCredential connectionUser) {
        if (delegationMappings == null) {
            return false;
        }

        String[] allowedMappings = loadPropertyValue(connectionUser.getName(), connectionUser.getRealm());
        if (allowedMappings.length == 1 && "*".equals(allowedMappings[1])) {
            // A wild card mapping was found.
            return true;
        }
        for (String current : allowedMappings) {
            if (requestedUser.equals(current)) {
                return true;
            }
        }

        return false;
    }

    private String[] loadPropertyValue(final String userName, final String realm) {
        String value = null;

        value = delegationMappings.getProperty(userName + "@" + realm);
        if (value == null) {
            value = delegationMappings.getProperty(userName + "@*");
        }
        if (value == null) {
            value = delegationMappings.getProperty("*@" + realm);
        }
        if (value == null) {
            value = delegationMappings.getProperty("*");
        }

        if (value == null) {
            return new String[0];
        } else {
            return value.split(",");
        }
    }

    @Override
    protected Principal getIdentity() {
        return identity;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        Group roles = new SimpleGroup("Roles");
        Group callerPrincipal = new SimpleGroup("CallerPrincipal");
        Group[] groups = { roles, callerPrincipal };
        callerPrincipal.addMember(getIdentity());
        return groups;
    }

    private Properties loadProperties(final String name) throws IOException {
        ClassLoader classLoader = SecurityActions.getContextClassLoader();
        URL url = classLoader.getResource(name);
        InputStream is = url.openStream();
        try {
            Properties props = new Properties();
            props.load(is);
            return props;

        } finally {
            is.close();
        }
    }

}
