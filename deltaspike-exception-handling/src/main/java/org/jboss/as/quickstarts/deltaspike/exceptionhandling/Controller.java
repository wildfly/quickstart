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

package org.jboss.as.quickstarts.deltaspike.exceptionhandling;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.exception.MyException;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.exception.MyOtherException;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.exception.WebRequest;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Model
public class Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    // Inject CDI Event
    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    // The Service implementation always throws Exceptions
    @Inject
    private MyService myService;

    @Inject
    private FacesContext facesContext;

    public void testMyException() {
        try {
            // This Operation will throw an Exception
            myService.doSomeOperationWithAnException();
        } catch (MyException e) {
            // Fires the Event with the Exception (with expected Qualifier) to be handled 
            ExceptionToCatchEvent etce = new ExceptionToCatchEvent(e, e.getClass().getAnnotation(WebRequest.class));
            catchEvent.fire(etce);
            // Checks if the exception was handled
            if (!etce.isHandled()) {
                facesContext.addMessage(null, new FacesMessage("The Exception was not handled by any ExceptionHandler"));
            }
        }
    }

    public void testMyOtherException() {
        try {
            // This Operation will throw an Exception
            myService.doSomeOperationWithAnotherException();
        } catch (MyOtherException e) {
            // Fires the Event with the Exception (with expected Qualifier) to be handled 
            ExceptionToCatchEvent etce = new ExceptionToCatchEvent(e, e.getClass().getAnnotation(WebRequest.class));
            catchEvent.fire(etce);
            // Checks if the exception was handled
            if (!etce.isHandled()) {
                facesContext.addMessage(null, new FacesMessage("The Exception was not handled by any ExceptionHandler"));
            }
        }
    }
    

}
