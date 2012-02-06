package org.jboss.as.quickstarts.tasksJsf;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;


/**
 * <p>
 * Holds current task in context of conversation
 * </p>
 *
 * @author Lukas Fryc
 *
 */
@ConversationScoped
public class CurrentTaskStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private Task currentTask;

    /**
     * <p>
     * Provides current task to the context available for injection using:
     * </p>
     *
     * <p>
     * <code>@Inject @CurrentTask currentTask;</code>
     * </p>
     *
     * <p>
     * or from the Expression Language context using an expression <code>#{currentTask}</code>.
     * </p>
     *
     * @return current authenticated user
     */
    @Produces
    @CurrentTask
    @Named("currentTask")
    public Task get() {
        return currentTask;
    }

    /**
     * Setup current task
     *
     * @param currentTask task to setup as current
     */
    public void set(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void unset() {
        this.currentTask = null;
    }
}
