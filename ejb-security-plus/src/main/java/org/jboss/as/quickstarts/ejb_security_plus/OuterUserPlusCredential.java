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

import org.jboss.as.core.security.api.UserPrincipal;

/**
 * A wrapper around the user from the incoming connection plus some additional secret to authenticate the incoming user.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public final class OuterUserPlusCredential {

    private final UserPrincipal user;
    private final String authToken;

    OuterUserPlusCredential(final UserPrincipal user, final String authToken) {
        if (user == null) {
            throw new IllegalArgumentException("UserPrincipal can not be null.");
        }
        if (authToken == null) {
            throw new IllegalArgumentException("Auth token can not be null.");
        }
        this.user = user;
        this.authToken = authToken;
    }

    String getName() {
        return user.getName();
    }

    String getRealm() {
        return user.getRealm();
    }

    String getAuthToken() {
        return authToken;
    }

    @Override
    public int hashCode() {
        return user.hashCode() * authToken.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof OuterUserPlusCredential && equals((OuterUserPlusCredential) other);
    }

    public boolean equals(OuterUserPlusCredential other) {
        return this == other || other != null && user.equals(other.user) && authToken.equals(other.authToken);
    }

}
