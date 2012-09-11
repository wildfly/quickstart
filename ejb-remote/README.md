ejb-remote: Remote EJB Client Example
=====================================
Authors: Jaikiran Pai, Mike Musgrove
Level: Intermediate
Technologies: EJB
Summary: Shows how to access an EJB from a remote Java client program using JNDI
Target Product: EAP

What is it?
-----------

This example shows how to access an EJB from a remote Java client application. It demonstrates the use of *EJB 3.1* and *JNDI* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

There are two components to this example: 

1. A server side component:

    The server component is comprised of a stateful EJB and a stateless EJB. It provides both an EJB JAR that is deployed to the server and a JAR file containing the remote business interfaces required by the remote client application.
2. A remote client application that accesses the server component. 

    The remote client application depends on the remote business interfaces from the server component. This application looks up the stateless and stateful beans via JNDI and invokes a number of methods on them.

Each component is defined in its own standalone Maven module. The quickstart provides a top level Maven module to simplify the packaging of the artifacts.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.

1. Make sure you have started the JBoss server. See the instructions in the previous section.
2. Open a command line and navigate to the ejb-remote quickstart directory
3. Build and install the server side component:
    * Navigate to the server-side subdirectory:

        cd server-side
    * Build the EJB and client interfaces JARs and install them in your local Maven repository.

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean install -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean install        
    * Deploy the EJB JAR to your server. This maven goal will deploy `server-side/target/jboss-as-ejb-remote-app.jar`. You can check the JBoss server console to see information messages regarding the deployment.

            mvn jboss-as:deploy
4. Build and run the client application
    * Navigate to the server-side subdirectory:

            cd ../client
    * Compile the client code

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean compile -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

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

1. Open a command line and navigate to the ejb-remote/client quickstart directory

        cd client
2. Type the following in the command line:

        mvn clean assembly:assembly
      
4. You can then run the executable JAR using `java -jar`:
      
        java -jar target/jboss-as-quickstarts-ejb-remote-client-7.0.2-SNAPSHOT-jar-with-dependencies.jar


Undeploy the Archive
--------------------

To undeploy the server side component from the JBoss server:

1. Navigate to the server-side subdirectory:

        cd ../server-side
2. Type the following command:

        mvn jboss-as:undeploy



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
