JAX-RS Hello World Example
===================

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7*.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package jboss-as:deploy`. 

The application is deployed to <http://localhost:8080/rs-helloworld>, so in order to get either both content you will have to point

* <http://localhost:8080/rs-helloworld/xml> if you want *xml* or
* <http://localhost:8080/rs-helloworld/json> if you want *json*

You can read more details in the _Getting Started Guide_.