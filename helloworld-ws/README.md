# helloworld-ws: Hello World JAX-WS Web Service

Author: Lee Newson  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `helloworld-ws` quickstart demonstrates a simple Hello World application, bundled and deployed as a WAR, that uses *JAX-WS* to say Hello.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

## What is it?

The `helloworld-ws` quickstart demonstrates the use of *JAX-WS* in WildFly Application Server as a simple Hello World application.

## System Requirements

The application this project produces is designed to be run on WildFly Application Server 11 or later.

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.3.1 or later. See [Configure Maven for WildFly 11](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of WILDFLY_HOME

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_WILDFLY_HOME.md#use-of-eap_home-and-jboss_home-variables).


## Start the Server

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/helloworld-ws.war` to the running instance of the server.
5. Review the server log to see useful information about the deployed web service endpoint.

        JBWS024061: Adding service endpoint metadata: id=org.jboss.as.quickstarts.wshelloworld.HelloWorldServiceImpl
         address=http://localhost:8080/helloworld-ws/HelloWorldService
         implementor=org.jboss.as.quickstarts.wshelloworld.HelloWorldServiceImpl
         serviceName={http://www.jboss.org/eap/quickstarts/wshelloworld/HelloWorld}HelloWorldService
         portName={http://www.jboss.org/eap/quickstarts/wshelloworld/HelloWorld}HelloWorld
         annotationWsdlLocation=null
         wsdlLocationOverride=null
         mtomEnabled=false


## Access the Application

You can verify that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/helloworld-ws/HelloWorldService?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.


## Undeploy the Archive

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Client Tests Using Arquillian

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the WildFly server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).

## Investigate the Console Output

The following expected output should appear. The output shows what was said to the Web Service by the client and the responses it received.

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstarts.wshelloworld.ClientArqTest
    [Client] Requesting the WebService to say Hello.
    [WebService] Hello World!
    [Client] Requesting the WebService to say Hello to John.
    [WebService] Hello John!
    [Client] Requesting the WebService to say Hello to John, Mary and Mark.
    [WebService] Hello John, Mary & Mark!
    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.988 sec


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

When you deploy this quickstart, you are presented with a window that explains there is no user interface for this quickstart and directs you to click on a link to view the WSDL definition. However, the Eclipse browser does not support the display of WSDL definitions. Instead, open an external browser and access the following URL: <http://localhost:8080/helloworld-ws/HelloWorldService?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
