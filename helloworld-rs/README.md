helloworld-rs: Helloworld Using JAX-RS (Java API for RESTful Web Services)
==========================================================================
Authors: Gustavo A. Brey and Gaston Coco 

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7* or *JBoss Enterprise Application Platform 6*.

System requirements
-------------------

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

Deploying the application
-------------------------

To deploy to JBoss AS 7 or JBoss Enterprise Application Platform 6, start the JBoss application server and type:

    mvn package jboss-as:deploy 

The application is deployed to <http://localhost:8080/jboss-as-helloworld-rs>, so in order to get either both content you will have to point

* <http://localhost:8080/jboss-as-helloworld-rs/xml> if you want *xml* or
* <http://localhost:8080/jboss-as-helloworld-rs/json> if you want *json*

