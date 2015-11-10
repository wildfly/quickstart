websocket-client: WebSocket Client Example
===================
Author: Paul Cowan  
Level: Intermediate  
Technologies: Web Socket, CDI Events, JSON, SSL  
Summary: Demonstrates use of a Java (not Javascript) WebSocket client, WebSocket configuration, programmatic binding, and secure WebSocket.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `websocket-client` quickstart demonstrates how to use the Java API for WebSockets to create Java client endpoint connected to a remote WebSocket server.

The example is modeled as a relay between a *frontend* WebSocket server endpoint and a *backend* WebSocket client endpoint.

Message Flow:  
`(Browser Javascript WebSocket Client) <-> (WildFly WebSocket Server Endpoint) <-> (WildFly Websocket Client Endpoint) <-> (Remote WebSocket Echo Server)`

CDI events are used to pass messages between the frontend and backend. A single backend WebSocket client endpoint is shared for all frontend clients.

The remote WebSocket server must be an *Echo* server; a simple WebSocket server that echos back messages the client sends for the purpose of testing.  Such a server is publicly available at ws://echo.websocket.org, but any echo server will work.

`Frontend` does not use WebSocket annotations because it demonstrates how to use `ServerEndpointConfig` to modify the default `Configurator` to use an application scoped Endpoint, and how to deploy the Endpoint programatically.

`Backend` does not use WebSocket annotations because it demonstrates how to use `ClientEndpointConfig` to configure the WebSocket client to connect to a secure (wss) WebSocket. 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the  WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn package wildfly:deploy
4. This will deploy `target/wildfly-websocket-client.war` to the running instance of the server.
 


Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/wildfly-websocket-client/>


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
