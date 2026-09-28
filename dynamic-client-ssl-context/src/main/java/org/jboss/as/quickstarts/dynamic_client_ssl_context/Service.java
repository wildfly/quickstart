/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.dynamic_client_ssl_context;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * It is important that jakarta.ws.rs.core.Application is extended
 * and the @ApplicationPath annotation is used with a "rest" path.
 * Without this the rest routes linked to index.html would not be found.
 */
@ApplicationPath("rest")
public class Service extends Application {
    // Left empty intentionally
}
