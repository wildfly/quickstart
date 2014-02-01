cdi-alternative: Demostrates CDI Alternatives
======================================================
Author: Nevin Zhu
Level: Intermediate
Technologies: CDI, Servlet, JSP
Summary: Demonstrates the use of CDI Alternatives where the bean is selected during deployment 
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

When more than one version of a bean is implemented for different purposes, the ability to switch between the versions during the development phase by injecting one qualifier or another is shown in this demo.

Instead of having to change the source code of the application, one can make the choice at deployment time by using alternatives.

Alternatives are commonly used for purposes like the following:

1. To handle client-specific business logic that is determined at runtime.
2. To specify beans that are valid for a particular deployment scenario, for example, when country-specific sales tax laws require country-specific sales tax business logic.
3. To create dummy or mock versions of beans to be used for testing.

Any java class which has a no-args constructor and is in an archive with a beans.xml is available for lookup and injection. 
For EL resolution, it must contain @Named


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss WildFly
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
        
4. This will deploy `target/cdi-alternative.ear` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL <http://localhost:8080/cdi-alternative>.

You can specify alternative versions of the bean in the WEB-INF/beans.xml file by doing one of the following:

1. you can remove the '<alternatives>' tag
2. you can change the class name.

In this quickstart, in order to switch back to the default implementation, 
comment the '<alternatives>' block in the WEB-INF/beans.xml file and redeploy the quickstart.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

