package org.jboss.as.quickstarts.servlet;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test servlet which can be used to invoke common JMS tasks in test classes.
 */
@WebServlet("/jms-test")
public class JmsTestServlet extends HttpServlet {
    static final String QUEUE_SEND_RESPONSE = "Sent a text message to ";
    static final String QUEUE_TEXT_MESSAGE = "Hello Servlet!";
    static final String QUEUE_MDB_SEND_RESPONSE = "Sent a text message with 'consumer=MDB' selector to ";
    static final String QUEUE_MDB_TEXT_MESSAGE = "Hello MDB!";

    @Resource(lookup = "java:/jms/queue/jmsBridgeSourceQueue")
    private Queue queue;

    @Inject()
    private JMSContext context;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        TextMessage textMessage;

        try (PrintWriter out = resp.getWriter()) {
            // produce and send a text message to testQueue
            if (req.getParameterMap().keySet().contains("produce")) {
                textMessage = context.createTextMessage(QUEUE_TEXT_MESSAGE);
                context.createProducer().send(queue, textMessage);
                out.println(QUEUE_SEND_RESPONSE + queue.toString());

                // produce and send a text message with consumer=MDB selector to testQueue
            } else if (req.getParameterMap().keySet().contains("produce-mdb")) {
                textMessage = context.createTextMessage(QUEUE_MDB_TEXT_MESSAGE);
                textMessage.setStringProperty("consumer", "MDB");
                context.createProducer().send(queue, textMessage);
                out.println(QUEUE_MDB_SEND_RESPONSE + queue.toString());

                // consume a text message from testQueue
            } else if (req.getParameterMap().keySet().contains("consume")) {
                textMessage = (TextMessage) context.createConsumer(queue).receive();
                out.println(textMessage.getText());

                // print usage
            } else {
                out.println("Usage:<ul>" +
                        "<li>use <b>?produce</b> parameter to sent a message to test queue</li>" +
                        "<li>use <b>?produce-mdb</b> parameter to sent a message with 'consume=MDB' selector to test queue</li>" +
                        "<li>use <b>?consume</b> parameter to consume a message from test queue</li>" +
                        "</ul>");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
