richfaces-validation: RichFaces and Bean Validation
=======================================================
Author: [Lukas Fryc](https://community.jboss.org/people/lfryc)

What is it?
-----------

This is your project! It's a sample, Maven 3 project to help you
get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. 
This project is setup to allow you to use JSF 2.0, RichFaces 4.2, CDI 1.0, JPA 2.0 and Bean Validation 1.0. 

It consists of one entity - Member - which is annotated with JSR-303 (Bean Validation)
constraints. These constraints are, in typical applications, checked on several places:

* as database constraints
* on the persistence layer
* on view layer (using JSF / Bean Validation integration)
* on the client side (using RichFaces 4.2 - Client Side Validation)

This allows you to not repeat the definitions of the constraints, but still have them applied at all layers,
and thus report good error messages to users.


However this quickstart does not contain any persistence layer, since it shows integration
of RichFaces, JSF and Bean Validation.

There are tests for all Bean Validation constraints for Member entity which allows you to check
constraints without need to test them on view layer. These tests are written using Arquillian framework.


Application Theme
-----------------

Application contains view layer written in JSF and RichFaces and shows AJAX-based wizard for new
member registration. Each member needs to fill in these data in separated screens:

* e-mail
* name and phone
* confirmation of all inserted data

Once users are successfully registered, they are redirected to initial page with the message
that they have successfully registered.

The validation is done using client-side validation where possibly. There is also object-graph
validation for password confirmation using @AssertTrue annotation.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

Building WAR deployment with Maven
===============================

For building application, you need to trigger the Maven build using command-line:

    mvn clean package
    
This command will build the WAR archive in target/jboss-as-richfaces-validation.war.


Deploying the WAR to the JBoss AS
=================================

To deploy the application to JBoss AS 7, you needs just copy the built WAR
to the JBOSS_HOME/standalone/deployments directory.

There is Maven plugin which makes this task even simpler:

Let's start the JBoss AS and trigger following JBoss AS Maven plugin from
inside the project (you need to built the WAR first, see above):

    mvn jboss-as:deploy
    
After successful deploy, you should see server log output similar to following:

    JBAS018210: Registering web context: /jboss-as-richfaces-validation
    JBAS018559: Deployed "jboss-as-richfaces-validation.war"

You can access the running application on URL
    
[http://localhost:8080/jboss-as-richfaces-validation/](http://localhost:8080/jboss-as-richfaces-validation/)

You can un-deploy the application by deleting the WAR from deployments
directory or you can use the JBoss AS Maven plugin again:

    mvn jboss-as:undeploy
    
In this case, you should see following output in server console:

    JBAS018558: Undeployed "jboss-as-tasks-jsf.war"

    
Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

If you created the project from the command-line using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.
 
Detailed instructions for using Eclipse with JBoss AS 7 are provided in the 
JBoss AS 7 Getting Started Guide for Developers.


Running the project from IDE
============================

To enhance your development turn-around, it's recommended to use IDE to build
and deploy the project.

Running the project from JBDS
-----------------------------

At first, you need to make sure you have setup the JBoss AS server instance
setup in JBDS and running.

Then select your project and choose Run > Run As > Run on Server
in the context menu and select JBoss AS 7 server instance.

After successful deploy, you should see server log output similar to following:

    JBAS018210: Registering web context: /jboss-as-richfaces-validation
    JBAS018559: Deployed "jboss-as-richfaces-validation.war"

You can access the running application on URL
    
[http://localhost:8080/jboss-as-richfaces-validation/](http://localhost:8080/jboss-as-richfaces-validation/)


Running the Arquillian tests
============================

By default, tests are configured to be skipped. The reason is that the sample
test is an Arquillian test, which requires the use of a container. You can select either
managed or remote container, the difference is that the remote one requires a running JBoss AS 7 / 
JBoss Enterprise Application Platform 6 instance prior executing tests.

Testing on Remote Server
------------------------
 
First you need to start JBoss AS 7 or JBoss Enterprise Application Platform 6 instance. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

Once the instance is started, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

Testing on Managed Server
-------------------------
 
Arquillian will start the container for you. All you have to do is setup a path to your JBoss AS7 or JBoss
Enterprise Application Platform 6. To do this, run
  
    export JBOSS_HOME=/path/to/jboss-as
  
or if you are using Windows
 
    set JBOSS_HOME=X:\path\to\jboss-as

To run the test in JBoss AS 7 or JBoss EAP 6, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed

Investigating console output
----------------------------

JUnit will present you test report summary:

	Tests run: 5, Failures: 0, Errors: 0, Skipped: 0

If you are interested in more details, check ``target/surefire-reports`` directory. 
You can check console output to verify that Arquillian had really used the real application server. 
Search for lines similar to the following ones in the server output log:

	 [timestamp] INFO [org.jboss.as.server.deployment] (MSC service thread 1-2) Starting deployment of "test.war"
	 ...
	 [timestamp] INFO [org.jboss.as.server] (management-handler-threads - 1) JBAS018559: Deployed "test.war"
	 ...
	 [timestamp] INFO [org.jboss.as.server.deployment] (MSC service thread 1-3) Stopped deployment test.war in 48ms
	 ...
	 [timestamp] INFO [org.jboss.as.server] (management-handler-threads - 1) JBAS018558: Undeployed "test.war
	 
	 
	 
