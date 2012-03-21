QUICKSTART_NAME: Brief Description of the Quickstart
======================================================
Author: YOUR_NAME and optional CONTACT_INFO

_This file is meant to serve as a template or guideline for your own quickstart README.md file. Be sure to replace QUICKSTART_NAME and YOUR_NAME, with the appropriate values._

What is it?
-----------

This is where you provide an overview of what the quickstart demonstrates. For example

 * What are the technologies demonstrated by the quickstart?
 * What does it do when you run it?

You should include any information that would help the user understand the quickstart.  

If possible, give an overview, including any code they should look at to understand how it works..


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

You can link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Configure Optional Components
-------------------------

If your quickstart requires any additional components, decribe how to set them up here. 

If your quickstart requires a secured user, PostgreSQL, or Byteman, you can link to the instructions in the README file located in the root folder of the quickstart directory. For example:

 * This quickstart use secured management interfaces and requires that you create a management (or application) user to access the running application. Instructions to set up a Management (or Application) user can be found here: 

    * [Add a Management User](../README.html/#addmanagementuser)

    * [Add an Application User](../README.html/#addapplicationuser)

 * This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Install and Configure the PostgreSQL Database](../README.html/#postgresql)

 * This quickstart uses Byteman to help demonstrate crash recovery. Instructions to install and configure Bytemand can be found here: [Install and Configure Byteman](../README.html#byteman)

If your quickstart requires any other components, you must describe how to configure them here.


Start the JBoss Server
-------------------------

Does this quickstart require a running server? If so, you must show how to start the server. If you start the server in one of the following 3 ways, you can simply link to the instructions in the README file located in the root folder of the quickstart directory using one of the following:

 *  Follow the instructions here to [Start the JBoss Server with the _web_ profile](../README.html#startserverweb)

 *  Follow the instructions here to [Start the JBoss Server with the _full_ profile](../README.html/#startserverfull)

 *  Follow the instructions here to [Start the JBoss Server with a custom configuration](../README.html/#startservercustom). You must pass the following argument string on the command line as noted in the instructions. For example "--server-config=../../docs/examples/configs/standalone-xts.xml".

If the server is started in a different manner than above, give the specific instructions.


Build and Deploy the Quickstart
-------------------------

Remind them to start the server if a running server is required.

Next give instructions to build and deploy the quickstart. If the quickstart is built and deployed using the standard Maven commands, "mvn clean package" and "mvn jboss-as:deploy", rather than repeat them here, you can link to the instructions in the README file located in the root folder the quickstart folder. For example:

 * To build and deploy the quickstart, follow the instruction here: [Build and Deploy the Quickstarts](../README.html/#buildanddeploy)

If this quickstart requires different or additional instructions, add those instructions here.


Access the application (For quickstarts that have a UI component)
---------------------

Access the running application in a browser at the following URL:  [http://localhost:8080/jboss-as-QUICKSTART_NAME](http://localhost:8080/jboss-as-QUICKSTART_NAME)

Make sure to make the URL a hyperlink as above, substituting the your quickstart name for the QUICKSTART_NAME. 

Briefly describe what you will see when you access the application. For example: 

 * You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component. 

    * If the box is checked, the updates will be executed within a session bean method. 
    * If the box is not checked, the transactions and JPA updates will run in a servlet instead of session beans. 

 * To list all existing key/value pairs, leave the key input box empty. 

 * To add or update the value of a key, enter a key and value input boxe and click the submit button to see the results.

Add any information that will help them run and understand your quickstart.


Run the Arquillian tests (For quickstarts that contain Arquillian tests)
-------------------------

By default, tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. 

* Run these tests using either a managed or remote container.

  * Test the quickstart on a Remote Server

      * A remote container requires you start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server before running the test. Follow the instructions here to [Start the JBoss Server with the _web_ profile](../README.html#startserverweb) (or point to the server start method required by your quickstart).
      * Run the test goal with the following profile activated:

                  mvn clean test -Parq-jbossas-remote

  * Test the quickstart on Managed Server

      * Arquillian will start the container for you. You must set the path to your JBoss Enterprise Application Platform 6 or JBoss AS7. Open a command line and type the following command for your operating system:

                  Linux: export JBOSS_HOME=/path/to/jboss-as
                  Windows: SET JBOSS_HOME=X:\path\to\jboss-as

      * Run the test goal with the following profile activated:

                  mvn clean test -Parq-jbossas-managed

* Investigate console output

      Copy and paste output from the JUnit tests to show what to expect from the tests.

* Check the server console. You will see messages similar to the following:

      Copy and paste log messages output by the application to show what to expect when running the application.


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
