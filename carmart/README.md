carmart: Basic Infinispan example
=================================
Author: Tristan Tarrant, Martin Gencur  
Level: Intermediate  
Technologies: Infinispan, CDI  
Summary: Shows how to use Infinispan instead of a relational database.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart>  

What is it?
-----------

CarMart is a simple web application that uses Infinispan Cache instead of a relational database.

Users can list cars, add new cars, or remove them from the CarMart. Information about each car is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, and more.

The CarMart quickstart application and the data grid run in the same JVM. All libraries (JAR files) are bundled with the application and deployed to JBoss WildFly.  The library usage mode only allows local access to a single node in a distributed cluster. This usage mode gives the application access to data grid functionality within a virtual machine in the container being used.

System requirements
-------------------

All you need to build this project is Java 8 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


Start JBoss WildFly
-----------------------------------------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Application in Library Mode
-----------------------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
        
4. This will deploy `target/wildfly-carmart.war` to the running instance of the server.
 

Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-carmart/>


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
