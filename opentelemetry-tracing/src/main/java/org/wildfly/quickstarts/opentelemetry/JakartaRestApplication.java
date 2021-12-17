package org.wildfly.quickstarts.opentelemetry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/")
public class JakartaRestApplication extends Application {
}
