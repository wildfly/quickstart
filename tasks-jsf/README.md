tasks-jsf: JSF, JPA quickstart
==============================

Author: [Lukas Fryc](https://community.jboss.org/people/lfryc)


What is it?
-----------

This is your project! It's a sample Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss EAP 6. 
This project is setup to allow you to use JPA 2.0 persistence with JSF 2.0 as view layer.

The theme of this application is simple Task management with simple log in. The [roject contains two entities - a user and a task.

This sample includes a persistence unit and some sample persistence code to help you get your feet wet with database access in enterprise Java.

Persistence code is covered by tests to help you write business logic without the need to use any view layer.

JSF 2.0 is used to present user two views - authentication form and task view.

The task view is contains a task list, a task detail and a task addition form. The task view uses AJAX.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


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
2. Open a command line and navigate to the root of the tasks-jsf quickstart directory.
3. Type the following in the command line: 
    For JBoss Enterprise Application Platform 6, Maven user settings NOT configured: 

        mvn clean package jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

    For JBoss AS 7 or JBoss Enterprise Application Platform 6, Maven user settings configured: 

        mvn clean package jboss-as:deploy

4. This will build and deploy `ear/target/jboss-as-tasks-jsf.ear`.
5. To undeploy the application, run this command:

        mvn jboss-as:undeploy

You can also use Eclipse to start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server and deploy the project. See the <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-tasks-jsf>.

Running the Arquillian tests
============================

Integration tests written in Arquillian give you the opportunity to check the application's logic before accessing the view, leading to a better development experience.

By default, the tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. You can activate this test by selecting one of the container configurations provided for JBoss Enterprise Application Platform 6 or JBoss AS 7.

Testing on Remote Server
-------------------------
 
First you need to start JBoss Enterprise Application Platform 6 or JBoss AS 7. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

Now, run the test goal with the `arq-jbossas-remote` profile activated:

    mvn clean test -Parq-jbossas-remote

Testing on Managed Server
-------------------------
 
Arquillian will start the container for you. All you have to do is setup the path to JBoss Enterprise Application Platform 6 or JBoss AS 7. Edit `src/test/resources/arquillian` and set `jbossHome`.

To run the test in JBoss Enterprise Application Platform 6 or JBoss AS 7, run the test goal with the following profile activated:

    mvn clean test -Parq-jbossas-managed

Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
