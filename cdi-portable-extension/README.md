cdi-portable-extension: CDI Portable Extension 
======================================================
Author: Jason Porter  
Level: Intermediate  
Technologies: CDI  
Summary: The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension that uses SPI classes to inject beans with data from an XML file.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension and some of the SPI classes used
to complete that task in an application deployed to Red Hat JBoss Enterprise Application Platform. 
This particular extension explores the ProcessInjectionTarget and 
InjectionTarget SPI classes of CDI, to demonstrate a possible way to seed data into beans.

A Portable Extension is an extension to Java EE 6 and above, which is tailored to a specific
use case and will run on any Java EE 6 or later implementation. Portable extensions can implement 
features not yet supported by the specifications, such as type-safe messages or external configuration of beans.

The project contains very simple domain model classes, an extension class, the service registration file
for that extension and an Arquillian test to verify the extension is working correctly.

It does not contain any user interface, the tests must be run to verify everything is working
correctly.

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


Run the Arquillian Tests
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote


Investigate the Console Output
----------------------------

Maven prints summary of the 2 performed tests to the console.

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.extension.test.CreatureExtensionTest
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.87 sec

    Results :

    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

_Note:_ You may see the following warning. You can ignore it as it should be resolved by this JIRA: https://issues.jboss.org/browse/WFLY-5165.

    WARN: The protocol 'http-remoting-jmx' is deprecated, instead you should use 'remote+http'.


### Server log

There are two logging statements done when the tests are run:

#### Example

    INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) WFLYSRV0027: Starting deployment of "test.war" (runtime-name: "test.war")
    ...
    INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    [timestamp] INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter
    ...
    INFO  [org.jboss.as.server] (management-handler-thread - 6) WFLYSRV0010: Deployed "test.war" (runtime-name : "test.war")
    ...
    [timestamp] INFO  [org.jboss.as.server.deployment] (MSC service thread 1-4) WFLYSRV0028: Stopped deployment test.war (runtime-name: test.war) in 27ms
    ...
    INFO  [org.jboss.as.server] (management-handler-thread - 6) WFLYSRV0009: Undeployed "test.war" (runtime-name: "test.war")

The two statements to look for are these:

    INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.Monster
    INFO  [org.jboss.as.quickstart.cdi.extension.CreatureExtension] (MSC service thread 1-3) Setting up injection target for class org.jboss.as.quickstart.cdi.extension.model.NonPlayerCharacter

Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts)


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

