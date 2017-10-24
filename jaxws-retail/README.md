# jaxws-retail: A Retail JAX-WS Web Service

Author: R Searls  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-retail` quickstart is a working example of a simple web service endpoint.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

## What is it?

The `jaxws-retail` quickstart demonstrates the use of *JAX-WS* in WildFly Application Server as a simple profile management application. It also demonstrates usage of wsconsume to generate classes from WSDL file.

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

4. This will deploy `service/target/jaxws-retail-service.war` to the running instance of the server.

_Note:_ You will see the following errors and warnings in the server log. These messages come from the `jaxws-tools-maven-plugin` plugin that generates source files based on the WSDL. You can ignore these warnings.

    [INFO] Could not find log4j.xml configuration, logging to console.
    [INFO]
    [INFO] TODO! Cheek SOAP 1.2 extension
    [ERROR] log4j:WARN No appenders could be found for logger (org.apache.cxf.common.logging.LogUtils).
    [ERROR] log4j:WARN Please initialize the log4j system properly.
    [ERROR] log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.

_Note:_ You may also see the following errors if your Linux environment defines a BASH_FUNC_scl() function. You can ignore these errors.

    [ERROR] /bin/sh: scl: line 1: syntax error: unexpected end of file
    [ERROR] /bin/sh: error importing function definition for `BASH_FUNC_scl`


## Access the Application

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jaxws-retail/ProfileMgmtService/ProfileMgmt?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

## Run the Client

1. Make sure the service deployed properly.

2. Open a command prompt and navigate into the client directory of this quickstart.

        cd client/
3. Type this command to run the client.

        mvn exec:java

    __Note__: This quickstart requires `quickstart-parent` artifact to be installed in your local Maven repository.
    To install it, navigate to quickstarts project root directory and run the following command:

        mvn clean install

4. You should see the following output in the client console.

        Jay Boss's discount is 10.00


## Undeploy the Archive

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts).

This quickstart is dependent on a WSDL file that is included in the `jaxws-retail-service` project, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

When you import this project into JBoss Developer Studio, you see 17 errors. These `Java Problems` are because these classes are not included in this project. Instead, they are defined in and generated from the `jaxws-retail-service/src/main/webapp/WEB-INF/wsdl/ProfileMgmtService.wsdl` WSDL file. You can ignore these errors.

This quickstart requires that you build the parent project, deploy the service, and then run the client.

1. To build the parent project, right-click on the `jaxws-retail` project and choose `Run As` --> `Maven install`.
2. To deploy the service:

    * Right-click on the `jaxws-retail-service` project and choose `Run As` --> `Maven build...`.
    * Enter `install` in the `Goals` field.
    * Select the `JRE` tab.
    * Make sure the Java runtime used is a JDK one.
    * Click `Run`.
    * In the `jaxws-retail-service` project, select the `target/generated-sources/wsconsume` folder and choose `Build Path` --> `Use a Source Folder`.
    * Right-click on the `jaxws-retail-service` project and choose `Run As` --> `Run on Server`.
    * Select the JBoss EAP server and click `Finish`.
    * You should see the following message in the `Console` tab:

        `WFLYSRV0010: Deployed "jaxws-retail-service.war"`

    * You also see the "404 - Not Found" error in the application window. This is because there is no user interface for this quickstart. You can ignore this error.

3. To run the application:

    * Right-click on the `jaxws-retail-client` project and choose `Run As` --> `Maven Build`.
    * Enter `exec:java` for the `Goals` and click `Run`.
    * Review the output in the console window. You should see the following message:

        `Jay Boss's discount is 10.00`
3. To undeploy the project, right-click on the `jaxws-retail-service` project and choose `Run As` --> `Maven build`. Enter `wildfly:undeploy` for the `Goals` and click `Run`.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources

_Note:_ You will see the following informational messages. This is because the source files for this JAR are not available in the Maven repository.

    [INFO] The following files have NOT been resolved:
    [INFO]    org.apache.ant:ant-launcher:jar:sources:1.7.0:provided
    [INFO]    com.sun:tools:jar:sources:1.6:system
    [INFO]    asm:asm:jar:sources:3.3.1:provided
