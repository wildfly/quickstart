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
package org.jboss.as.quickstart.cdi.parameterlogger.model;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@ParameterLogger
public class ParameterLoggerInterceptor {

    @Inject ParameterLog log;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {
        for (int i = 0; i < ic.getMethod().getParameterAnnotations().length; i++) {
            for (int j = 0; j < ic.getMethod().getParameterAnnotations()[i].length; j++) {
                if (ic.getMethod().getParameterAnnotations()[i][j].annotationType().equals(LogParameterValue.class)) {
                    log.info("Logging parameter value for parameter " + i + " of method " + ic.getMethod().getName() + ": " + ic.getParameters()[i]);
                }
            }
        }
        return ic.proceed();
    }

}
