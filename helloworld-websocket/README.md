# helloworld-websocket: WebSocket Hello World Example

Author: Jeff Mesnil  
Level: Beginner  
Technologies: Web Socket  
Summary: The `helloworld-websocket` quickstart demonstrates the use of Web Socket server endpoints.
Target Product: ${product.name}  
Source: <${github.repo.url}>    

## What is it?

This example demonstrates the use of *Web Socket* in *${product.name}*.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

## System Requirements

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

The application this project produces is designed to be run on ${product.name.full} ${product.version}.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are required.

With the prerequisites out of the way, you're ready to build and deploy.

## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. To deploy the application, you first need to produce the archive to deploy using the following Maven goal:

        mvn clean package

3. Type this command to deploy the archive:

        mvn wildfly:deploy

   This deploys both the client and service applications.

## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

## Undeploy the Archive

To undeploy run this command:

      mvn wildfly:undeploy

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
