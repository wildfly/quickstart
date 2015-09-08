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

import java.text.DateFormat;
import java.util.Date;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.as.quickstarts.cdi.service.History;

/**
 * AuditInterceptor binds to {@link @Audit} annotation, so methods or beans which @Audit annotation is applied to, will be
 * intercepted.
 *
 * @author ievgen.shulga
 */
@Interceptor
@Audit
public class AuditInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ic) throws Exception {
        String methodName = ic.getMethod().getName();
        String time = DateFormat.getTimeInstance().format(new Date());
        if (methodName.equals("create")) {
            History.getItemHistory().add(String.format("Item created at %s", time));
        } else if (methodName.equals("getList")) {
            History.getItemHistory().add(String.format("List of Items retrieved at %s", time));
        }
        return ic.proceed();
    }
}
