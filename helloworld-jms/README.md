HelloWorld JMS Example
======================
This example demonstrates the use of external JMS clients with JBoss AS 7.

The example provides a demonstration of both producing and consuming messages to and from
a JMS destination deployed in the JBoss AS 7 environment. The example can be run from the Maven
commandline, or from the Eclipse environment. 

To run the example from the commandline simply type:

    mvn compile exec:java

The example provides for a certain amount of customization for the `mvn:exec` plugin via system properties explained below:


Required Properties
-------------------

* `connection.type`
   
   Which connection type to the JMS provider you want to use. Accepted values are `netty` or `jndi`.
   
   Default: `netty`
   
   *Note:* Currently external JNDI access is under development in JBoss AS 7. As a result netty is the default value.
		
* `java.naming.factory.initial`

   The JNDI initial context factory you wish to use
   
   Default: `org.jboss.as.naming.InitialContextFactory`

* `java.naming.provider.url`
   
   The provider URL of the JNDI context you wish to use

   Default: `jnp://localhost:1099`


Optional Properties
-------------------

* `cf.name`

   The JNDI name of the JMS ConnectionFactory you want to use.

   Default: `RemoteConnectionFactory`

* `dest.name`

   The JNDI name of the JMS Destination you want to use.
   
   Default: `testQueue`

* `message.content`

   The content of the JMS TextMessage.
	
   Default: `"Hello, World!"`

* `message.count`

   The number of JMS messages you want to produce and consume.

   Default: `1`
	

Deployment Note
---------------

By default, the JMS messaging provider is not deployed with the standalone JBoss AS 7 server. You will either need to run 
a domain server, or configure the standalone server for JMS message. Please refer to the JBoss AS 7 Documentation for further details.

To run the standalone server with JMS Messaging enabled

    standalone.sh -c standalone-full.xml


	


 
