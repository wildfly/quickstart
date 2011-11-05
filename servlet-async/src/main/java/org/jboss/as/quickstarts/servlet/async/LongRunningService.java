package org.jboss.as.quickstarts.servlet.async;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.servlet.AsyncContext;

/**
 * A simple service to simulate a long running task.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Stateless
public class LongRunningService {

  private Logger logger = Logger.getLogger(LongRunningService.class.getName());
  
  /**
   * The use of {@link Asynchronous} causes this EJB method to be executed
   * asynchronously, by a different thread from a dedicated, container managed
   * thread pool.
   * 
   * @param asyncContext
   *          the context for a suspended Servlet request that this EJB will
   *          complete later.
   */
   @Asynchronous
   public void readData(AsyncContext asyncContext) {
       try {
           // This is just to simulate a long running operation.
           Thread.sleep(5000);
      
           PrintWriter writer = asyncContext.getResponse().getWriter();
           writer.println(new SimpleDateFormat("HH:mm:ss").format(new Date()));
           writer.close();
           
           asyncContext.complete();
       } catch (Exception e) {
           logger.log(Level.SEVERE, e.getMessage(), e);
       }
   }
}
