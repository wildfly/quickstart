jboss-as-tasks
==============

What is it?
-----------

This is your project! It's a sample, Maven 3 project to help you
get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss EAP 6. 
This project is setup to allow you to use JPA 2.0. 
It includes a persistence unit and some sample persistence code to help 
you get your feet wet with database access in enterprise Java. 

It does not contain an user interface layer. The main purpose of the project is 
to show you how to test JPA with Arquillian.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

You will use a real server to test internals of your application with Arquillian.

Running the Arquillian tests
============================

By default, tests are configured to be skipped. The reason is that the sample
test is an Arquillian test, which requires the use of a container. You can
activate this test by selecting one of the container configuration provided 
for JBoss AS 7 / JBoss EAP 6 (remote).

Testing on Remote Server
-------------------------
 
First you need to start JBoss AS 7 or JBoss EAP6. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To run the test in JBoss AS 7, first start a JBoss AS 7 or JBoss EAP 6 instance. Then, run the
test goal with the following profile activated:

    mvn clean test -Parq-jbossas-remote

Testing on Managed Server
-------------------------
 
Arquillian will start the container for you. All you have to do is setup a path to your
extracted . To do this, run
  
    export JBOSS_HOME=/path/to/jboss-as
  
or if you are using windows
 
    set JBOSS_HOME=X:\path\to\jboss-as

Or hardcode the path in pom.xml file

To run the test in JBoss AS 7 or JBoss EAP 6, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed


Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.
 
Detailed instructions for using Eclipse with JBoss AS 7 are provided in the 
JBoss AS 7 Getting Started Guide for Developers.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
