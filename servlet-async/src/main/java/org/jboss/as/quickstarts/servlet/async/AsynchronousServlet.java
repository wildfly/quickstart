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
package org.jboss.as.quickstarts.servlet.async;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple asynchronous servlet taking advantage of features added in 3.0.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /AsynchronousServlet using the {@link WebServlet} annotation. The
 * {@link LongRunningService} is injected by CDI.
 * </p>
 *
 * <p>
 * It shows how to detach the execution of a long-running task from the request processing thread, so the thread is free to
 * serve other client requests. The long-running tasks are executed using a dedicated thread pool and create the client response
 * asynchronously using the {@link AsyncContext}.
 * </p>
 *
 * <p>
 * A long-running task in this context does not refer to a computation intensive task executed on the same machine but could for
 * example be contacting a third-party service that has limited resources or only allows for a limited number of concurrent
 * connections. Moving the calls to this service into a separate and smaller sized thread pool ensures that less threads will be
 * busy interacting with the long-running service and that more requests can be served that do not depend on this service.
 * </p>
 *
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@SuppressWarnings("serial")
@WebServlet(value = "/AsynchronousServlet", asyncSupported = true)
public class AsynchronousServlet extends HttpServlet {

    @Inject
    private LongRunningService longRunningService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        // Here the request is put in asynchronous mode
        AsyncContext asyncContext = req.startAsync();

        // This method will return immediately when invoked,
        // the actual execution will run in a separate thread.
        longRunningService.readData(asyncContext);
    }
}
