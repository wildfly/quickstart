helloworld-html5: POH5 Hello World Example
===================

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7* using the POH5 architecture.
POH5 is basically a smart, HTML5+CSS3+JavaScript front-end using RESTful services on the backend.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on JBoss AS 7 or JBoss Enterprise Application Platform 6.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are
required.

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

    mvn package

You can now deploy the artifact by executing the following command:

    mvn jboss-as:deploy

This will deploy both the client and service applications.

The application will be running at the following URL <http://localhost:8080/jboss-as-helloworld-html5/>.

To undeploy run this command:

    mvn jboss-as:undeploy

You can also start the JBoss container and deploy the project using JBoss Tools. See the
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>
for more information.

### Deploying to OpenShift

You can also deploy the application directly to OpenShift, Red Hat's cloud based PaaS offering, follow the instructions [here](https://community.jboss.org/wiki/DeployingHTML5ApplicationsToOpenshift)

Importing the project into an IDE
=================================

Detailed instructions for using Eclipse / JBoss Tools with are provided in the
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

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

Development notes
=================

You can test the REST endpoint using the following URL
http://localhost:8080/jboss-as-helloworld-html5/hello/json/David

HelloWorld.java - establishes the RESTful endpoints using JAX-RS

Web.xml - maps RESTful endpoints to "/hello"

index.html - is a jQuery augmented plain old HTML5 web page

Copyright headers
-----------------

To update the copyright headers, just run `mvn license:format -Dyear=<current year>`


