package org.jboss.as.quickstarts.xa;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Logger;

/**
 * <p>
 * A Message Driven Bean that asynchronously receives and processes
 * messages of the form key=value and updates a database table with
 * those values using the services of an injected bean.
 * </p>
 *
 * @author Serge Pagop (spagop@redhat.com)
 * @author Mike Musgrove (mmusgrov@redhat.com)
 *
 */
@MessageDriven(name = "DbUpdaterMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class DbUpdaterMDB implements MessageListener {
    // A helper service for persisting key value pairs
    @Inject
    XAService xaService;

    private final static Logger LOGGER = Logger.getLogger(DbUpdaterMDB.class.getName());

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message rcvMessage) {
        LOGGER.info("Received Message: " + rcvMessage.toString());

        try {
            if (rcvMessage instanceof TextMessage) {
                TextMessage msg = (TextMessage) rcvMessage;
                String sm = msg.getText();
                String[] kvPair = sm.split("=");

                // only process messages of the form key=value
                if (kvPair.length == 2 && kvPair[0].length() != 0) {
                    xaService.modifyKeyValueTable(false, kvPair[0], kvPair[1] + " updated via JMS");
                    LOGGER.info("JTA Crash Record Quickstart: key value pair updated via JMS");
                }
            } else {
                LOGGER.warning("JTA Crash Record Quickstart: Unexpected message. Type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
