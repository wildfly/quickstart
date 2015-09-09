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

import javax.ejb.Remote;

/**
 * Business interface to access asynchronous methods of the SLSB.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Remote
public interface AsynchronousAccess {

    /**
     * A method which is called and will not returning a result. It will be started and run to complete the work. The sleep
     * should simulate a complex action.
     */
    void fireAndForget(long sleepTime);

    /**
     * A method which is called and will return a result. It will be started and return the result after completed the work. The
     * sleep should simulate a complex action.
     */
    Future<String> longerRunning(long sleepTime);

    /**
     * A method that throws a Exception to demonstrate the behavior if the asynchronous processing fail.
     *
     * @return never
     * @throws Exception ever for demonstration purpose
     */
    Future<String> failure() throws IllegalAccessException;
}
