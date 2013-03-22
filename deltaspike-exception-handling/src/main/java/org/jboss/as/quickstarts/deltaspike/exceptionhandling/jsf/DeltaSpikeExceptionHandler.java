/*
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
package org.jboss.as.quickstarts.deltaspike.exceptionhandling.jsf;

import java.util.Iterator;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

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