jboss-as-ds-exception-handling: An example how to use DeltaSpike exception handling
====================================================================================
Author: Rafael Benevides
Level: Beginner
Technologies: CDI, JSF, DeltaSpike
Summary: Exception being handled by different handlers and purpose
Prerequisites: 
Target Product: 

What is it?
-----------

This project demonstrates DelstaSpike exception handling. Exception handling in DeltaSpike is based around the CDI eventing model.

The entire exception handling process starts with an event. This helps keep your application minimally coupled to
DeltaSpike, but also allows for further extension. Exception handling in DeltaSpike is all about letting you take care of 
exceptions the way that makes the most sense for your application. Events provide this delicate balance. Firing the event
is the main way of starting the exception handling process.


The project can throw two Exceptions: MyException and MyOtherException. And there are 3 different handlers:   
 - FacesMessageExceptionHandler  - Shows every Exception using FacesMessage
 - LogExceptionHandler - Log every Exception
 - MyExceptionCountHandler - Only Count how many times MyException was throw.

The MyExceptionCountHandler is also used as a Named CDI bean.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-as-ds-exception-handling.war` to the running instance of the server.
 

Access the application 
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-as-ds-exception-handling>

You will be presented to simple for with two buttons the permits to throw two exceptions: MyException and MyOtherException.

Click on Each Button and realize that the a message will be presented showing the exception message followed by the number of times that the service was invoked.

Check the logs and see the that the exception message is also being printed.

Realize that the counter about MyException is only incremented when MyException is throw. 

Undeploy the Archive
--------------------

Contributor: For example: 

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

Contributor: For example: 

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
