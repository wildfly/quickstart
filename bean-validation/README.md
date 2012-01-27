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

By default, tests are configured to be skipped. The reason is that the sample
test is an Arquillian test, which requires the use of a container. You can
activate this test by selecting one of the container configuration provided 
for JBoss AS 7 / JBoss Enterprise Application Platform 6 (remote).

### Testing on Remote Server
 
First you need to start JBoss AS 7 or JBoss Enterprise Application Platform 6 instance. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

Once the instance is started, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

### Testing on Managed Server
 
Arquillian will start the container for you. All you have to do is setup a path to your
extracted . To do this, run
  
    export JBOSS_HOME=/path/to/jboss-as
  
or if you are using Windows
 
    set JBOSS_HOME=X:\path\to\jboss-as

To run the test in JBoss AS 7 or JBoss EAP 6, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed

### Investigating console output

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
	 
	 
	 