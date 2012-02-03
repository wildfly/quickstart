package org.jboss.as.quickstarts.jms;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.InitialContext;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQJMSConnectionFactory;
import org.hornetq.jms.client.HornetQQueue;

public class JMSClientUtil {
    private static final Logger log = Logger.getLogger(JMSClientUtil.class.getName());

    private static final String DEFAULT_CONN_TYPE = "jndi";
    private static final String DEFAULT_CF_JNDI = "RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION_JNDI = "testQueue";

    public static ConnectionFactory getConnectionFactory() throws Exception {
        InitialContext context = null;
        ConnectionFactory connectionFactory = null;

        try {
            String connType = (System.getProperty("connection.type") == null) ? DEFAULT_CONN_TYPE : System
                    .getProperty("connection.type");
            String cfName = (System.getProperty("cf.name") == null) ? DEFAULT_CF_JNDI : System.getProperty("cf.name");

            log.info("Attempting to acquire ConnectionFactory with a connection type of: " + connType);

            if (connType.equalsIgnoreCase("jndi")) {
                context = new InitialContext();
                connectionFactory = (ConnectionFactory) context.lookup(cfName);
                log.info("Found ConnectionFactory " + cfName + " in JNDI");
            } else {
                log.info("Creating Netty Based ConnectionFactory.");
                TransportConfiguration config = new TransportConfiguration(NettyConnectorFactory.class.getName());
                connectionFactory = new HornetQJMSConnectionFactory(false, config);
            }

            return connectionFactory;

        } finally {
            closeResources(context);
        }

    }

    public static Destination getDestination() throws Exception {
        InitialContext context = null;
        Destination destination = null;

        try {
            String connType = (System.getProperty("connection.type") == null) ? DEFAULT_CONN_TYPE : System
                    .getProperty("connection.type");
            String destName = (System.getProperty("dest.name") == null) ? DEFAULT_DESTINATION_JNDI : System
                    .getProperty("dest.name");

            log.info("Attempting to acquire Destination with a connection type of: " + connType);

            if (connType.equalsIgnoreCase("jndi")) {
                log.info("Found Destination " + destName + " in JNDI");
                context = new InitialContext();
                destination = (Destination) context.lookup(destName);
            } else {
                log.info("HornetQ Destination " + destName);
                destination = new HornetQQueue(destName);
            }
            return destination;
        } finally {
            closeResources(context);
        }
    }

    public static void closeResources(Object... objects) {
        try {
            for (Object object : objects) {
                Method close = object.getClass().getMethod("close", new Class[] {});
                close.invoke(object, new Object[] {});
            }
        } catch (Exception ignore) {
        }

    }
}
