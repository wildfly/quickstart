ejb-in-ear
========================

What is it?
-----------

This example demonstrates the deployment of an EAR artifact. The EAR contains: *JSF 2.0* WAR and an *EJB 3.1* JAR.

The example is composed of three maven projects, each with a shared parent. The projects are as follows:

1. ejb
This project contains the EJB code and can be built independently to produce the JAR archive.

2. web
This project contains the JSF pages and the managed bean.

3. ear
This project builds the EAR artifact and pulls in the ejb and web artifacts.

The root pom builds each of the projects in the above order and deploys the EAR archive to the server.


The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking 'Greet', the name is sent to a managed bean (Greeter).
3. On setting the name, the Greeter invokes the GreeterEJB, which was injected to the managed bean (notice the field annotated with @EJB).
4. The response from invoking the GreeterEJB is stored in a field (message) of the managed bean.
5. The managed bean is annotated as @SessionScoped, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is
displayed to the user.

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

This will deploy `ear/target/jboss-as-ejb-in-ear.ear`.

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-in-ear>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the
JBoss AS 7 Getting Started Guide for Developers.

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

