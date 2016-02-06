cdi-alternative: Demonstrates CDI Alternatives
======================================================
Author: Nevin Zhu  
Level: Intermediate  
Technologies: CDI, Servlet, JSP  
Summary: The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code.   
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code in Red Hat JBoss Enterprise Application Platform. Instead, you choose the bean implementation during development by injecting a qualifier. Then at deployment time, rather than modify the source code, you choose the alternative.

Alternatives are commonly used for purposes like the following:

1. To handle client-specific business logic that is determined at runtime.
2. To specify beans that are valid for a particular deployment scenario, for example, when country-specific sales tax laws require country-specific sales tax business logic.
3. To create dummy or mock versions of beans to be used for testing.

Any java class which has a no-args constructor and is in an archive with a beans.xml is available for lookup and injection. 
For EL resolution, it must contain @Named


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy
        
4. This will deploy `target/wildfly-cdi-alternative.war` to the running instance of the server.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-cdi-alternative>.

You can specify alternative versions of the bean in the WEB-INF/beans.xml file by doing one of the following:

1. You can remove the `<alternatives>` tag
2. You can change the class name.

In this quickstart, in order to switch back to the default implementation, 
comment the `<alternatives>` block in the WEB-INF/beans.xml file and redeploy the quickstart.

Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

