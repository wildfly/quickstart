log4j: How to Define a Module Dependency and Use log4j in an Application  
=======================================================================
Author: Bartosz Baranowski

What is it?
-----------

This simple JSF 2.0 application shows how to use container defined modules 
to limit the size of the application package. It also shows how to use 
common versions of certain classes at runtime.

Applications must often depend on third party libraries.
By default, Java EE packages allow you to include dependencies in a deployable unit
which can lead to uncontrolled growth of the deployable unit. This can be avoided by 
the use of a container defined module. A modules is nothing more than a container
managed binary dependency, which is shared by all deployed applications.

For more details on class loading and modules please refer to 
<a href="https://docs.jboss.org/author/display/AS7/Class+Loading+in+AS7" title="Class Loading in AS7">Class Loading in AS7</a>  

This example is very simple. It declares dependency on Apache Log4j module to
allow itself to use custom logging framework.This is achieved with a simple addition to the xml file: 
<b>src/main/webapp/WEB-INF/jboss-deployment-structure.xml</b>.
For more details about this file please refer to 
<a href="https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile"title="JBoss Deployment Structure File">JBoss Deployment Structure File</a>  



System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or JBoss Enterprise Application Platform 6.
The following instructions target JBoss AS 7, but they also apply to JBoss Enterprise Application Platform 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6). To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-log4j.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-log4j/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc