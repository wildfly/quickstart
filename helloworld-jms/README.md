helloworld-jms: HelloWorld JMS Example
======================
Author: Weston Price

What is it?
-----------

This quickstart demonstrates the use of external JMS clients with JBoss Enterprise Application Platform 6 or JBoss AS 7.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a JBoss Enterprise Application Platform 6 or JBoss AS 7 server.

2. A message consumer that receives message from a JMS destination deployed to a JBoss Enterprise Application Platform 6 or JBoss AS 7 server. 

This quickstart can be run from the Maven command line, or from the Eclipse environment. 


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.html/#addapplicationuser)


Start the JBoss Server
-------------------------

This quickstart requires a running server. Follow the instructions here to [Start the JBoss Server with the _full_ profile](../README.html/#startserverfull).



Build and Deploy the Quickstart
-------------------------

To run the quickstart from the command line:

1. Make sure you have started the JBoss server. See the instructions in the previous section.

2. Open a command line and navigate to the root of the directory of the helloworld-jms quickstart directory:

      cd PATH_TO_QUICKSTARTS/helloworld-jms

3. Type the following command to compile and execute the quickstart:

       For JBoss Enterprise Application Platform 6, Maven user settings NOT configured: 

           mvn clean compile exec:java -s PATH_TO_QUICKSTARTS/example-settings.xml

       For JBoss AS 7 or JBoss Enterprise Application Platform 6, Maven user settings configured: 

           mvn clean compile exec:java
 
4. If the maven command is successful, with the default configuration you will see output similar to this:

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

5. The example provides for a certain amount of customization for the `mvn:exec` plugin using the system properties described in the next section.


Optional Properties
-------------------

* `username`
   
    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.html/#addapplicationuser).
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.html/#addapplicationuser)
   
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

* `jboss.naming.provider.url`

	  This property allows configuration of the JNDI directory used to lookup the JMS destination. This is useful when the client resides on another host. 

    Default: `"localhost"`



 
