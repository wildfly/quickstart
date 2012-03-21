ejb-in-ear: Deployment of an EAR Containing a JSF WAR and an EJB JAR
====================================================================
Author: Paul Robinson

What is it?
-----------

This example demonstrates the deployment of an EAR artifact. The EAR contains: *JSF 2.0* WAR and an *EJB 3.1* JAR.

The example is composed of three maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB code and can be built independently to produce the JAR archive.

2. `web`: This project contains the JSF pages and the managed bean.

3. `ear`: This project builds the EAR artifact and pulls in the ejb and web artifacts.

The root `pom.xml` builds each of the subprojects in the above order and deploys the EAR archive to the server.


The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking _Greet_, the name is sent to a managed bean (`Greeter`).
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected to the managed bean (notice the field annotated with `@EJB`).
4. The response from invoking the `GreeterEJB` is stored in a field (message) of the managed bean.
5. The managed bean is annotated as `@SessionScoped`, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is
displayed to the user.

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
2. Open a command line and navigate to the root of the ejb-in-ear quickstart directory.
3. Type the following in the command line: 
    For JBoss Enterprise Application Platform 6, Maven user settings NOT configured: 

        mvn clean package jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

    For JBoss AS 7 or JBoss Enterprise Application Platform 6, Maven user settings configured: 

        mvn clean package jboss-as:deploy

4. This will build and deploy `ear/target/jboss-as-ejb-in-ear.ear`.
5. To undeploy the application, run this command:

        mvn jboss-as:undeploy

You can also use Eclipse to start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server and deploy the project. See the <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-in-ear>.

Enter a name in the input field and click the _Greet_ button to see the response.

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, then all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.

If you created the project from the command line using `archetype:generate`, you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.

Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
