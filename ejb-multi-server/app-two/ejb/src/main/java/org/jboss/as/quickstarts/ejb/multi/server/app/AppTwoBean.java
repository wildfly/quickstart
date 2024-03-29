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
package org.jboss.as.quickstarts.ejb.multi.server.app;

import java.security.Principal;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.logging.Logger;

/**
 * Simple bean with methods to get the node name of the server and log messages. One method is annotated with a security role.
 * The security-domain is declared by the JBoss specific annotation SecurityDomain.
 *
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@SecurityDomain(value = "other")
@Stateless
public class AppTwoBean implements AppTwo {
    private static final Logger LOGGER = Logger.getLogger(AppTwoBean.class);

    @Resource
    SessionContext context;

    @Override
    public String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }

    @Override
    public String invoke(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("[" + caller.getName() + "] " + text);
        return "app2[" + caller.getName() + "]@" + getJBossNodeName();
    }

}
