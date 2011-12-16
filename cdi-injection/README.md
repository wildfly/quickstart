CDI Injection
=======================

What is it?
-----------

This example demonstrates the use of *CDI 1.0 Injection and Qualifiers* in *JBoss AS 7* with JSF as the
front-end client.

You can test the output at the URL http://localhost:8080/jboss-as-cdi-injection

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.

With the prerequisites out of the way, you're ready to build and deploy.


Deploying the Application
-------------------------

First you need to start JBoss AS 7. To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn clean package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-cdi-injection.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-cdi-injection/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
