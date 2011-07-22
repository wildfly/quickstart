Helloworld OSGi Example
=======================

This example demonstrates the use of *OSGi* in JBoss AS 7.

The example can be deployed using Maven from the command line, by using the AS 7 
web console or by dropping the OSGi bundle in the deployments folder.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7 using Maven, start JBoss AS 7 and type 
  `mvn package jboss-as:deploy`.
This will build, deploy and start the OSGi bundle. You will see a 
  `Hello AS7 World!!`
message appear on the console when this is done.

You can read more details in the _Getting Started Guide_.
