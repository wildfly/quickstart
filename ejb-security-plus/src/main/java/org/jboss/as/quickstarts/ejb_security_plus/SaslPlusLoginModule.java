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

import java.io.IOException;
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
 * A specialised login module to authenticate the user by taking the previously authenticated identity from the connection and
 * verifying it with additional data.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SaslPlusLoginModule extends AbstractServerLoginModule {

    private static final String ADDITIONAL_SECRET_PROPERTIES = "additionalSecretProperties";

    private static final String DEFAULT_AS_PROPERTIES = "additional-secret.properties";

    private Properties additionalSecrets;

    private Principal identity;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        addValidOptions(new String[] { ADDITIONAL_SECRET_PROPERTIES });
        super.initialize(subject, callbackHandler, sharedState, options);

        String propertiesName;
        if (options.containsKey(ADDITIONAL_SECRET_PROPERTIES)) {
            propertiesName = (String) options.get(ADDITIONAL_SECRET_PROPERTIES);
        } else {
            propertiesName = DEFAULT_AS_PROPERTIES;
        }
        try {
            additionalSecrets = SecurityActions.loadProperties(propertiesName);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to load properties '%s'", propertiesName), e);
        }
    }

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

        if (credential instanceof OuterUserPlusCredential) {

            OuterUserPlusCredential oupc = (OuterUserPlusCredential) credential;

            if (verify(name, oupc.getName(), oupc.getAuthToken())) {
                identity = new SimplePrincipal(name);
                if (getUseFirstPass()) {
                    String userName = identity.getName();
                    if (log.isDebugEnabled())
                        log.debug("Storing username '" + userName + "' and empty password");
                    // Add the username and an empty password to the shared state map
                    sharedState.put("javax.security.auth.login.name", identity);
                    sharedState.put("javax.security.auth.login.password", oupc);
                }
                loginOk = true;
                return true;
            }
        }

        return false; // Attempted login but not successful.
    }

    private boolean verify(final String authName, final String connectionUser, final String authToken) {
        // For the purpose of this quick start we are not supporting switching users, this login module is validation an
        // additional security token for a user that has already passed the sasl process.
        return authName.equals(connectionUser) && authToken.equals(additionalSecrets.getProperty(authName));
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

}
