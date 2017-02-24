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
package org.jboss.as.quickstarts.tasksJsf;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Provides authentication operations with current user store: {@link Authentication}.
 *
 * @author Lukas Fryc
 *
 */
@Named
@RequestScoped
public class AuthController {

    @Inject
    private Authentication authentication;

    @Inject
    private UserDao userDao;

    @Inject
    private FacesContext facesContext;

    @Inject
    private Conversation conversation;

    /**
     * <p>
     * Provides current user to the context available for injection using:
     * </p>
     *
     * <p>
     * <code>@Inject @CurrentUser currentUser;</code>
     * </p>
     *
     * <p>
     * or from the Expression Language context using an expression <code>#{currentUser}</code>.
     * </p>
     *
     * @return current authenticated user
     */
    @Produces
    @Named
    @CurrentUser
    public User getCurrentUser() {
        return authentication.getCurrentUser();
    }

    /**
     * <p>
     * Authenticates current user with 'username' against user data store
     * </p>
     *
     * <p>
     * Starts the new conversation.
     * </p>
     *
     * @param username the username of the user to authenticate
     */
    public void authenticate(String username) {
        if (isLogged()) {
            throw new IllegalStateException("User is logged and tries to authenticate again");
        }

        User user = userDao.getForUsername(username);
        if (user == null) {
            user = createUser(username);
        }
        authentication.setCurrentUser(user);
        conversation.begin();
    }

    /**
     * Logs current user out and ends the current conversation.
     */
    public void logout() {
        authentication.setCurrentUser(null);
        conversation.end();
    }

    /**
     * Returns true if user is logged in
     *
     * @return true if user is logged in; false otherwise
     */
    public boolean isLogged() {
        return authentication.getCurrentUser() != null;
    }

    private User createUser(String username) {
        try {
            User user = new User(username);
            userDao.createUser(user);
            facesContext.addMessage(null, new FacesMessage("User successfully created"));
            return user;
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage("Failed to create user '" + username + "'", e.getMessage()));
            return null;
        }
    }
}
