# resteasy-jaxrs-client: External JAX-RS Client

Author: Blaine Mincey  
Level: Intermediate  
Technologies: JAX-RS, CDI  
Summary: The `resteasy-jaxrs-client` quickstart demonstrates an external JAX-RS RestEasy client, which interacts with a JAX-RS Web service that uses *CDI* and *JAX-RS*.  
Prerequisites: helloworld-rs  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `resteasy-jaxrs-client` quickstart demonstrates an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses *CDI* and *JAX-RS*
in ${product.name.full}.

This client "calls" the HelloWorld JAX-RS Web Service that was created in the [helloworld-rs](../helloworld-rs/README.md) quickstart. See the **Prerequisite** section below for details on how to build and deploy the [helloworld-rs](../helloworld-rs/README.md) quickstart.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Prerequisites

IMPORTANT: This quickstart depends on the deployment of the `helloworld-rs` quickstart for its test. Before running this quickstart, see the [helloworld-rs](../helloworld-rs/README.md)  README file for details on how to deploy it.

You can verify the deployment of the [helloworld-rs](../helloworld-rs/README.md) quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/${project.artifactId}/rest/xml>
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/${project.artifactId}/rest/json>



## Run the Client

1. Make sure you have started the ${product.name} server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command prompt and navigate to the root directory of this quickstart.
4. Type the following command to run the client:

        mvn clean package exec:java

## Investigate the Console Output

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you
should see the following if the execution is successful:

        ===============================================
        URL: http://localhost:8080/${project.artifactId}/rest/xml
        MediaType: application/xml

        *** Response from Server ***

        <xml><result>Hello World!</result></xml>

        ===============================================
        ===============================================
        URL: http://localhost:8080/${project.artifactId}/rest/json
        MediaType: application/json

        *** Response from Server ***

        {"result":"Hello World!"}
        ===============================================

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

1. Before you run this quickstart, be sure to import, deploy, and test the `helloworld-rs` quickstart as described in the [Prerequisites](#prerequisites) section of this file.

2. Import this quickstart into JBoss Developer Studio.

3. Build and run the quickstart project.
    * Right-click on the `${project.artifactId}` project and choose `Run As` --> `Maven build`.
    * Enter `clean package exec:java` for the `Goals:` and click `Run`.
    * The client output displays in the `Console` window.
