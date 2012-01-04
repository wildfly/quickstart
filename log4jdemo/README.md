jboss-as-log4jdemo
========================

What is it?
-----------

This is a simple JSF 2.0 application which will teach you how easy it is to
use container defined modules to limit size of application package and use 
common version of certain classes at runitme.

It is common for different applications to depend on third party libraries.
By default, Java EE packages allow to include dependencies in deployable unit.
This however may lead to uncontrolled growth of deployable unit. To avoid that, 
one can use container defined module. Module is nothing more than container
managed binary dependency, which is shared by all deployed applications.

For more details on class loading and modules please refer to <https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile> 

This example is very simple. It declares dependecy on Apache Log4j module to
allow itself to use custom logging framework.This example achieves this with simple 
xml file: <b>src/main/webapp/WEB-INF/jboss-deployment-structure.xml</b>.
For more details about this file please refer to <https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile>


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (or EAP 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-log4jdemo.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-log4jdemo/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc