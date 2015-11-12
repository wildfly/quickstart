ejb-remote: Remote EJB Client Example
=====================================
Author: Jaikiran Pai, Mike Musgrove  
Level: Intermediate  
Technologies: EJB, JNDI  
Summary: The `ejb-remote` quickstart uses *EJB* and *JNDI* to demonstrate how to access an EJB, deployed to WildFly, from a remote Java client application.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `ejb-remote` quickstart shows how to access an EJB from a remote Java client application. It demonstrates the use of *EJB* and *JNDI* in Red Hat JBoss Enterprise Application Platform.

There are two components to this example: 

1. A server side component:

    The server component is comprised of a stateful EJB and a stateless EJB. It provides both an EJB JAR that is deployed to the server and a JAR file containing the remote business interfaces required by the remote client application.
2. A remote client application that accesses the server component. 

    The remote client application depends on the remote business interfaces from the server component. This application looks up the stateless and stateful beans via JNDI and invokes a number of methods on them.

Each component is defined in its own standalone Maven module. The quickstart provides a top level Maven module to simplify the packaging of the artifacts.


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

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.

1. Make sure you have started the WildFly server. See the instructions in the previous section.
2. Open a command prompt and navigate to the ejb-remote quickstart directory
3. Build and install the server side component:
    * Navigate to the server-side subdirectory:

            cd server-side
    * Build the EJB and client interfaces JARs and install them in your local Maven repository.

            mvn clean install        
    * Deploy the EJB JAR to your server. This Maven goal will deploy `server-side/target/wildfly-ejb-remote-server-side.jar`. You can check the WildFly server console to see information messages regarding the deployment.

            mvn wildfly:deploy
4. Build and run the client application
    * Navigate to the client subdirectory:

            cd ../client
    * Compile the client code

            mvn clean compile
    * Execute the client application within Maven

            mvn exec:exec


Investigate the Console Output
-------------------------

When the client application runs, it performs the following steps:

1. Obtains a stateless session bean instance.
2. Sends method invocations to the stateless bean to add two numbers, and then displays the result.
3. Sends a second invocation to the stateless bean subtract two numbers, and then displays the result.
4. Obtains a stateful session bean instance.
5. Sends several method invocations to the stateful bean to increment a field in the bean, displaying the result each time.
6. Sends several method invocations to the stateful bean to decrement a field in the bean, displaying the result each time.

The output in the terminal window  will look like the following:

      Obtained a remote stateless calculator for invocation
      Adding 204 and 340 via the remote stateless calculator deployed on the server
      Remote calculator returned sum = 544
      Subtracting 2332 from 3434 via the remote stateless calculator deployed on the server
      Remote calculator returned difference = 1102
      Obtained a remote stateful counter for invocation
      Counter will now be incremented 5 times
      Incrementing counter
      Count after increment is 1
      Incrementing counter
      Count after increment is 2
      Incrementing counter
      Count after increment is 3
      Incrementing counter
      Count after increment is 4
      Incrementing counter
      Count after increment is 5
      Counter will now be decremented 5 times
      Decrementing counter
      Count after decrement is 4
      Decrementing counter
      Count after decrement is 3
      Decrementing counter
      Count after decrement is 2
      Decrementing counter
      Count after decrement is 1
      Decrementing counter
      Count after decrement is 0

Logging statements have been removed from this output here to make it clearer.


Build and Run The Quickstart as an Executable JAR
-------------------------

The remote client application can also be built as a standalone executable JAR with all of its dependencies.

1. Open a command prompt and navigate to the ejb-remote/client quickstart directory

        cd client
2. Type the following in the command line:

        mvn clean assembly:assembly
      
4. You can then run the executable JAR using the `java -jar` command. You will see the same console output as above.
      
        java -jar target/wildfly-ejb-remote-client-jar-with-dependencies.jar


Undeploy the Archive
--------------------

To undeploy the server side component from the WildFly server:

1. Navigate to the server-side subdirectory:

        cd ../server-side
2. Type the following command:

        mvn wildfly:undeploy



Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Install the required Maven artifacts and deploy the server side of the quickstart project.
   * Right-click on the `jboss-ejb-remote-server-side` project and choose `Run As` --> `Maven Install`.
   * Right-click on the `jboss-ejb-remote-server-side` project and choose `Run As` --> `Run on Server`.

2. Build and run the client side of the quickstart project.
   * Right-click on the `jboss-ejb-remote-client` project and choose `Run As` --> `Java Application`. 
   * In the `Select Java Application` window, choose `RemoteEJBClient - org.jboss.as.quickstarts.ejb.remote.client` and click `OK`.
   * The client output displays in the `Console` window.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

