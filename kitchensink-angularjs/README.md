# kitchensink-angularjs: Demonstrates AngularJS with JAX-RS

Author: Pete Muir  
Level: Intermediate  
Technologies: AngularJS, CDI, JPA, EJB, JPA, JAX-RS, BV  
Summary: The `kitchensink-angularjs` quickstart demonstrates a Java EE 7 application using AngularJS with JAX-RS, CDI, EJB, JPA, and Bean Validation.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `kitchensink-angularjs` quickstart is a deployable Maven 3 project to help you get your foot in the door developing with AngularJS on Java EE 7 with ${product.name.full}.

This project is setup to allow you to create a compliant Java EE 7 application using CDI 1.2, EJB 3.2, JPA 2.1 and Bean Validation 1.1. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Run the Arquillian Functional Tests

This quickstart provides Arquillian functional tests as well. They are located in the functional-tests/ subdirectory under the root directory of this quickstart.
Functional tests verify that your application behaves correctly from the user's point of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the `functional-tests/` directory in this quickstart.
4. If you have a running instance of the ${product.name} server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-remote

5. If you prefer to run the functional tests using managed instance of the ${product.name} server, meaning the tests will start the server for you, type fhe following command:

        mvn clean verify -Parq-managed


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

_Note:_ If you have not installed the [AngularJS Eclipse plugin](https://github.com/angelozerr/angularjs-eclipse) into JBoss Developer Studio, you may see one or more of the following warnings when you import this project. You can ignore these warnings.

    HTML Problem: Undefined attribute name (ng-app)
    HTML Problem: Undefined attribute name (ng-click)
    HTML Problem: Undefined attribute name (ng-disabled)
    HTML Problem: Undefined attribute name (ng-hide)
    HTML Problem: Undefined attribute name (ng-model)
    HTML Problem: Undefined attribute name (ng-pattern)
    HTML Problem: Undefined attribute name (ng-repeat)
    HTML Problem: Undefined attribute name (ng-show)
    HTML Problem: Undefined attribute name (ng-submit)
    HTML Problem: Undefined attribute name (ng-view)

## Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
