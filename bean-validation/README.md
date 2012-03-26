bean-validation: Bean Validation via Arquillian Example
=======================================================
Author: Karel Piwko <kpiwko@redhat.com>

What is it?
-----------

This is your project! It's a sample, Maven 3 project to help you
get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. 
This project is setup to allow you to use CDI 1.0, JPA 2.0 and Bean Validation 1.0. 
It includes a persistence unit and some sample persistence code to help 
you get your feet wet with database access in enterprise Java. 

It does not contain an user interface layer. The main purpose of the project is 
to show you how to test Bean Validation with Arquillian. If you want to see an
example how to test Bean Validation via an user interface, check out Kitchensink
example available at <https://github.com/jbossas/quickstart/tree/master/kitchensink>.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

Running the Arquillian tests
----------------------------

This quickstart provides Arquillian tests. See [Run the Arquillian Tests](../README.html/#arquilliantests) for more information on how to set up and run the tests. When asked to start the JBoss Server, this quickstart requires that you start it with the _web_ profile.

### Investigate the Console Output

When you run the tests, JUnit will present you test report summary:

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
	 
	 
	 
