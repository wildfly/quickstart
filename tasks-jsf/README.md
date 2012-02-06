tasks-jsf: JSF, JPA quickstart
==============================

Author: [Lukas Fryc](https://community.jboss.org/people/lfryc)


What is it?
-----------

This is your project! It's a sample Maven 3 project to help you
get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss EAP 6. 
This project is setup to allow you to use JPA 2.0 persistence with JSF 2.0 as view layer.

The theme of this application is simple Task management with simple log in.
Project contains two entities - user and task.

This sample includes a persistence unit and some sample persistence code to help 
you get your feet wet with database access in enterprise Java.

Persistence code is covered by tests to help you write business logic without need
to use any view layer.

JSF 2.0 is used to present user two views - authentication form and task view.

The task view is formed by a task list, a task detail and a task addition form.
Whole task view is completely driven by AJAX.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

You will use a real server to test internals of your application with Arquillian.

Building WAR deployment with Maven
===============================

To build the application, the only you need is trigger Maven build from
command-line:

    mvn clean package
    
This command will build the WAR archive in target/jboss-as-tasks-jsf.war.


Deploying the WAR to the JBoss AS
=================================

To deploy the application to JBoss AS 7, you needs just copy the built WAR
to the JBOSS_HOME/standalone/deployments directory.

There is Maven plugin which makes this task even simpler:

Let's start the JBoss AS and trigger following JBoss AS Maven plugin from
inside the project (you need to built the WAR first, see above):

    mvn jboss-as:deploy
    
After successful deploy, you should see server log output similar to following:

    JBAS018210: Registering web context: /jboss-as-tasks-jsf
    JBAS018559: Deployed "jboss-as-tasks-jsf.war"

You can access the running application on URL
    
[http://localhost:8080/jboss-as-tasks-jsf/](http://localhost:8080/jboss-as-tasks-jsf/)

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

    JBAS018210: Registering web context: /jboss-as-tasks-jsf
    JBAS018559: Deployed "jboss-as-tasks-jsf.war"

You can access the running application on URL
    
[http://localhost:8080/jboss-as-tasks-jsf/](http://localhost:8080/jboss-as-tasks-jsf/)


Running the Arquillian tests
============================

Integration tests written in Arquillian gives you opportunity to check
application's logic before accessing the view, which in turn speed ups
the usual development turn-around.

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
 
Arquillian will start the container for you. All you have to do is setup a path to JBoss AS. 
Edit `src/test/resources/arquillian` and set the `jbossHome` element.

To run the test in JBoss AS 7 or JBoss EAP 6, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed
    

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
