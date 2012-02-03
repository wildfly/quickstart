package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class HelloWorldJMSClient {
    private static final Logger log = Logger.getLogger(HelloWorldJMSClient.class.getName());

    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final int DEFAULT_MESSAGE_COUNT = 1;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage message = null;

        try {
            connectionFactory = JMSClientUtil.getConnectionFactory();
            destination = JMSClientUtil.getDestination();
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            consumer = session.createConsumer(destination);
            connection.start();

            int count = (System.getProperty("message.count") == null) ? DEFAULT_MESSAGE_COUNT : Integer.valueOf(System
                    .getProperty("message.count"));
            String content = (System.getProperty("message.content") == null) ? DEFAULT_MESSAGE : System
                    .getProperty("message.content");

            log.info("Sending " + count + " messages with content: " + content);

            for (int i = 0; i < count; i++) {
                message = session.createTextMessage();
                message.setText(content);
                producer.send(message);

            }

            for (int i = 0; i < count; i++) {
                message = (TextMessage) consumer.receive();
                log.info("Received message with content " + message.getText());
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
        } finally {
            JMSClientUtil.closeResources(producer, consumer, session, connection);
        }

    }

}
