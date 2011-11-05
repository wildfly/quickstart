package org.jboss.as.quickstarts.servlet.async;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple asynchronous servlet taking advantage of features added in 3.0.
 * </p>
 * 
 * <p>
 * The servlet is registered and mapped to /AsynchronousServlet using the {@link WebServlet}
 * annotation. The {@link LongRunningService} is injected by CDI.
 * </p>
 * 
 * <p>
 * It shows how to detach the execution of a resource intensive task from the 
 * request processing thread, so the thread is free to serve other client requests. 
 * The resource intensive tasks are executed using a dedicated thread pool and 
 * create the client response asynchronously using the {@link AsyncContext}.
 * </p>
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@SuppressWarnings("serial")
@WebServlet(value="/AsynchronousServlet", asyncSupported=true)
public class AsynchronousServlet extends HttpServlet {

   @Inject
   private LongRunningService longRunningService;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
       // Here the request is put in asynchronous mode
       AsyncContext asyncContext = req.startAsync();
       
       // This method will return immediately when invoked, 
       // the actual execution will run in a separate thread.
       longRunningService.readData(asyncContext);
   }
}