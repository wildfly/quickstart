package org.jboss.as.quickstarts.tasksJsf;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;


/**
 * Store for current authenticated user
 *
 * @author Lukas Fryc
 *
 */
@ConversationScoped
public class Authentication implements Serializable {

    private static final long serialVersionUID = -1L;

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }
}
