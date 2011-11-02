POH5 Hello World Example
===================

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7* using the POH5 architecture.
POH5 is basically a smart, HTML5+CSS3+JavaScript front-end using RESTful services on the backend.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

To deploy to JBoss AS 7, start JBoss AS 7, and type `mvn package jboss-as:deploy`. 

The application is deployed to http://localhost:8080/poh5-helloworld

You can test the REST endpoint using the following URL
http://localhost:8080/poh5-helloworld/hello/json/David 

HelloWorld.java - establishes the RESTful endpoints using JAX-RS

Web.xml - maps RESTful endpoints to "/hello"
 
index.html - is a jQuery augmented plain old HTML5 web page
