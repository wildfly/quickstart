jboss-websocket-hello: A simple WebSocket application
========================
Author: Sande Gilda, Emmanuel Hugonett   
Level: Beginner  
Technologies: WebSocket, CDI, JSF  
Summary: The `websocket-hello` quickstart demonstrates how to create a simple WebSocket application.  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `websocket-hello` quickstart demonstrates how to create a simple WebSocket-enabled application in Red Hat JBoss Enterprise Application Platform. It consists of the following:

* A JavaScript enabled WebSocket HTML client.
* A WebSocket server endpoint that uses annotations to interact with the WebSocket events.
* A `jboss-web.xml` file configured to enable WebSockets

WebSockets are a requirement of the Java EE 7 specification and are implemented in WildFly 7. They are configured in the `undertow` subsystem of the server configuration file. This quickstart uses the WebSocket default settings, so it is not necessary to modify the server configuration to deploy and run this quickstart. 

_Note: This quickstart demonstrates only a few of the basic functions. A fully functional application should provide better error handling and intercept and handle additional events._

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-websocket-hello.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-websocket-hello>. 

1. Click on the `Open Connection` button to create the WebSocket connection and display current status of `Open`.
2. Type a name and click `Say Hello` to create and send the 'Say hello to `<NAME>`' message. The message appears in the server log and a response is sent to the client.
3. Click on the `Close Connection` button to close the WebSocket connection and display the current status of `Closed`.
4. If you attempt to send another message after closing the connection, the following message appears on the page: "WebSocket connection is not established. Please click the Open Connection button".


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------


You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 



Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

  
