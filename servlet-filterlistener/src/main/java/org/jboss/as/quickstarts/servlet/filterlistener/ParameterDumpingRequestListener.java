package org.jboss.as.quickstarts.servlet.filterlistener;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * A simple request listener that dumps all the parameters of the HTTP requests.
 * <p>
 * Because Request Listeners see requests before Filters see them, this listener
 * sees the original parameter values as sent by the user rather than the
 * modified ones passed down the filter chain by {@link VowelRemoverFilter}.
 * 
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@WebListener
public class ParameterDumpingRequestListener implements ServletRequestListener {

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    Map<String, String[]> paramMap = sre.getServletRequest().getParameterMap();
    ServletContext servletContext = sre.getServletContext();
    
    // to see log messages at runtime, check the terminal window where you started JBoss AS.
    servletContext
        .log("ParameterDumpingRequestListener: request has been initialized. It has " + paramMap.size() + " parameters:");
    for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
      for (String val : entry.getValue()) {
        servletContext.log("  " + entry.getKey() + "=" + val);
      }
    }
  }

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
    sre.getServletContext().log("ParameterDumpingRequestListener: request has been destroyed");
  }

}
