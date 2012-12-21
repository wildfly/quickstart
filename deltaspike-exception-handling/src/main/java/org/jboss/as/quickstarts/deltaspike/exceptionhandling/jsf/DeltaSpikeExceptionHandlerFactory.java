package org.jboss.as.quickstarts.deltaspike.exceptionhandling.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class DeltaSpikeExceptionHandlerFactory extends ExceptionHandlerFactory {

    private final javax.faces.context.ExceptionHandlerFactory parent;

    public DeltaSpikeExceptionHandlerFactory(final ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new DeltaSpikeExceptionHandler(parent.getExceptionHandler());
    }

}