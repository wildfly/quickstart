package org.jboss.as.quickstarts.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@MessageDriven(name = "testQueueMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jmsBridgeSourceQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "user", propertyValue = "admin"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "admin"),
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:jboss/ConnectionFactory"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class JmsTestQueueMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(JmsTestQueueMDB.class.toString());

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                LOGGER.info("Received Message from queue: " + msg.getText());
            } else {
                LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
