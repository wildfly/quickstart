hibernate3: How to Use  Hibernate 3 in an Application
=====================================================
Author: Bartosz Baranowski 

What is it?
-----------

This is a simple JSF 2.0 example based on hibernate4 quickstart. Its purpose is to demonstrate how you can use Hibernate 3 in your application.

This quickstart, like the log4j quickstart, demonstrates how to define a module dependency. However, this quickstart goes beyond that and also demonstrates the following:
 
    * WAR creation - The Maven script and Maven WAR plugin create a *WAR* archive that includes ONLY the Hibernate 3.x binaries. To understand better how this is achieved, please refer to the *pom.xml* in the root directory of this quickstart. Additional information can be found in the <http://maven.apache.org/plugins/maven-war-plugin> documentation.
    * Module exclusion and inclusion - This example demonstrates how to control class loading using *dependencies* and *exclusions* in the *jboss-deployment-structure.xml* file. For more information about this file, please refer to <https://docs.jboss.org/author/display/AS7/Developer+Guide#DeveloperGuide-JBossDeploymentStructureFile>
    * Persistence configuration - Configuration is required to tell the container how to load JPA/Hibernate.
 
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

This will deploy `target/jboss-as-hibernate3.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-hibernate3/>.

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
