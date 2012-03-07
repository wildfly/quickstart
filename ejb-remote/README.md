ejb-remote: Remote EJB Client Example
=====================================
Authors: Jaikiran Pai and Mike Musgrove

What is it?
-----------

This example shows how to access an EJB from a remote Java client program. It
demonstrates the use of *EJB 3.1* and *JNDI* in *JBoss AS 7*.

There are two parts to the example: a server side component and a remote client program
that accesses it. Each part is in its own standalone Maven module, however the quickstart
does provide a top level module to simplify the packaging of the artifacts.

The server component is comprised of a stateful and a stateless EJB. It provides both an EJB JAR
that is deployed to the server and a JAR file containing the remote business interfaces required
by the remote client application.

The remote client application depends on the remote business interfaces from the server component.
This program looks up the stateless and stateful beans via JNDI and invokes a number of methods on
them.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

Building and deploying the application
-------------------------

Follow these steps to build, deploy and run the quickstart.

1. Start JBoss AS 7 (or EAP 6):

        $JBOSS_HOME/bin/standalone.sh

2. Build and install the server side component.  

         cd server-side/
        mvn clean install

  This will build the EJB and client interfaces JARs, and install them in your local Maven repository.

3. Deploy the EJB JAR to your server

        mvn jboss-as:deploy

  This maven goal will deploy `server-side/target/jboss-as-ejb-remote-app.jar`. You can check the AS
console to see information messages regarding the deployment.

4. Build and run the client application

         cd ../client
         mvn clean compile
         mvn exec:exec
   
   This will compile and execute the client application within Maven.  Refer to `The Output` below.
  
Note that you can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

The Output
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

Undeploy the Application
-------------------------

To undeploy the server side component from JBoss AS:

        cd ../server-side
        mvn jboss-as:undeploy


Build Executable JAR
-------------------------

The remote client application can also be built as a standalone executable JAR with all of it's 
dependencies.

      cd client
      mvn clean assembly:assembly
      
You can then run the executable JAR using `java -jar`:
      
      java -jar target/jboss-as-quickstarts-ejb-remote-client-7.0.2-SNAPSHOT-jar-with-dependencies.jar


Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

