package org.jboss.as.quickstarts.kitchensink.web;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Provides common resources used across whole application
 * 
 * @author Lukas Fryc
 */
public class Resources {

    @Produces
    public Logger getLogger(InjectionPoint ip) {
        String category = ip.getMember().getDeclaringClass().getName();
        return Logger.getLogger(category);
    }
}
