cdi-veto: An example of a Portable Extension and some of the APIs / SPIs of CDI to veto beans.
======================================================
Author: Jason Porter
Level: Intermediate
Technologies: CDI
Summary: Creating a basic CDI extension to demonstrate vetoing beans.
Target Product: EAP

What is it?
-----------

This project demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task. This particular extension explores the ProcessInjectionTarget and
InjectionTarget SPI classes of CDI to demonstrate removing a bean from CDI's knowledge and
correctly injecting JPA entities in your application.

A Portable Extension is a user extension to Java EE 6 or above which is tailored to a specific
use case which will run on any Java EE 6 or higher implementation. This may be something that the
specifications don't support just yet, but could be implemented via a portable extension such as
type-safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service provider configuration file,
and an Arquillian test to verify the extension is working correctly.

This quickstart does not contain any user interface. The tests must be run to verify everything is working
correctly.

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


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Investigate the Console Output
----------------------------

Run the tests via Maven (or your IDE) and examine the console output during the test phase.

You'll see four tests run:

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithVetoExtensionAndManagerTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.988 sec
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithVetoExtensionWithoutManagerTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.093 sec
    Running org.jboss.as.quickstart.cdi.veto.test.InjectionWithoutVetoExtensionWithManagerTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.492 sec

    Results :

    Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

In the server log you'll see a few lines similar to 

             INFO  [VetoExtension] (MSC service thread 1-8) Vetoed class class org.jboss.as.quickstart.cdi.veto.model.Car
             INFO  [CarManager] (http--127.0.0.1-8080-2) Returning new instance of Car

That will let you know the extension is working. To really see what's going on and understand this example, please read the source and the tests.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
