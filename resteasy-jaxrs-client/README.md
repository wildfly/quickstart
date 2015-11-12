resteasy-jaxrs-client: External JAX-RS Client
======================
Author: Blaine Mincey  
Level: Intermediate  
Technologies: JAX-RS, CDI  
Summary: The `resteasy-jaxrs-client` quickstart demonstrates an external JAX-RS RestEasy client, which interacts with a JAX-RS Web service that uses *CDI* and *JAX-RS*.  
Prerequisites: helloworld-rs  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `resteasy-jaxrs-client` quickstart demonstrates an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses *CDI* and *JAX-RS* 
in Red Hat JBoss Enterprise Application Platform.

This client "calls" the HelloWorld JAX-RS Web Service that was created in the [helloworld-rs](../helloworld-rs/README.md) quickstart. See the **Prerequisite** section below for details on how to build and deploy the [helloworld-rs](../helloworld-rs/README.md) quickstart.


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Prerequisites
-----------

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs](../helloworld-rs/README.md)  README file for details on how to deploy it.

You can verify the deployment of the [helloworld-rs](../helloworld-rs/README.md) quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-helloworld-rs/rest/xml> 
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-helloworld-rs/rest/json>



Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. 

1. Make sure you have started the WildFly server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command prompt and navigate to the root directory of this quickstart.
4. Type the following command to run the test goal with the following profile activated:

        mvn clean test 


Investigate the Console Output
----------------------------

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following if the execution is successful:

        ===============================================
        URL: http://localhost:8080/jboss-helloworld-rs/rest/xml
        MediaType: application/xml

        *** Response from Server ***

        <xml><result>Hello World!</result></xml>
    
        ===============================================
        ===============================================
        URL: http://localhost:8080/jboss-helloworld-rs/rest/json
        MediaType: application/json

        *** Response from Server ***

        {"result":"Hello World!"}
        ===============================================


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->

