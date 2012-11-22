/*
* JBoss, Home of Professional Open Source
* Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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

package org.jboss.as.quickstart.cdi.parameterlogger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.apache.deltaspike.core.spi.activation.Deactivatable;
import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.jboss.as.quickstart.cdi.parameterlogger.model.LogParameterValue;
import org.jboss.as.quickstart.cdi.parameterlogger.model.MyBean;
import org.jboss.as.quickstart.cdi.parameterlogger.model.ParameterLogger;

/**
 * A simple CDI Portable Extension to add a interceptor binding annotation if certain parameter annotations are present
 */
public class ParameterLoggerExtension implements Extension, Deactivatable {

    /**
     * Observer to a CDI lifecycle event to correctly setup the XML backed "injection".
     * @param pit CDI lifecycle callback payload
     * @param <X> Type of the Injection to observe
     */
    <X extends MyBean> void addParameterLogger(@Observes ProcessAnnotatedType<X> pat) {
        boolean logAnyParameter = false;
        for (AnnotatedMethod<? super X> m : pat.getAnnotatedType().getMethods()) {
            for (AnnotatedParameter<? super X> p : m.getParameters()) {
                if (p.isAnnotationPresent(LogParameterValue.class)) {
                    logAnyParameter = true;
                }
            }
        }
        
        if (logAnyParameter) {
            AnnotatedTypeBuilder<X> builder = new AnnotatedTypeBuilder<X>()
                    .readFromType(pat.getAnnotatedType())
                    .addToClass(AnnotationInstanceProvider.of(ParameterLogger.class));
            pat.setAnnotatedType(builder.create());
            
        }
    }
}
