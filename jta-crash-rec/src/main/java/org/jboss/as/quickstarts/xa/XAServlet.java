package org.jboss.as.quickstarts.xa;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * A servlet fo triggering the update of a database and a JMS producer within a single XA transaction.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /XA using the {@linkplain WebServlet
 * @HttpServlet}. The {@link XAService} is injected by CDI.
 * </p>
 *
 * @author Michael Musgrove
 *
 */
@SuppressWarnings("serial")
@WebServlet("/XA")
public class XAServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>XA Crash Recovery</title></head><body>";

    // page content describing the quickstart and providing a form to perform basic operations against a database
    static String PAGE_CONTENT = "<h2>XA Recovery</h2>" +
            "<p>Demonstration of how to atomically update a relational database table using JPA and send " +
            "a message using JMS (these kinds of paired updates to two different resources are called " +
            "XA transactions and are defined by the JEE JTA specification). " +
            "</p> " +
            "<p>The relational table contains two columns to represent a key value pair. " +
            "Below is an HTML form containing two input boxes for creating, updating, deleting or listing " +
            "such pairs. " +
            "</p> " +
            "<p>When you add or update a pair the example will start a transaction, update the table, " +
            "produce a JMS message (containing the change) and finally commit the transaction. If all goes well " +
            "then eventually the message is received by a consumer that will then modify the pair (with text that " +
            "indicates it was changed by the message consumer) and update the database. " +
            "The idea is to demonstrate recovery by halting the application server after the database modification " +
            "is committed but before the JMS producer is committed." +
            "</p> " +
            "<p>The <a href=\"readme.html\">readme instructions</a> that accompany this quickstart will walk you " +
            "through how to generate a failure in between the update to the database and the sending of the  " +
            "message (by halting the application server). After the failure if you use an SQL client to examine " +
            "the table you will see that it contains the pair you added but it will not show the update by the " +
            "JMS consumer since the message is still pending. When you restart the application server the " +
            "transaction recovery system will attempt to resend the message. On receipt by the consumer the " +
            "pair is updated and if you now examine the table this final change will be visible. But note that " +
            "since message consumption is asynchronous you may need to wait a moment before the message is " +
            "delivered to the consumer" +
            "</p> " +
            "<form>" +
            "Key: <input type=\"text\" name=\"key\" />" +
            "&nbsp;(Leave blank to select all key/value pairs).<br />" +
            "Value: <input type=\"text\" name=\"value\" /><br />" +
            "<input type=\"submit\" name=\"submit\" value=\"Submit\" />" +
            "&nbsp;Add or update a pair (or list all pairs if key is blank).<br />" +
            "<input type=\"submit\" name=\"submit\" value=\"Delete\" />" +
            "&nbsp;Delete a pair (or all pairs if key is blank).<br />" +
            "</form>" +
            "<p><a href=\".\">Refresh Table</a></p>";

    static String PAGE_FOOTER = "</body></html>";

    // use CDI to inject a bean that will perform JPA and JMS operations
    @Inject
    XAService xaService;

    /**
     * <p>Servlet entry point.
     * </p>
     * <p>The behaviour of the servlet is controlled by servlet query parameters:
     * </p>
     * <p>Database inserts/updates are controlled by the presence of parameters "submit" (with value "Submit") and "key".
     * </p>
     * <p>Database deletes are controlled by the presence of parameters "submit" (with value "Delete") and "key".
     * If the value of "key" is empty then all rows are deleted otherwise just the row with the key value is deleted.
     * </p>
     * @param req the HTTP request
     * @param resp the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * Read servlet parameters to determine which key value pair to use and
         * whether this is an insert/update/list request or a delete request
         * @see XAServlet#PAGE_CONTENT
         */
        String key = req.getParameter("key");
        String value = req.getParameter("value");
        boolean delete = "Delete".equals(req.getParameter("submit"));

        PrintWriter writer = resp.getWriter();
        // perform the database operation
        String responseText = xaService.updateKeyValueDatabase(delete, key, value);

        writer.println(PAGE_HEADER);
        writer.println(PAGE_CONTENT);
        writer.println(responseText);
        writer.println(PAGE_FOOTER);

        writer.close();
    }
}
