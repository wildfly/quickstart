# websocket-endpoint: Web application using WebSockets and JSON-P

Author: Rafael Benevides  
Level: Beginner  
Technologies: CDI, WebSocket, JSON-P  
Summary: Shows how to use WebSockets with JSON to broadcast information to all open WebSocket sessions in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>   


## What is it?

The `websocket-endpoint` quickstart demonstrates how to use Java API for WebSockets to create server endpoints in ${product.name.full}.

The `BidWebSocketEndpoint` provides the WebSocket endpoint that receives `Message` instances from clients/browsers and replies with the current `Bidding` instance. The conversion from JSON content to the specific instances are made by `MessageDecoder` and `BiddingEncode` classes.

Every update made on the `Bidding` are immediately propagated to all opened WebSocket sessions without any browser submission or AJAX polling mechanism.


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



## Access the application

Access the running application in a browser at the following URL:  <http://localhost:8080/${project.artifactId}/>

You are presented with a simple form that shows a bidding with the status `NOT_STARTED`.

Click on `Do a bid!` button. That will start the bidding and trigger the 1 minute countdown timer. You can also notice that every Bid will be listed under the `List of bids` section.

You should open the application URL in other browsers or tabs. You will notice that every change on the bidding is automatically update in all opened browser or tabs. The item will be SOLD once that it reaches the `Buy now price`. At the countdown timeout, the bidding will be EXPIRED. You can click on `Buy it now` button to immediately buy the item.

You can restart the bidding if you click on `Reset bidding` button.


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


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
