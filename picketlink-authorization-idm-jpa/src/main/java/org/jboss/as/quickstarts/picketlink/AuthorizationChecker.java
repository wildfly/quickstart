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
package org.jboss.as.quickstarts.picketlink;

import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.model.Group;
import org.picketlink.idm.model.Role;

/**
 * This is a utility bean that may be used by the view layer to determine whether the
 * current user has specific privileges. 
 * 
 * @author Shane Bryzak
 *
 */
public @Named class AuthorizationChecker {
    @Inject Identity identity;
    @Inject IdentityManager identityManager;

    public boolean hasApplicationRole(String roleName) {
        Role role = identityManager.getRole(roleName);
        return identityManager.hasRole(identity.getUser(), role);
    }

    public boolean isMember(String groupName) {
        Group group = identityManager.getGroup(groupName);
        return identityManager.isMember(identity.getUser(), group);
    }

    public boolean hasGroupRole(String roleName, String groupName) {
        Group group = identityManager.getGroup(groupName);
        Role role = identityManager.getRole(roleName);
        return identityManager.hasGroupRole(identity.getUser(), role, group);
    }
}
