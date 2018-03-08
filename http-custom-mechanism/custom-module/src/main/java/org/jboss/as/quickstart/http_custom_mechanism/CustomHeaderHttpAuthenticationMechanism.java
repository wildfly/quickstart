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
package org.jboss.as.quickstart.http_custom_mechanism;

import static org.jboss.as.quickstart.http_custom_mechanism.CustomMechanismFactory.CUSTOM_NAME;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;

import org.wildfly.security.auth.callback.AuthenticationCompleteCallback;
import org.wildfly.security.auth.callback.EvidenceVerifyCallback;
import org.wildfly.security.auth.callback.IdentityCredentialCallback;
import org.wildfly.security.credential.PasswordCredential;
import org.wildfly.security.evidence.PasswordGuessEvidence;
import org.wildfly.security.http.HttpAuthenticationException;
import org.wildfly.security.http.HttpServerAuthenticationMechanism;
import org.wildfly.security.http.HttpServerMechanismsResponder;
import org.wildfly.security.http.HttpServerRequest;
import org.wildfly.security.http.HttpServerResponse;
import org.wildfly.security.password.interfaces.ClearPassword;

/**
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
class CustomHeaderHttpAuthenticationMechanism implements HttpServerAuthenticationMechanism {

    /*
     * As the mechanism is instantiated by a factory it is generally a good practice to minimise visibility.
     */

    private static final String USERNAME_HEADER = "X-USERNAME";
    private static final String PASSWORD_HEADER = "X-PASSWORD";
    private static final String MESSAGE_HEADER = "X-MESSAGE";

    private static final HttpServerMechanismsResponder RESPONDER = new HttpServerMechanismsResponder() {
        /*
         * As the responses are always the same a static instance of the responder can be used.
         */

        public void sendResponse(HttpServerResponse response) throws HttpAuthenticationException {
            response.addResponseHeader(MESSAGE_HEADER, "Please resubit the request with a username specified using the X-USERNAME and a password specified using the X-PASSWORD header.");
            response.setStatusCode(401);
        }
    };

    private final CallbackHandler callbackHandler;

    CustomHeaderHttpAuthenticationMechanism(final CallbackHandler callbackHandler) {
        this.callbackHandler = callbackHandler;
    }

    public void evaluateRequest(HttpServerRequest request) throws HttpAuthenticationException {
        final String username = request.getFirstRequestHeaderValue(USERNAME_HEADER);
        final String password = request.getFirstRequestHeaderValue(PASSWORD_HEADER);

        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            /*
             * This mechanism is not performing authentication at this time however other mechanisms may be in use concurrently and could succeed so we register
             */
            request.noAuthenticationInProgress(RESPONDER);
            return;
        }

        /*
         * The first two callbacks are used to authenticate a user using the supplied username and password.
         */

        NameCallback nameCallback = new NameCallback("Remote Authentication Name", username);
        nameCallback.setName(username);
        final PasswordGuessEvidence evidence = new PasswordGuessEvidence(password.toCharArray());
        EvidenceVerifyCallback evidenceVerifyCallback = new EvidenceVerifyCallback(evidence);

        try {
            callbackHandler.handle(new Callback[] { nameCallback, evidenceVerifyCallback });
        } catch (IOException | UnsupportedCallbackException e) {
            throw new HttpAuthenticationException(e);
        }

        if (evidenceVerifyCallback.isVerified() == false) {
            request.authenticationFailed("Username / Password Validation Failed", RESPONDER);
        }

        /*
         * This next callback is optional, as we have the users password we can associate it with the private credentials of the
         * SecurityIdentity so it can be used again later.
         */

        try {
            callbackHandler.handle(new Callback[] {new IdentityCredentialCallback(new PasswordCredential(ClearPassword.createRaw(ClearPassword.ALGORITHM_CLEAR, password.toCharArray())), true)});
        } catch (IOException | UnsupportedCallbackException e) {
            throw new HttpAuthenticationException(e);
        }

        /*
         * The next callback is important, although at this stage they are authenticated an authorization check is now needed to
         * ensure the user has the LoginPermission granted allowing them to login.
         */

        AuthorizeCallback authorizeCallback = new AuthorizeCallback(username, username);

        try {
            callbackHandler.handle(new Callback[] {authorizeCallback});

            /*
             * Finally this example is very simple so we can deduce the outcome from the callbacks so far, however some
             * mechanisms may still go on to take additional information into account and make an alternative decision so a
             * callback is required to report the final outcome.
             */

            if (authorizeCallback.isAuthorized()) {
                callbackHandler.handle(new Callback[] { AuthenticationCompleteCallback.SUCCEEDED });
                request.authenticationComplete();
            } else {
                callbackHandler.handle(new Callback[] { AuthenticationCompleteCallback.FAILED });
                request.authenticationFailed("Authorization check failed.", RESPONDER);
            }
            return;
        } catch (IOException | UnsupportedCallbackException e) {
            throw new HttpAuthenticationException(e);
        }
    }

    public String getMechanismName() {
        return CUSTOM_NAME;
    }

}
