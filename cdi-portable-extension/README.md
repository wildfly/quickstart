cdi-portable-extension: An example of a Portable Extension and some of the APIs / SPIs of CDI
======================================================
Author: Jason Porter
Level: Intermediate
Technologies: CDI
Summary: Creating a basic CDI extension to provide injection of fields from an XML file.
Target Product: EAP

What is it?
-----------

This project demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task. This particular extension explores the ProcessInjectionTarget and 
InjectionTarget spi classes of CDI. To demonstrate a possible way to seed data into beans.

A Portable Extension is essentially an extension to Java EE 6+ which is tailored to a specific
use case which will run on any Java EE 6 or higher implementation. There may be something that the
specifications don't support just yet, but could be implemented via a portable extension such as
type safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service registration file
for that extension and an Arquillian test to verify the extension is working correctly.

It does not contain any user interface, the tests must be run to verify everything is working
correctly.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.


Configure Maven
---------------

Contributor: You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

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


Run tests from JBDS
-----------------------

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'arq-jbossas-managed' for running on
managed server or 'arq-jbossas-remote' for running on remote server.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
----------------------------


### Maven

Maven prints summary of performed tests into the console:

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.extension.test.CreatureExtensionTest
    log4j:WARN No appenders could be found for logger (org.jboss.logging).
    log4j:WARN Please initialize the log4j system properly.
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.87 sec

    Results :

    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0


### Server log

There are two logging statements done when the tests are run:

#### Example

    15:07:13,145 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-5) JBAS015876: Starting deployment of "test.war"
    15:07:13,267 INFO  [org.jboss.weld.deployer] (MSC service thread 1-1) JBAS016002: Processing weld deployment test.war
    15:07:13,283 INFO  [org.jboss.weld.deployer] (MSC service thread 1-7) JBAS016005: Starting Services for CDI deployment: test.war
    15:07:13,290 INFO  [org.jboss.weld.deployer] (MSC service thread 1-3) JBAS016008: Starting weld service for deployment test.war
    15:07:13,338 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    15:07:13,343 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter
    15:07:13,377 INFO  [org.jboss.web] (MSC service thread 1-1) JBAS018210: Registering web context: /test
    15:07:13,495 INFO  [org.jboss.as.server] (management-handler-thread - 5) JBAS018559: Deployed "test.war"
    15:07:13,922 INFO  [org.jboss.weld.deployer] (MSC service thread 1-8) JBAS016009: Stopping weld service for deployment test.war
    15:07:13,932 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) JBAS015877: Stopped deployment test.war in 16ms
    15:07:14,039 INFO  [org.jboss.as.repository] (management-handler-thread - 6) JBAS014901: Content removed from location /home/jporter/java_libraries/jboss-as/build/target/jboss-as-7.1.1.Final/standalone/data/content/4d/40e4e277a16327b45b62954d70d91bbf3fcf42/content
    15:07:14,040 INFO  [org.jboss.as.server] (management-handler-thread - 6) JBAS018558: Undeployed "test.war"

The two statements to look for are these:

    15:07:13,338 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    15:07:13,343 INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
