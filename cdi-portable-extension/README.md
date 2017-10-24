# cdi-portable-extension: CDI Portable Extension

Author: Jason Porter  
Level: Intermediate  
Technologies: CDI  
Summary: The `cdi-portable-extension` quickstart demonstrates a simple CDI Portable Extension that uses SPI classes to inject beans with data from an XML file.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `cdi-portable-extension` quickstart demonstrates how to use some of the SPI classes to create a simple CDI portable extension in an application deployed to ${product.name.full}.

CDI exposes a set of SPIs to allow development of portable extensions to CDI. A portable extension is an extension to Java EE 6 and above that is tailored to a specific use case and runs on any Java EE 6 or later implementation. Portable extensions can implement features not yet supported by the specifications, such as type-safe messages or external configuration of beans.

This particular extension explores the `ProcessInjectionTarget` and `InjectionTarget` SPI classes of CDI to demonstrate one possible way to seed data into beans. It uses the `ProcessInjectionTarget` to create and add state to beans using XML. It is similar to the Seam XML configuration idea from Seam 3, but is much more simplistic.

The project contains very simple domain model classes, an extension class, the service registration file
for that extension and an Arquillian test to verify the extension is working correctly.

* `CreatureExtension`: Class that implements `Extension`. This is the first step in creating a portable extension. It is not a bean since it is instantiated by the container during the initialization process, before any beans or contexts exist. However, it can be injected into other beans once the initialization process is complete.
* `XmlBackedWrappedInjectionTarget`: Wrapper class for the standard `@link InjectionTarget` to add the field values from the XML file.
* `CreatureType`: An enum in the `model` package. It defines the two types of creatures, `MONSTER` and `NPC`.
* `Creature`: The interface in the `model` package. Defines the name and type of creature.
* `Monster`: Class in the `model` package that implments creature with a type `MONSTER`.
* `NonPlayerCharacter`: Class in the `model` package that implements creature with a type `NPC`.
* `META-INF/creatures.xml`: The XML in this file seeds the bean with data.
* `META-INF/creatures.xsd`: The schema for the XML that seeds the bean with data.

On application start, there will be one instance of `Monster` with a name of `Cat`, hitpoints of `10` and an initiative of `25`. There will also be one instance of `NonPlayerCharacter` with name of `Drunkard` and location of `Drunken Duck Tavern`. There is no instantiation code for the object outside of the CDI extension.

_Note:_ This quickstart does not contain any user interface. Instead, you run tests and check server log messages to verify everything is working correctly.


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


## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Investigate the Console Output

Maven prints a summary of the two performed tests to the console.

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstart.cdi.extension.test.CreatureExtensionTest
    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.87 sec

    Results :

    Tests run: 2, Failures: 0, Errors: 0, Skipped: 0


### Server log

The following messages are written to the server log when the tests are run:

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

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
