/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
