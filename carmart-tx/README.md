carmart-tx: Basic infinispan example with transactions
=================================================================
Author: Tristan Tarrent, Martin Gencur
Level: Intermediate
Technologies: Infinispan, CDI
Summary: Show how to use Infinispan instead of a relational database with transactions enabled.

What is it?
-----------

Transactional CarMart is a simple web application that uses Infinispan instead of a relational database.

Users can list cars, add new cars or remove them from the CarMart. Information about each car is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, etc.

The Transactional CarMart quickstart works in a library mode. All libraries (jar files) are bundled with the application and deployed into the server. Caches are configured programatically and run in the same JVM as the web application.

All operations are done in a transactional context. In order to run the application in Tomcat, a standalone transaction manager from JBoss Transactions is used.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


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
        
4. This will deploy `target/carmart-quickstart-tx.war` to the running instance of the server.
 

Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-as-carmart-tx/>

Users can list cars, add new cars or remove them from the CarMart. Information about each car is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, etc.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

