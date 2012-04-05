jax-rs-client: External JAX-RS Client
======================

Author: Blaine Mincey, blaine.mincey@redhat.com

What is it?
-----------

This example demonstrates an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses *CDI 1.0* and *JAX-RS* 
in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.  

This client "calls" the HelloWorld JAX-RS Web Service that was created in the `helloworld-rs` quickstart. See the **Prerequisite** section below for details on how to build and deploy the `helloworld-rs` quickstart.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Prerequisites
-----------

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs README](../hellworld-rs/README.html) file for details on how to deploy it.

You can verify the deployment of the `helloworld-rs` quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-as-helloworld-rs/xml> 
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-as-helloworld-rs/json>



Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.html/#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command line and navigate to the root directory of this quickstart.
4. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Investigate the Console Output
----------------------------

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following if the execution is successful:

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

