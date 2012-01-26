helloworld: Helloworld Example
===============================
Author: Pete Muir

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *Servlet 3* in JBoss AS 7 or JBoss Enterprise Application Platform 6.

System requirements
-------------------

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

Deploying the application
-------------------------

To deploy the application to JBoss AS 7 (or JBoss Enterprise Application Platform 6) using Maven, start the JBoss application server and type

    mvn package jboss-as:deploy

The application is deployed to <http://localhost:8080/jboss-as-helloworld>. 

You can read more details in the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.