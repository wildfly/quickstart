# servlet-async: How to Write an Asynchronous Servlet

Author: Christian Sadilek  
Level: Intermediate  
Technologies: Asynchronous Servlet, CDI, EJB  
Summary: The `servlet-async` quickstart demonstrates how to use asynchronous servlets to detach long-running tasks and free up the request processing thread.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `servlet-async` quickstart is a sample project showing the use of asynchronous servlets in ${product.name.full}.

It shows how to detach the execution of a long-running task from the request processing thread, so the thread is free
to serve other client requests. The long-running tasks are executed using a dedicated thread pool and create the
client response asynchronously.

A long-running task in this context does not refer to a computation intensive task executed on the same machine but
could for example be contacting a third-party service that has limited resources or only allows for a limited number
of concurrent connections. Moving the calls to this service into a separate and smaller sized thread pool ensures
that less threads will be busy interacting with the long-running service and that more requests can be served that do
not depend on this service.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the Server

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

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.


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
