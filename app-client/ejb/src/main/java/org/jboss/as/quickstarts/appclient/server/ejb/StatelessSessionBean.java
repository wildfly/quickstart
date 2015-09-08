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
package org.jboss.as.quickstarts.appclient.server.ejb;

import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

/**
 * Simple implementation to show the behavior of EJB clients which are started in
 * the application client environment.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Stateless
public class StatelessSessionBean implements StatelessSession {
    private static final Logger LOGGER = Logger.getLogger(StatelessSessionBean.class);
    @Resource
    SessionContext context;

    @Override
    public void invokeWithClientContext() {
        LOGGER.info("ClientContext is here = " + context.getContextData());
    }

    @Override
    public String getGreeting() {
        return "Hello from StatelessSessionBean@" + System.getProperty("jboss.node.name");
    }
}
