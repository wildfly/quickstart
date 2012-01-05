package org.jboss.as.quickstarts.bmt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.*;

/**
 * <p>
 * A servlet which shows how to manually manage transactions and database resources using
 * both managed (session bean) and (mostly) non-managed (simple CDI pojo) components. Reasons
 * why the developer might want to control transaction demarcation are various, for example
 * application requirements may need to access synchronizations or there may be XAResources
 * that are not supported by the JEE container.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /BMT using the {@linkplain WebServlet
 * @HttpServlet}. The {@link ManagedComponent} and {@link UserTransaction} are injected by CDI.
 * </p>
 *
 * @author Mike Musgrove
 *
 */

@WebServlet("/BMT")
public class TransactionServlet extends HttpServlet {
    static String PAGE_HEADER = "<html><head /><body>";
    static String PAGE_FOOTER = "</body></html>";

    /*
     * Inject a stateless bean. Although stateless beans are thread safe it is probably not a
     * solution that scales particularly well.
     */
    @Inject
    ManagedComponent managedBean;

    /*
     * Inject a POJO that will manage transactions and the JPA EntityManager itself
     */
    @Inject
    UnManagedComponent unManagedBean;

    /**
     * <p>Servlet entry point.
     * </p>
     * <p>The behaviour of the servlet is controlled by servlet query parameters.
     * If parameters named "key" and "value" are present then that pair is added (or the key is updated if it already
     * exists) to the database. If the parameter "strategy" is part of the request and has the value "unmanaged" then
     * both the transaction and the EntityManager are controlled manually. Otherwise the Entity Manager is controlled
     * by the container and the transaction is controlled by the developer.
     * </p>
     * @param req the HTTP request
     * @param resp the HTTP response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String responseText;
        String key = req.getParameter("key");
        String value = req.getParameter("value");
        String txStrategy =  req.getParameter("strategy");

        if ("unmanaged".equalsIgnoreCase(txStrategy))
            responseText = unManagedBean.updateKeyValueDatabase(key, value);
        else
            responseText = managedBean.updateKeyValueDatabase(key, value);

        writer.println(PAGE_HEADER);
        writer.println("<h1>" + responseText + "</h1>");
        writer.println(PAGE_FOOTER);

        writer.close();
    }

}
