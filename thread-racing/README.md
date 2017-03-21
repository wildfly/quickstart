# thread-racing: A Java EE thread racing web application

Author: Eduardo Martins  
Level: Beginner  
Technologies: Batch, CDI, EE Concurrency, JAX-RS, JMS, JPA, JSON, Web Sockets  
Summary: A thread racing web application that demonstrates technologies introduced or updated in the latest Java EE specification.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `thread-racing` quickstart is a web application that demonstrates new and updated technologies introduced by the Java EE 7 specification through simple use cases.

The web application allows the user to trigger a race between 4 threads and follow, in real time, the progress of each thread until the race ends.

The race itself consists of multiple stages, each demonstrating the usage of a specific new or updated Java EE 7 technology:

 * Batch 1.0
 * EE Concurrency 1.0
 * JAX-RS 2.0
 * JMS 2.0
 * JSON 1.0

WebSockets 1.0 is one of the most relevant new technologies introduced by Java EE 7. Instead of being used in a race stage, a WebSockets 1.0 ServerEndpoint provides the remote application interface.
A new race is run when a client establishes a session. That session is then used to update the client in real time, with respect to the race progress and results. The `src/main/java/org/jboss/as/quickstarts/threadracing/WebSocketRace.java` file is the WebSocket server endpoint class and is a good entry point when studying how the application code works.

JPA 2.1 is also present in the application code. Specifically it is used to store race results in the default data source instance, which is also new to Java EE. Further details are included in the `src/main/java/org/jboss/as/quickstarts/threadracing/results/RaceResults.java` class.

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the ${product.name} Server with the Full Profile

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml
        For Windows: ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

To start a race press the `Insert Coin` button. The page displays the names of the threads as they join the race. It then tracks the progress of each thread through the Batch, EE Concurrency, JAX-RS, JMS, and JSON stages of the race. Finally, it displays the official race results and championship standings.


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warning in the server log. You can ignore this warning.

    HHH000431: Unable to determine H2 database version, certain features may not work


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

_NOTE:_ Within JBoss Developer Studio, be sure to define a server runtime environment that uses the `standalone-full.xml` configuration file.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
