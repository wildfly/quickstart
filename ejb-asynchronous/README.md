# ejb-asynchronous: EJB with asynchronous methods

Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: Asynchronous EJB  
Summary: The `ejb-asynchronous` quickstart demonstrates the behavior of asynchronous EJB invocations by a deployed EJB and a remote client and how to handle errors.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-asynchronous` quickstart demonstrates the behavior of asynchronous EJB invocations in ${product.name.full}. The methods are invoked by both an EJB in the deployment and by a remote client. The quickstart also shows error handling if the asynchronous method invocation fails.

The example is composed of 2 Maven modules, each with a shared parent. The modules are as follows:

1. `ejb`: This module contains the EJB's and will be deployed to the server
2. `client` : This module contains a remote EJB client

The root `pom.xml` builds each of the submodules in the above order and deploys the archive to the server.


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
4. This will deploy `ejb/target/${project.artifactId}-ejb.jar` to the running instance of the server.

Check whether the application is deployed successfully.


## Access the Application

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type this command to start the client

        cd client
        mvn exec:exec

    __Note__: This quickstart requires `quickstart-parent` artifact to be installed in your local Maven repository.
    To install it, navigate to quickstarts project root directory and run the following command:

        mvn clean install

3. Check the client output

        INFO: The server log should contain a message at (about) <date>, indicating that the call to the asynchronous bean completed.
        INFO: Got the async result as expected => returning at <date>, duration was 200ms
        INFO: Got the async result as expected after wait => returning at <date>, duration was 1500ms
        INFO: Catch the expected Exception of the asynchronous execution!
        INFO: Results of the parallel (server) processing : [returning at <date> duration was 5000ms, returning at <date>, duration was 3000ms]

4. Check the server log.

    There should be two INFO log messages for the `fireAndForget` invocation:

          'fireAndForget' Will wait for 15000ms

    and 15sec later (the client should be finished at this time)

          action 'fireAndForget' finished


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Install the required Maven artifacts and deploy the asynchronous EJB quickstart project.
   * Right-click on the `${project.artifactId}-ejb` project and choose `Run As` --> `Maven Install`.
   * Right-click on the `${project.artifactId}-ejb` project and choose `Run As` --> `Run on Server`.

2. Build and run the client side of the quickstart project.
   * Right-click on the `${project.artifactId}-client` project and choose `Run As` --> `Java Application`.
   * In the `Select Java Application` window, choose `AsynchronousClient - org.jboss.as.quickstarts.ejb.asynchronous.client` and click `OK`.
   * The client output displays in the `Console` window.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
