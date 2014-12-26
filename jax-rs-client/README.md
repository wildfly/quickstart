jax-rs-client: External JAX-RS Client
======================
Author: Blaine Mincey
Level: Intermediate
Technologies: JAX-RS 2.0
Summary: Demonstrates the usage of JAX-RS 2.0 client, to interact with a remote JAX-RS 2.0 REST Service
Prerequisites: helloworld-rs
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This example demonstrates an external JAX-RS 2.0 client which interacts with a JAX-RS 2.0 Rest service
in *JBoss WildFly*.

This client "calls" the HelloWorld JAX-RS Web Service that was created in the `helloworld-rs` quickstart. See the **Prerequisite** section below for details on how to build and deploy the `helloworld-rs` quickstart.


System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Prerequisites
-----------

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs README](../helloworld-rs/README.md) file for details on how to deploy it.

You can verify the deployment of the `helloworld-rs` quickstart by accessing the following resource at <http://localhost:8080/wildfly-helloworld-rs/rest/>.

Run the Tests
-------------

This quickstart provides tests.

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command line and navigate to the root directory of this quickstart.
4. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Dclient


Investigate the Console Output
----------------------------

This command will compile the example and execute a test to make two separate requests to the Web Service.
Towards the end of the Maven build output, you should see the following if the execution is successful:

    URL: http://localhost:8080/wildfly-helloworld-rs/rest/xml
    MediaType: application/xml
    <xml><result>Hello World!</result></xml>

    URL: http://localhost:8080/wildfly-helloworld-rs/rest/json
    MediaType: application/json
    {"result":"Hello World!"}