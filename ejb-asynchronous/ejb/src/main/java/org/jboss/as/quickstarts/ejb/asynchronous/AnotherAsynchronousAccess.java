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
package org.jboss.as.quickstarts.ejb.asynchronous;

import java.util.concurrent.Future;

import javax.ejb.Asynchronous;
import javax.ejb.Local;

/**
 * Business interface to access an EJB method declared as asynchronous here.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Local
public interface AnotherAsynchronousAccess {
    /**
     * An example method to demonstrate that the annotation is also possible within the interface.
     *
     * @param sleepTime milliseconds to wait for demonstration
     * @return a simple string with a timestamp when the method is finished
     */
    @Asynchronous
    Future<String> interfaceAsync(long sleepTime);
}
