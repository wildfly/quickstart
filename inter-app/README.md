Inter-app: Shows how to communicate between two applications using EJB and CDI 
==============================================================================
Author: Pete Muir
Level: Advanced
Technologies: EJB, CDI, JSF
Summary: Shows how to communicate between two applications using EJB and CDI
Target Product: EAP

What is it?
-----------

This quickstart shows you how to easily communicate between two modular deployments to JBoss Enterprise Application Platform 6 or JBoss AS 7. Two wars, with a shared API jar, are deployed to the app server. EJB is used to provide inter-application communication, with EJB beans alised to CDI beans, making the inter-application communication transparent to clients of the bean.

CDI only provides intra-applicaion injection (i.e within a top level deployment, ear, war, jar etc). This improves performance of the application server, as to satisfy an injection point all possible candidates have to be scanned / analyzed. If inter-app injection was supported by CDI, performance would scale according to the number of deployments you have (the more deployments in the running system, the slower the deployment). Java EE injection uses unique JNDI names for the wiring, so each injection point is O(1). The approach shown here combines the two approaches such that you limit the name based wiring to one location in your code, and the main consumers of components can use CDI injection to reference these name wired components. For the name approach to work though, you still need to publish instances, and EJB singletons allow you to do that with just one extra annotation.


In all, the project has three modules:

* `jboss-as-inter-app-shared.jar` - this module contains the interfaces which define the contract between the beans exposed by the wars. It is deployed as a module
* `jboss-as-inter-app-A.war` - the first war, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appB
* `jboss-as-inter-app-B.war` - the second war, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appA

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.

Start JBoss Enterprise Application Platform 6 or JBoss AS 7
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

        mvn clean package jboss-as:deploy
4. This will deploy `shared/target/jboss-as-inter-app-shared.jar`, `appA/target/jboss-as-inter-app-A.war` and `appB/target/jboss-as-inter-app-B.war` to the running instance of the server.

Access the application (For quickstarts that have a UI component)
---------------------


Access the running application in a browser at the following URLs:

* <http://localhost:8080/jboss-as-inter-app-A>
* <http://localhost:8080/jboss-as-inter-app-B>

You are presented with a form that allows you to set the value on the bean in the other application, as well as display of the value on this application's bean. Enter a new value and press "Update and Send!" to update the value on the other application. Do the same on the other application, and hit the button again on the first application. You should see the values shared between the applications.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

------------------------------------

