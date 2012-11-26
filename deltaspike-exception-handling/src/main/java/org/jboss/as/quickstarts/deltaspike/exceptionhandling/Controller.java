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

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.exception.MyException;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.exception.MyOtherException;

/**
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Named
@SessionScoped
public class Controller implements Serializable {

    private static final long serialVersionUID = 1L;

    // Inject CDI Event
    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    // The Service implementation always throws Exceptions
    @Inject
    private MyService myService;

    public void testMyException() {
        try {
            // This Operation will throw an Exception
            myService.doSomeOperationWithAnException();
        } catch (MyException e) {
            // Fires the Event with the Exception to be handled
            catchEvent.fire(new ExceptionToCatchEvent(e));
        }
    }

    public void testMyOtherException() {
        try {
            // This Operation will throw an Exception
            myService.doSomeOperationWithAnotherException();
        } catch (MyOtherException e) {
            // Fires the Event with the Exception to be handled
            catchEvent.fire(new ExceptionToCatchEvent(e));
        }
    }

}
