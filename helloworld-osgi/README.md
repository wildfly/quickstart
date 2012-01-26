helloworld-osgi: Helloworld Using OSGi (Open Services Gateway initiative)
=========================================================================
Author: Pete Muir

What is it?
-----------

This example demonstrates the use of *OSGi* in JBoss AS 7.

System requirements
-------------------

The example can be deployed using Maven from the command line, by using the AS 7 
web console or by dropping the OSGi bundle in the deployments folder.

To set up Maven or JBoss Tools in Eclipse, refer to the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

Deploying the application
-------------------------

To deploy to the application to JBoss AS 7 using Maven, start JBoss AS 7 and type:
 
    mvn package jboss-as:deploy


This will build, deploy and start the OSGi bundle. You will see a 
  `Hello AS7 World!!`
message appear on the console when this is done.

You can read more details in the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.
