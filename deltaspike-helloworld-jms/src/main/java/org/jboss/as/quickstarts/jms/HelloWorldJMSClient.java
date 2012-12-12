/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;
import java.util.Properties;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.deltaspike.core.api.config.annotation.ConfigProperty;

@Default
public class HelloWorldJMSClient {

    private static final Logger log = Logger.getLogger(HelloWorldJMSClient.class.getName());

    @Inject
    @ConfigProperty(name = "username", defaultValue = "quickstartUser")
    private String usernameConfig;

    @Inject
    @ConfigProperty(name = "password", defaultValue = "quickstartPassword")
    private String passwordConfig;

    @Inject
    @ConfigProperty(name = "connection.factory", defaultValue = "jms/RemoteConnectionFactory")
    private String connectionFactoryConfig;

    @Inject
    @ConfigProperty(name = "destination", defaultValue = "jms/queue/test")
    private String destinationConfig;

    @Inject
    @ConfigProperty(name = "message.count", defaultValue = "1")
    private Integer messageCount;

    @Inject
    @ConfigProperty(name = "message.content", defaultValue = "Hello, World!")
    private String messageContent;

    // Set up all the default values
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";

    public void executeClient() throws Exception {

        Connection connection = null;
        TextMessage message = null;
        Context context = null;

        try {
            // Set up the context for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, usernameConfig);
            env.put(Context.SECURITY_CREDENTIALS, passwordConfig);
            context = new InitialContext(env);

            // Perform the JNDI lookups
            log.info("Attempting to acquire connection factory \"" + connectionFactoryConfig + "\"");
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryConfig);
            log.info("Found connection factory \"" + connectionFactoryConfig + "\" in JNDI");

            log.info("Attempting to acquire destination \"" + destinationConfig + "\"");
            Destination destination = (Destination) context.lookup(destinationConfig);
            log.info("Found destination \"" + destinationConfig + "\" in JNDI");

            // Create the JMS connection, session, producer, and consumer
            connection = connectionFactory.createConnection(usernameConfig, passwordConfig);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            MessageConsumer consumer = session.createConsumer(destination);
            connection.start();

            log.info("Sending " + messageCount + " messages with content: " + messageContent);

            // Send the specified number of messages
            for (int i = 0; i < messageCount; i++) {
                message = session.createTextMessage(messageContent);
                producer.send(message);
            }

            // Then receive the same number of messages that were sent
            for (int i = 0; i < messageCount; i++) {
                message = (TextMessage) consumer.receive(5000);
                log.info("Received message with content " + message.getText());
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        } finally {
            if (context != null) {
                context.close();
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
                connection.close();
            }
        }
    }
}
