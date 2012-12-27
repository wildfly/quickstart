package org.jboss.as.quickstarts.kitchensink.exception.handlers;

import java.util.Iterator;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.jboss.as.quickstarts.kitchensink.exception.annotation.FacesRequest;
import org.jboss.as.quickstarts.kitchensink.exception.annotation.FacesRequestLiteral;

/**
 * This class is responsible to get the wrapped exception and fire the {@link ExceptionToCatchEvent}
 * 
 * All exceptions are marked with {@link FacesRequest} annotation so the
 * {@link org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler} can distinguish between Faces Request
 * and Rest Request
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class DeltaSpikeExceptionHandler extends ExceptionHandlerWrapper {

    private final BeanManager beanManager;

    private final ExceptionHandler wrapped;

    public DeltaSpikeExceptionHandler(final ExceptionHandler wrapped) {
        this.wrapped = wrapped;
        this.beanManager = BeanManagerProvider.getInstance().getBeanManager();
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator();
        while (it.hasNext()) {
            try {
                ExceptionQueuedEvent evt = it.next();
                // Fires the Event with the Exception (with expected Qualifier) to
                // be handled
                ExceptionToCatchEvent etce = new ExceptionToCatchEvent(evt.getContext().getException(),
                        FacesRequestLiteral.INSTANCE);
                beanManager.fireEvent(etce);
            } finally {
                it.remove();
            }

        }
        getWrapped().handle();
    }
}