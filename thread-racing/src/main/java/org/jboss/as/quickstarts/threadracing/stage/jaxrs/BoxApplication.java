package org.jboss.as.quickstarts.threadracing.stage.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 REST Services, and the {@link javax.ws.rs.ApplicationPath} defines the common path of such services.
 * @author Eduardo Martins
 */
@ApplicationPath(BoxApplication.PATH)
public class BoxApplication extends Application {

    /**
     * the jaxrs app path
     */
    public static final String PATH = "box";

    /**
     * To define a REST Service for the app simply add its class to the set returned by this method implementation.
     * @return
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(BoxService.class);
        return resources;
    }
    
}
