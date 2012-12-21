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

package org.jboss.as.quickstarts.deltaspike.exceptionhandling.handlers;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.annotation.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;
import org.jboss.as.quickstarts.deltaspike.exceptionhandling.MyException;

import java.io.Serializable;

/**
 * This exception handler just handle {@link MyException}.
 * 
 * This handler counts how many times this exception was handled in the user http session.
 * 
 * It can be accessed by JSF expression through the {@link Named} annotation.
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */

@Named
@ExceptionHandler
@SessionScoped
public class MyExceptionCountHandler implements Serializable {

    private static final long serialVersionUID = 1L;

    private int exceptionCount;

    public int getExceptionCount() {
        return exceptionCount;
    }

    public void countMyException(@Handles ExceptionEvent<MyException> myExceptionEvent) {
        exceptionCount++;
        myExceptionEvent.handledAndContinue();
    }

}
