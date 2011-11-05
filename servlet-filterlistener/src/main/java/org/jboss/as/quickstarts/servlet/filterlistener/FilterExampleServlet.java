package org.jboss.as.quickstarts.servlet.filterlistener;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple servlet that exists as a target for Servlet Filters and Servlet Listeners.
 * 
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@SuppressWarnings("serial")
@WebServlet("/FilterExample")
public class FilterExampleServlet extends HttpServlet {

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      PrintWriter writer = resp.getWriter();
      writer.println("<!DOCTYPE HTML>");
      writer.println("<html>");
      writer.println(" <head>");
      writer.println("  <title>Servlet filter example</title>");
      writer.println(" </head>");
      writer.println(" <body>");
      writer.println("  <form>");
      writer.println("   <label for=userInput>Enter some text:</label> <input type=text name=userInput>");
      writer.println("   <button type=submit>Send</button></form>");
      writer.println("  </form>");
      
      if (req.getParameter("userInput") != null) {
        writer.println("  <h1>You Typed " + req.getParameter("userInput") + "</h1>");
      }
      
      writer.println(" </body>");
      writer.println("</html>");
      writer.close();
   }

}
