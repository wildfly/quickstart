helloworld-websocket: WebSocket Hello World Example
===================
Author: Jeff Mesnil  
Level: Beginner  
Technologies: Web Socket  
Summary: Basic Web Socket |Demonstrates the use of Web Socket server endpoints  
Target Product: ${product.name}  
Source: <${github.repo.url}>    

What is it?
-----------

This example demonstrates the use of *Web Socket* in *${product.name}*.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

System requirements
-------------------

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

The application this project produces is designed to be run on ${product.name}.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are required.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

### Deploying locally

First you need to start the JBoss container. To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn clean package

You can now deploy the artifact by executing the following command:

    mvn wildfly:deploy

This will deploy both the client and service applications.

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

To undeploy run this command:

    mvn wildfly:undeploy

You can also start the JBoss container and deploy the project using JBoss Tools. See the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>
for more information.

Importing the project into an IDE
=================================

Detailed instructions for using Eclipse / JBoss Tools with are provided in the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
