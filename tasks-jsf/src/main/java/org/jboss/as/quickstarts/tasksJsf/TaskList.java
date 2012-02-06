package org.jboss.as.quickstarts.tasksJsf;

import java.util.List;


/**
 * <p>
 * Operations with cached list of tasks for current user.
 * </p>
 *
 * <p>
 * Implementation needs to preserve contracts for {@link #getAll()} and {@link #invalidate()}.
 * </p>
 *
 * @author Lukas Fryc
 *
 */
public interface TaskList {

    /**
     * <p>
     * Obtains list of tasks for current user.
     * </p>
     *
     * <p>
     * Caches current value; cache can be invalidated using {@link #invalidate()}.
     *
     * <p>
     * Delegates to persistence layer at most once per request or once per call of {@link #invalidate()}.
     * </p>
     *
     * @return
     */
    List<Task> getAll();

    /**
     * Invalidates all caches which this lists holds.
     */
    void invalidate();
}
