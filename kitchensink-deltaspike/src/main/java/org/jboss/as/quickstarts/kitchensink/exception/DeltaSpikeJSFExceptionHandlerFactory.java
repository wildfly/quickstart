package org.jboss.as.quickstarts.kitchensink.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.as.quickstarts.kitchensink.exception.handlers.DeltaSpikeExceptionHandler;


/**
 * This class is registered in src/main/webapp/WEB-INF/faces-config.xml file.
 * 
 * It creates te {@link DeltaSpikeExceptionHandler} class that is responsible to fire the {@link ExceptionToCatchEvent}
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 *
 */
public class DeltaSpikeJSFExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final javax.faces.context.ExceptionHandlerFactory parent;

    public DeltaSpikeJSFExceptionHandlerFactory(final ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new DeltaSpikeExceptionHandler(parent.getExceptionHandler());
    }

}