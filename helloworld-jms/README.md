HelloWorld JMS Example
======================
This example demonstrates the use of external JMS clients with JBoss AS 7.

The example provides a demonstration of both producing and consuming messages to and from
a JMS destination deployed in the JBoss AS 7 environment. The example can be run from the Maven
commandline, or from the Eclipse environment. 

However, before the example can be run successfully please follow the instructions in the 
"Add a Management or Application User" section of the parent README.md.

By default, the JMS messaging provider is not deployed with the standalone JBoss AS 7 server. You will either need to run 
a domain server, or configure the standalone server for JMS message. Please refer to the JBoss AS 7 Documentation for further details.

To run the standalone server with JMS enabled:

    standalone.sh -c standalone-full.xml

To run the quickstart from the commandline:

    mvn compile exec:java

If the quickstart is successful with the default configuration you will see output similar to this:

    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!

The example provides for a certain amount of customization for the `mvn:exec` plugin via system properties explained below:


Optional Properties
-------------------

* `username`
   
    This username is used for both the JMS connection and the JNDI look-up.  See the instructions in the 
    "Add a Management or Application User" section of the parent README.md for more information.
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up.  See the instructions in the 
    "Add a Management or Application User" section of the parent README.md for more information.
   
    Default: `quickstartPassword`

* `connection.factory`

    The name of the JMS ConnectionFactory you want to use.

    Default: `jms/RemoteConnectionFactory`

* `destination`

    The name of the JMS Destination you want to use.
   
    Default: `jms/queue/test`

* `message.count`

    The number of JMS messages you want to produce and consume.

    Default: `1`

* `message.content`

    The content of the JMS TextMessage.
	
    Default: `"Hello, World!"`



	


 
