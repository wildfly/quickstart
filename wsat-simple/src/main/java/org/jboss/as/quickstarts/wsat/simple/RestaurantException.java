package org.jboss.as.quickstarts.wsat.simple;

/**
 * @author paul.robinson@redhat.com, 2012-01-06
 */
public class RestaurantException extends Exception {
    public RestaurantException(String message, Throwable cause) {
        super(message, cause);
    }
}
