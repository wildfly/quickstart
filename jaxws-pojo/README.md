# jaxws-pojo: An POJO JAX-WS Web Service

Author: R Searls  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-pojo` quickstart is a working example of the web service endpoint created from a POJO.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

## What is it?

The `jaxws-pojo` quickstart demonstrates the use of *JAX-WS* in WildFly Application Server as a simple POJO web service application.

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

4. This will deploy `service/target/jaxws-pojo-service.war` to the running instance of the server.

## Access the Application

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jaxws-pojo/JSEBean?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

## Run the Client
1. Make sure the service deployed properly.
2. Open a command prompt and navigate into the client directory of this quickstart.

        cd client/

3. Type this command to run the client.

        mvn exec:java        
    __Note__: This quickstart requires `quickstart-parent` artifact to be installed in your local Maven repository.
    To install it, navigate to quickstarts project root directory and run the following command:

        mvn clean install


4. You should see the following response.

        JSEBean pojo: pojoClient calling


## Undeploy the Archive

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

For this quickstart, follow the special instructions to build [Quickstarts Containing an EAR](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#deploy-and-undeploy-a-quickstart-containing-server-and-java-client-projects)

1. Right-click on the `jaxws-pojo-service` subproject, and choose `Run As` --> `Run on Server`.
2. Choose the server and click `Finish`.
3. This starts the server, deploys the application, and opens a browser window that accesses the running application.
4. To undeploy the project, right-click on the `jaxws-pojo-ear` project and choose `Run As` --> `Maven build`. Enter `wildfly:undeploy` for the `Goals` and click `Run`.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
