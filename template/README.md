QUICKSTART_NAME: Brief Description of the Quickstart
======================================================
Author: YOUR_NAME and optional CONTACT_INFO
Level: [one of the following: Beginner, Intermediate, or Advanced]
Technologies: (list technologies used here)
Summary: (a brief description of the quickstart to appear in the table )
Prerequisites: (list any quickstarts that must be deployed prior to running this one)
Target Product: WildFly
Source: (The URL for the repository that is the source of record for this quickstart)


_This file is meant to serve as a template or guideline for your own quickstart README.md file. Be sure to replace QUICKSTART_NAME and YOUR_NAME, with the appropriate values._

Contributor instructions are prefixed with 'Contributor: '

What is it?
-----------

Contributor: This is where you provide an overview of what the quickstart demonstrates. For example:

 * What are the technologies demonstrated by the quickstart?
 * What does it do when you run it?

You should include any information that would help the user understand the quickstart.  

If possible, give an overview, including any code they should look at to understand how it works..


System requirements
-------------------

Contributor: For example: 

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

Contributor: You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure Optional Components
-------------------------

Contributor: If your quickstart requires any additional components, decribe how to set them up here. If your quickstart requires a secured user, PostgreSQL, or Byteman, you can link to the instructions in the README file located in the root folder of the quickstart directory. Here are some examples:

 * This quickstart uses a secured management interface and requires that you create a management (or application) user to access the running application. Instructions to set up a Management (or Application) user can be found here: 

    * [Add a Management User](../README.md#addmanagementuser)

    * [Add an Application User](../README.md#addapplicationuser)

 * This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Install and Configure the PostgreSQL Database](../README.md#postgresql)

 * This quickstart uses Byteman to help demonstrate crash recovery. Instructions to install and configure Byteman can be found here: [Install and Configure Byteman](../README.md#byteman)


Start JBoss WildFly
-------------------------

Contributor: Does this quickstart require one or more running servers? If so, you must show how to start the server. If you start the server in one of the following 3 ways, you can simply copy the instructions in the README file located in the root folder of the quickstart directory:

 * Start JBoss WildFly with the Web Profile

 * Start JBoss WildFly with the Full Profile

 * Start JBoss WildFly with Custom Options. You will need to provide the argument string to pass on the command line, for example:

      `--server-config=../../docs/examples/configs/standalone-xts.xml`

Contributor: If the server is started in a different manner than above, give the specific instructions.


Build and Deploy the Quickstart
-------------------------

Contributor: If the quickstart is built and deployed using the standard Maven commands, "mvn clean package" and "mvn wildfly:deploy", copy the following:

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
4. This will deploy `target/wildfly-QUICKSTART_NAME.war` (or `target/wildfly-QUICKSTART_NAME.ear`) to the running instance of the server.
 
Contributor: Be sure to replace the QUICKSTART_NAME. If this quickstart requires different or additional instructions, be sure to modify or add those instructions here.


Access the application (For quickstarts that have a UI component)
---------------------

Contributor: Provide the URL to access the running application. Be sure to make the URL a hyperlink as below, substituting the your quickstart name for the QUICKSTART_NAME. 

        Access the running application in a browser at the following URL:  <http://localhost:8080/wildfly-QUICKSTART_NAME>


Contributor: Briefly describe what you will see when you access the application. For example: 

        You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component. 

            If the box is checked, the updates will be executed within a session bean method. 
            If the box is not checked, the transactions and JPA updates will run in a servlet instead of session beans. 

        To list all existing key/value pairs, leave the key input box empty. 
    
        To add or update the value of a key, enter a key and value input boxe and click the submit button to see the results.

Contributor: Add any information that will help them run and understand your quickstart.


Undeploy the Archive
--------------------

Contributor: For example: 

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Arquillian Tests (For quickstarts that contain Arquillian tests)
-------------------------

Contributor: For example: 

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote

Contributor: The quickstart README should show what to expect from the the tests

* Copy and paste output from the JUnit tests to show what to expect in the console from the tests.

* Copy and paste log messages output by the application to show what to expect in the server log when running the tests.



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
Contributor: For example: 

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

Contributor: For example: 

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc