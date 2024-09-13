/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.helloworld.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JakartaRESTActivator is an arbitrary name, what is important is that jakarta.ws.rs.core.Application is extended
 * and the @ApplicationPath annotation is used with a "rest" path.  Without this the rest endpoints linked to
 * from index.html would not be found.
 */
@ApplicationPath("rest")
public class JakartaRESTActivator extends Application {
    // Left empty intentionally
}