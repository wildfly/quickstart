richfaces-validation: RichFaces and Bean Validation
=======================================================
Author: [Lukas Fryc](https://community.jboss.org/people/lfryc)

What is it?
-----------

This is your project! It's a sample, Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. This project is setup to allow you to use JSF 2.0, RichFaces 4.2, CDI 1.0, JPA 2.0 and Bean Validation 1.0. 

It consists of one entity - `Member` - which is annotated with JSR-303 (Bean Validation) constraints. These constraints are, in typical applications, checked in several places:

* as database constraints
* in the persistence layer
* in view layer (using JSF / Bean Validation integration)
* on the client (using RichFaces 4.2 - Client Side Validation)

This allows you to not repeat the definitions of the constraints, but still have them applied at all layers, and thus report good error messages to users.

This quickstart does not contain any persistence layer, it only shows integration of RichFaces, JSF and Bean Validation.

The quickstart includes tests for all Bean Validation constraints for the `Member` entity which allows you to check constraints without the need to test them on the view layer. The tests are written using the Arquillian framework.

The application contains a view layer written using JSF and RichFaces, and includes an AJAX wizard for new member registration. Each member needs to fill in in the wizard:

* e-mail
* name and phone
* confirmation of supplied information

Once users are successfully registered, they are redirected back to the initial page. A message that they have registered successfully is displayed to them.

The validation is done using client-side validation where possible.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. The following instructions target JBoss AS 7, but they also apply to JBoss Enterprise Application Platform 6.
 
With the prerequisites out of the way, you're ready to build and deploy.

Configure Maven 
-------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Start the JBoss Server
-------------------------

Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server with the web profile.

1. Open a command line and navigate to the root of the JBoss directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the application
-------------------------

1. Make sure your server is running.
2. Open a command line and navigate to the root of the richfaces-validation quickstart directory.
3. Type the following in the command line: 
    For JBoss Enterprise Application Platform 6, Maven user settings NOT configured: 

        mvn clean package jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

    For JBoss AS 7 or JBoss Enterprise Application Platform 6, Maven user settings configured: 

        mvn clean package jboss-as:deploy

4. This will build and deploy `ear/target/jboss-as-richfaces-validation.war`.
5. To undeploy the application, run this command:

        mvn jboss-as:undeploy

You can also use Eclipse to start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server and deploy the project. See the <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.


Running the Arquillian tests
============================

This quickstart provides Arquillian tests. See [Run the Arquillian Tests](../README.html/#arquilliantests) for more information on how to set up and run the tests. When asked to start the JBoss Server, this quickstart requires that you start it with the _web_ profile.

### Investigate the Console Output

When you run the tests, JUnit will present you test report summary:

	Tests run: 5, Failures: 0, Errors: 0, Skipped: 0

If you are interested in more details, check `target/surefire-reports` directory. You can check console output to verify that Arquillian had really used the real application server. Search for lines similar to the following ones in the server output log:

    [timestamp] INFO [org.jboss.as.server.deployment] (MSC service thread 1-2) Starting deployment of "test.war"
    ...
    [timestamp] INFO [org.jboss.as.server] (management-handler-threads - 1) JBAS018559: Deployed "test.war"
    ...
    [timestamp] INFO [org.jboss.as.server.deployment] (MSC service thread 1-3) Stopped deployment test.war in 48ms
    ...
    [timestamp] INFO [org.jboss.as.server] (management-handler-threads - 1) JBAS018558: Undeployed "test.war
	 
	 
Import the project into an IDE
---------------------

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), there is nothing to do. You should already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the command line using `archetype:generate`, you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
	 
