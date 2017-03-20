# QUICKSTART_NAME: Brief Description of the Quickstart (try to limit the description to 55 characters)

Author: YOUR_NAME and optional CONTACT_INFO  
Level: [one of the following: Beginner, Intermediate, or Advanced]  
Technologies: (list technologies used here)  
Summary: (A brief description of the quickstart to appear in the table and in Google search SEO results. Try to limit the description to 155 characters )  
Prerequisites: (list any quickstarts that must be deployed prior to running this one)  
Target Product: (${product.name}, JBoss Mobile, JBoss Data Grid, etc)  _Official names are here: https://mojo.redhat.com/docs/DOC-962110_  
Source: (The URL for the repository that is the source of record for this quickstart)  


_NOTE: This file is meant to serve as a template or guideline for your own quickstart README.md file:_

* _The first lines in the file after the quickstart name and description (Author:, Level:, etc.) are metadata tags used by the [JBoss Developer site](http://www.jboss.org/developer-materials/#!formats=jbossdeveloper_quickstart). Make sure you include 2 spaces at the end of each line so they also render correctly when rendered as HTML files._
* _Be sure to replace the `QUICKSTART_NAME` and `YOUR_NAME` variables in your `README` file with the appropriate values._
* _Contributor instructions are enclosed within comments `<!-- Contributor: -->`. These instructions are only meant to help you and you should NOT include them in your README file!_
* _Review the other quickstart `README` files if you need help with formatting or content._

## What is it?

<!-- Contributor: This is where you provide an overview of what the quickstart demonstrates. Be sure to include the full product name on the first line. For example: -->

The `QUICKSTART_NAME` quickstart demonstrates ... in ${product.name.full}.
 * What are the technologies demonstrated by the quickstart?
 * What does it do when you run it?

You should include any information that would help the user understand the quickstart.

If possible, give an overview, including any code they should look at to understand how it works..


## System Requirements

<!-- Contributor: For example: -->

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Configure Optional Components

<!-- Contributor: If your quickstart requires any additional components, decribe how to set them up here. If your quickstart requires a secured user, PostgreSQL, or Byteman, you can copy instructions you find in other quickstarts, or you can use the examples here: -->

 * This quickstart uses a secured management interface and requires that you create a management (or application) user to access the running application. Instructions to set up a Management (or Application) user can be found here:

    * [Add a Management User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-a-management-user)

    * [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user)

 * This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Configure the PostgreSQL Database for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#configure-the-postgresql-database-for-use-with-the-quickstarts)

 * This quickstart uses Byteman to help demonstrate crash recovery. Instructions to install and configure Byteman can be found here: [Configure Byteman for Use with the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_BYTEMAN.md#configure-byteman-for-use-with-the-quickstarts)


## Start the Server

<!-- Contributor: Does this quickstart require one or more running servers? If so, you must show how to start the server. If you start the server in one of the following 3 ways, you can simply copy the instructions in the README file located in the root folder of the quickstart directory: -->

 * Start the ${product.name} Server

 * Start the ${product.name} Server with the Full Profile

 * Start the ${product.name} Server with Custom Options. You will need to provide the argument string to pass on the command line, for example:

      `--server-config=../../docs/examples/configs/standalone-xts.xml`

<!-- Contributor: If the server is started in a different manner than above, give the specific instructions. -->


## Build and Deploy the Quickstart

<!-- Contributor: If the quickstart is built and deployed using the standard Maven commands, copy the following: -->

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy
4. This will deploy `target/${project.artifactId}.war` (or `target/${project.artifactId}.ear`) to the running instance of the server.

<!-- Contributor: Be sure to replace the `QUICKSTART_NAME`. If this quickstart requires different or additional instructions, be sure to modify or add those instructions here. -->


## Access the Application

<!-- Contributor: Add this section only if the quickstart has a UI component and provide the URL to access the running application. Be sure to make the URL a hyperlink as below, substituting the your quickstart name for the `QUICKSTART_NAME`. -->

        Access the running application in a browser at the following URL:  <http://localhost:8080/${project.artifactId}>


<!--Contributor: Briefly describe what you will see when you access the application. For example: -->

        You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component.

            If the box is checked, the updates will be executed within a session bean method.
            If the box is not checked, the transactions and JPA updates will run in a servlet instead of session beans.

        To list all existing key/value pairs, leave the key input box empty.

        To add or update the value of a key, enter a key and value input boxe and click the submit button to see the results.

<!-- Contributor: Add any information that will help them run and understand your quickstart. -->


## Undeploy the Archive

<!--Contributor: For example: -->

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Arquillian Tests (For quickstarts that contain Arquillian tests)

<!-- Contributor: For example: -->

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Investigate the Console Output

<!-- Contributor: The quickstart README should show what to expect in the console from running the tests. -->

* If applicable, copy and paste output from the JUnit tests to show what to expect in the console from the tests.

## Investigate the Server Log

<!-- Contributor: The quickstart README should show what to expect in the server log from running the tests. -->

* If applicable, copy and paste log messages output by the application to show what to expect in the server log when running the tests.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

<!-- Contributor: For example: -->

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

## Debug the Application

<!--Contributor: For example: -->

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
