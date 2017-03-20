# websocket-hello: A simple WebSocket application

Author: Sande Gilda, Emmanuel Hugonett   
Level: Beginner  
Technologies: WebSocket, CDI, JSF  
Summary: The `websocket-hello` quickstart demonstrates how to create a simple WebSocket application.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `websocket-hello` quickstart demonstrates how to create a simple WebSocket-enabled application in ${product.name.full}. It consists of the following:

* A JavaScript enabled WebSocket HTML client.
* A WebSocket server endpoint that uses annotations to interact with the WebSocket events.
* A `jboss-web.xml` file configured to enable WebSockets

WebSockets are a requirement of the Java EE 7 specification and are implemented in ${product.name} ${product.version}. They are configured in the `undertow` subsystem of the server configuration file. This quickstart uses the WebSocket default settings, so it is not necessary to modify the server configuration to deploy and run this quickstart.

_Note: This quickstart demonstrates only a few of the basic functions. A fully functional application should provide better error handling and intercept and handle additional events._

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the ${product.name} Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

1. Click on the `Open Connection` button to create the WebSocket connection and display current status of `Open`.
2. Type a name and click `Say Hello` to create and send the `Say hello to <NAME>` message. The message appears in the server log and a response is sent to the client.
3. Click on the `Close Connection` button to close the WebSocket connection and display the current status of `Closed`.
4. If you attempt to send another message after closing the connection, the following message appears on the page:

        WebSocket connection is not established. Please click the Open Connection button.


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
