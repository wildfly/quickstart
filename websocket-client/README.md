# websocket-client: WebSocket Java Client Example

Author: Paul Cowan  
Level: Intermediate  
Technologies: Web Socket, CDI Events, JSON, SSL  
Summary: Demonstrates use of a Javascript WebSocket client, WebSocket configuration, programmatic binding, and secure WebSocket.
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `websocket-client` quickstart demonstrates how to use the Java API for WebSockets to create Java client endpoint connected to a remote WebSocket server.

The example is modeled as a relay between a *frontend* WebSocket server endpoint and a *backend* WebSocket client endpoint.

Message Flow:  
`(Browser Javascript WebSocket Client) <-> (${product.name} WebSocket Server Endpoint) <-> (${product.name} Websocket Client Endpoint) <-> (Remote WebSocket Echo Server)`

CDI events are used to pass messages between the frontend and backend. A single backend WebSocket client endpoint is shared for all frontend clients.

The remote WebSocket server must be an *Echo* server; a simple WebSocket server that echos back messages the client sends for the purpose of testing.  Such a server is publicly available at ws://echo.websocket.org, but any echo server will work.

`Frontend` does not use WebSocket annotations because it demonstrates how to use `ServerEndpointConfig` to modify the default `Configurator` to use an application scoped Endpoint, and how to deploy the Endpoint programatically.

`Backend` does not use WebSocket annotations because it demonstrates how to use `ClientEndpointConfig` to configure the WebSocket client to connect to a secure (wss) WebSocket.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the Server

1. Open a command line and navigate to the root of the  ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

Access the running application in a browser at the following URL:  <http://localhost:8080/${project.artifactId}/>

You are presented with the `WebSocket Echo Replay` page confirming the connection to the remote WebSocket *Echo* server.

    Connecting to ws://localhost:8080/${project.artifactId}/relay
    RECV: Opened frontend session FRONTEND_SESSION_ID

Type a message in the text input field at the bottom of the page and click `Send`. The message is processed and the form displays the relayed results. The message `This is a test` was used in the following example.

    SEND: This is a test
    RECV: BROADCAST: Connecting to backend wss://echo.websocket.org
    RECV: BROADCAST: Opened backend session BACKEND_SESSION_ID
    RECV: Sending message from frontend session FRONTEND_SESSION_ID
    RECV: Received message from backend session BACKEND_SESSION_ID
    RECV: This is a test


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
