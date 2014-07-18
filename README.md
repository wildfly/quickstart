WildFly Quickstarts
====================
Summary: The quickstarts demonstrate Java EE 7 and a few additional technologies from the JBoss stack. They provide small, specific, working examples that can be used as a reference for your own project.

Introduction
---------------------


These quickstarts run on JBoss WildFly. This version uses the correct dependencies and ensures you test and compile against your runtime environment.

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#available-quickstarts): List of the available quickstarts and details about each one.

* [Suggested Approach to the Quickstarts](#suggested-approach-to-the-quickstarts): A suggested approach on how to work with the quickstarts.

* [System Requirements](#system-requirements): List of software required to run the quickstarts.

* [Configure Maven](#configure-maven): How to configure the Maven repository for use by the quickstarts.

* [Run the Quickstarts](#run-the-quickstarts): General instructions for building, deploying, and running the quickstarts.

* [Run the Arquillian Tests](#run-the-arquillian-tests): How to run the Arquillian tests provided by some of the quickstarts.

* [Optional Components](#optional-components): How to install and configure optional components required by some of the quickstarts.


Available Quickstarts
---------------------

The following is a list of the currently available quickstarts. The table lists each quickstart name, the technologies it demonstrates, gives a brief description of the quickstart, and the level of experience required to set it up. For more detailed information about a quickstart, click on the quickstart name.

Some quickstarts are designed to enhance or extend other quickstarts. These are noted in the **Prerequisites** column. If a quickstart lists prerequisites, those must be installed or deployed before working with the quickstart.

Quickstarts with tutorials in the [Get Started Developing Applications](https://github.com/wildfly/quickstart/guide/Introduction/ "Get Started Developing Applications") are noted with two asterisks ( ** ) following the quickstart name.

[TOC-quickstart]

Suggested Approach to the Quickstarts
--------------------------------------

We suggest you approach the quickstarts as follows:

* Regardless of your level of expertise, we suggest you start with the **helloworld** quickstart. It is the simplest example and is an easy way to prove your server is configured and started correctly.
* If you are a beginner or new to JBoss, start with the quickstarts labeled **Beginner**, then try those marked as **Intermediate**. When you are comfortable with those, move on to the **Advanced** quickstarts.
* Some quickstarts are based upon other quickstarts but have expanded capabilities and functionality. If a prerequisite quickstart is listed, be sure to deploy and test it before looking at the expanded version.


System Requirements
-------------

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.7, to run WildFly and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.1.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version 

3. The JBoss WildFly distribution ZIP.
    * For information on how to install and run JBoss, refer to the product documentation.

4. You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts. 


Configure Maven
-------------

### Configure Maven for JBoss WildFly

If you are using the JBoss WildFly Quickstart distribution, the community artifacts are available in the Maven central repository so no additional configuration is needed.

### Maven Profiles

Profiles are used by Maven to customize the build environment. The `pom.xml` in the root of the quickstart directory defines the following profiles:

* The `default` profile defines the list of modules or quickstarts that require nothing but JBoss Enterprise Application Platform or WildFly .
* The `requires-postgres` profile lists the quickstarts that require PostgreSQL to be running when the quickstart is deployed.
* The `complex-dependency` profile lists quickstarts that require manual configuration that can not be automated.
* The `requires-full` profile lists quickstarts the require you start the server using the full profile.
* The `requires-xts` profile lists quickstarts the require you start the server using the xts profile.
* The `non-maven` profile lists quickstarts that do not require Maven, for example, quickstarts that depend on deployment of other quickstarts or those that use other Frameworks such as Forge.


Run the Quickstarts
-------------------

The root folder of each individual quickstart contains a README file with specific details on how to build and run the example. In most cases you do the following:

* [Start the JBoss WildFly Server](#start-the-jboss-wildfly-server)
* [Build and deploy the quickstart](#build-and-deploy-the quickstart)


### Start the JBoss WildFly Server

Before you deploy a quickstart, in most cases you need a running server. A few of the Arquillian tests do not require a running server. This will be noted in the README for that quickstart. 

The JBoss server can be started a few different ways.

* [Start the JBoss Server With the _web_ profile](#start-jboss-wildfly-with-the-web-profile): This is the default configuration. It defines minimal subsystems and services.
* [Start the JBoss Server with the _full_ profile](#start-jboss-wildfly-with-the-full-profile): This profile configures many of the commonly used subsystems and services.
* [Start the JBoss Server with a custom configuration](#start-jboss-wildfly-with-custom-configuration-options): Custom configuration parameters can be specified on the command line when starting the server.    

The README for each quickstart will specify which configuration is required to run the example.

#### Start JBoss WildFly with the Web Profile

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

#### Start JBoss WildFly with the Full Profile

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml

#### Start JBoss WildFly with Custom Configuration Options

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server. Replace the CUSTOM_OPTIONS with the custom optional parameters specified in the quickstart.

        For Linux:   JBOSS_HOME/bin/standalone.sh CUSTOM_OPTIONS
        For Windows: JBOSS_HOME\bin\standalone.bat CUSTOM_OPTIONS
           
### Build and Deploy the Quickstarts

See the README file in each individual quickstart folder for specific details and information on how to run and access the example.

#### Build the Quickstart Archive

In some cases, you may want to build the application to test for compile errors or view the contents of the archive. 

1. Open a command line and navigate to the root directory of the quickstart you want to build.
2. Use this command if you only want to build the archive, but not deploy it:

            mvn clean package

#### Build and Deploy the Quickstart Archive

1. Make sure you [start the JBoss Server](#start-the-jboss-wildfly-server) as described in the README.
2. Open a command line and navigate to the root directory of the quickstart you want to run.
3. Use this command to build and deploy the archive:

            mvn clean package wildfly:deploy

#### Undeploy an Archive

The command to undeploy the quickstart is simply: 

        mvn wildfly:undeploy
 
### Verify the Quickstarts Build with One Command
-------------------------

You can verify the quickstarts build using one command. However, quickstarts that have complex dependencies must be skipped. For example, the _jax-rs-client_ quickstart is a RESTEasy client that depends on the deployment of the _helloworld-rs_ quickstart. As noted above, the root `pom.xml` file defines a `complex-dependencies` profile to exclude these quickstarts from the root build process. 

To build the quickstarts:

1. Do not start the server.
2. Open a command line and navigate to the root directory of the quickstarts.
3. Use this command to build the quickstarts that do not have complex dependencies:

            mvn clean install '-Pdefault,!complex-dependencies'


### Undeploy the Deployed Quickstarts with One Command
-------------------------

To undeploy the quickstarts from the root of the quickstart folder, you must pass the argument `-fae` (fail at end) on the command line. This allows the command to continue past quickstarts that fail due to complex dependencies and quickstarts that only have Arquillian tests and do not deploy archives to the server.

You can undeploy quickstarts using the following procedure:

1. Start the server.
2. Open a command line and navigate to the root directory of the quickstarts.
3. Use this command to undeploy any deployed quickstarts:

            mvn wildfly:undeploy -fae

To undeploy any quickstarts that fail due to complex dependencies, follow the undeploy procedure described in the quickstart's README file.


### Run the Arquillian Tests
-------------------------

Some of the quickstarts provide Arquillian tests. By default, these tests are configured to be skipped, as Arquillian tests require the use of a container. 

You can run these tests using either a remote or managed container. The quickstart README should tell you what you should expect to see in the console output and server log when you run the test.

1. Test the quickstart on a Remote Server
    * A remote container requires you start the JBoss WildFly server before running the test. [Start the JBoss Server](#start-the-jboss-wildfly-server) as described in the quickstart README file.
    * Run the test goal with the following profile activated:

            mvn clean test -Parq-wildfly-remote
2. Test the quickstart on Managed Server

    _Note: This test requires that your server is not running. Arquillian will start the container for you, however, you must first let it know where to find the remote JBoss container._
    * Open the test/resources/arquillian.xml file located in the quickstart directory. 
    * Find the configuration for the remote JBoss container. It should look like this:

            <!-- Example configuration for a remote WildFly instance -->
            <container qualifier="jboss" default="true">
                <!-- By default, arquillian will use the JBOSS_HOME environment variable.  Alternatively, the configuration below can be uncommented. -->
                <!--<configuration> -->
                <!--<property name="jbossHome">/path/to/wildfly</property> -->
                <!--</configuration> -->
            </container>
    * Remove the comments from the `<configuration>` elements.

            <!-- Example configuration for a remote WildFly instance -->
            <container qualifier="jboss" default="true">
                <!-- By default, arquillian will use the JBOSS_HOME environment variable.  Alternatively, the configuration below can be uncommented. -->
                <configuration>
                    <property name="jbossHome">/path/to/wildfly</property>
                </configuration>
            </container>
    * Find the "jbossHome" property and replace the "/path/to/wildfly" value with the actual path to your JBoss WildFly server.
    * Run the test goal with the following profile activated:

            mvn clean test -Parq-wildfly-managed

Use JBoss Developer Studio or Eclipse to Run the Quickstarts
-------------------------------------

You can also deploy the quickstarts from Eclipse using JBoss tools. For more information on how to set up Maven and the JBoss tools, refer to the [JBoss Enterprise Application Platform 6 Development Guide](https://access.redhat.com/knowledge/docs/JBoss_Enterprise_Application_Platform/) or [Get Started Developing Applications](https://github.com/wildfly/quickstart/guide/Introduction/ "Get Started Developing Applications").


Optional Components
-------------------
The following components are needed for only a small subset of the quickstarts. Do not install or configure them unless the quickstart requires it.

* [Create Users Required by the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#create-users-required-by-the-quickstarts): Add a Management or Application user for the quickstarts that run in a secured mode.

* [Configure the PostgreSQL Database for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL.md#configure-the-postgresql-database-for-use-with-the-quickstarts): The PostgreSQL database is used for the distributed transaction quickstarts.

* [Configure Byteman for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#configure-byteman-for-use-with-the-quickstarts): This tool is used to demonstrate crash recovery for distributed transaction quickstarts.

