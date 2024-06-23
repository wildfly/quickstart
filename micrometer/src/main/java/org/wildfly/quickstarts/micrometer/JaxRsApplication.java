package org.wildfly.quickstarts.micrometer;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/rest")
public class JaxRsApplication extends Application {
}
