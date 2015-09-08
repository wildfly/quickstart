/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.cdi.interceptor;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * LoggingInterceptor binds to {@link @Logging} annotation, so methods or beans which @Logging annotation is applied to, will be
 * intercepted.
 *
 * @author ievgen.shulga
 */
@Interceptor
// Binding the interceptor below. Now any business method or bean annotated with @Logging will be intercepted by
// LoggingInterceptor.aroundInvoke(..) method.
@Logging
public class LoggingInterceptor {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {
        String methodName = ic.getMethod().getName();
        logger.info("Executing " + ic.getTarget().getClass().getSimpleName() + "." + methodName + " method");
        return ic.proceed();
    }
}
