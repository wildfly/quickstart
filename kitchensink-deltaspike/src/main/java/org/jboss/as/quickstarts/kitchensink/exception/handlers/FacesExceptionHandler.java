/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.quickstarts.kitchensink.exception.handlers;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.annotation.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.jboss.as.quickstarts.kitchensink.exception.annotation.FacesRequest;

/**
 * This exception handler uses {@link FacesMessage} to display the exception message on JSF
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@ExceptionHandler
public class FacesExceptionHandler {

    void showFacesMessage(@Handles @FacesRequest ExceptionEvent<Throwable> evt, FacesContext facesContext) {
        String errorMessage = getRootErrorMessage(evt.getException());
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, evt.getException().getMessage()));
        evt.handledAndContinue();
    }
    
    private String getRootErrorMessage(Throwable throwable) {
        // Default to general error message that registration failed.
        String errorMessage = "Operation failed. See server log for more information";
        if (throwable == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = throwable;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }


}
