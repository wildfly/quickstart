External JAX-RS Client
======================


Author: Blaine Mincey, blaine.mincey@redhat.com

This example demonstrates the use an external JAX-RS RestEasy client
which interacts with a JAX-RS Web service that uses *CDI 1.0* and *JAX-RS* 
in *JBoss AS 7*.  Specifically, this client "calls" the HelloWorld JAX-RS
Web Service created in quickstart helloworld-rs.  Please refer to the helloworld-rs
README.md for instructions on how to build and deploy helloworld-rs.

Prerequisites
=============

Before running this quickstart, you must first build and deploy the helloworld-rs
quickstart to your AS7 server as this quickstart depends on that deployment for
its test.  Additionally, this example can be executed using Maven from the command line 
or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the _Getting Started Guide_.

Usage
=====
This example assumes that the following URLs are available based upon the 
HelloWorld REST application being deployed:

http://localhost:8080/jboss-as-helloworld-rs/xml

http://localhost:8080/jboss-as-helloworld-rs/json

Execute the example by running 'mvn test'

This command will compile the example and execute a test to make two separate
requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following for successful execution:

===============================================
URL: http://localhost:8080/jboss-as-helloworld-rs/xml
MediaType: application/xml

*** Response from Server ***

<xml><result>Hello World!</result></xml>

===============================================
===============================================
URL: http://localhost:8080/jboss-as-helloworld-rs/json
MediaType: application/json

*** Response from Server ***

{"result":"Hello World!"}

===============================================
