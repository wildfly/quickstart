/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * A servlet for triggering the update of a database and a JMS producer within a single XA transaction.
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

    private static String PAGE_HEADER = "<html><head><title>jta-crash-recovery</title></head><body>";

    // page content describing the quickstart and providing a form to perform basic operations against a database
    private static String PAGE_CONTENT = "<h2>XA Recovery</h2>" +
        "<p>This quickstart demonstrates how to atomically update multiple resources within one XA transaction. " +
        "It updates a relational database table using JPA and sends a message using JMS. " +
        "In this example, you can perform the following operations:</p>" +
        "<ul>" +
        "<li>To add a <b>key</b>/<b>value</b> pair, enter values in the input text boxes and click <i>Submit</i></li>" +
        "<li>To update a <b>key</b>/<b>value</b> pair, enter the key and click <i>Submit</i></li>" +
        "<li>To delete a <b>key</b>/<b>value</b> pair, enter the key and click <i>Delete</i></li>" +
        "<li>To delete all <b>key</b>/<b>value</b> pairs, leave the key blank and click <i>Delete</i></li>" +
        "<li>To list the existing <b>key</b>/<b>value</b> pairs, leave the key blank and click <i>Submit</i></li>" +
        "<li>The refresh the list, click the <i>Refresh Table</i> link.</li>" +
        "</ul>" +

        "<p>To demonstrate XA recovery, you will take the following steps:</p> " +
        "<ol>" +
        "<li>Add a <b>key</b>/<b>value</b> as described above.</li>" +
        "<li>Stop the JBoss EAP server.</li>" +
        "<li>Clear any transaction objectstore data remaining from previous tests.</li>" +
        "<li>Configure Byteman to halt the JBoss EAP server.</li>" +
        "<li>Start the JBoss EAP server.</li>" +
        "<li>Add another <b>key</b>/<b>value</b>. This will cause Byteman to halt the JBoss EAP server.</li>" +
        "<li>Verify the database record.</li>" +
        "<li>Disable Byteman.</li>" +
        "<li>Start the JBoss EAP server.</li>" +
        "<li>View the resulting recovered row in the table, indicated by the text \"<i>updated via JMS</i>\".</li>" +
        "</ol>" +
        "</p> " +
        "<p>See the README file located in the root of <i>jta-crash-rec</i> folder in the quickstart distribution " +
        "for complete instructions and details about how this quickstart works.</p> " +

        "<form>" +
        "Key: <input type=\"text\" name=\"key\" />" +
        "&nbsp;(Leave blank to list all key/value pairs).<br />" +
        "Value: <input type=\"text\" name=\"value\" /><br />" +
        "<input type=\"submit\" name=\"submit\" value=\"Submit\" />" +
        "&nbsp;Add or update a pair (or list all pairs if key is blank).<br />" +
        "<input type=\"submit\" name=\"submit\" value=\"Delete\" />" +
        "&nbsp;Delete a pair (or all pairs if key is blank).<br />" +
        "</form>" +
        "<p><a href=\".\">Refresh Table</a></p>";

    private static String PAGE_FOOTER = "</body></html>";

    // use CDI to inject a bean that will perform JPA and JMS operations
    @Inject
    private XAService xaService;

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
