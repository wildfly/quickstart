ejb-asynchronous:  An application with an EJB with asynchronous methods to demonstrate how it works
======================================================
Author: Wolf-Dieter Fink

What is it?
-----------

This is an example to demonstrate the behaviour of asynchronous EJB invocations.
The methods are invoked by an other EJB inside the server and by a remote client.
Also an example is included that shows the error handling if the asynchronous method fails.

The example is composed of 2 maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB's and will be deployed to the server
2. `client` : This project contains a remote ejb client to show the behaviour

The root `pom.xml` builds each of the subprojects in the above order and deploys the archive to the server.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

Start a server instance

    bin/standalone.sh

Build and Deploy the Quickstart
-------------------------

If the quickstart is built and deployed using the standard Maven commands, "mvn clean install" and "mvn jboss-as:deploy", copy the following:

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:
        mvn clean install jboss-as:deploy
4. This will deploy `service/target/jboss-as-ejb-asynchronous.jar` to the running instance of the server.
 
Check whether the application is deployed successfully.


Access the application
---------------------

1. Open a command line and navigate to the root directory of this quickstart.
2. Type this command to start the client
        cd client
        mvn exec:exec
3. check the client output
       INFO: See server log around [a date] whether the call finished
       INFO: Get the async result as expected => returning at [a date] duration was 200ms
       INFO: Get the async result as expected after wait => returning at [a date] duration was 1500ms
       INFO: Results of the parallel (server) processing : [returning at [a date] duration was 5000ms, returning at [a date] duration was 3000ms]
       INFO: Catch the expected Exception of the asynchronous execution!
4. check the server log
       There should be two INFO log messages for the 'fireAndForget' invocation:
          'fireAndForget' Will wait for 15000ms
       and 15sec later (the client should be finished at this time)
          action 'fireAndForget' finished
_NOTE: In AS7.1.1.Final there is a bug that an ERROR will be logged that the result can not be written, it will be solved in a later version._



Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy -Ddeploy.hostname=localhost -Ddeploy.port=10099


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

------------------------------------
