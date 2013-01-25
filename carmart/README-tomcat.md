carmart: Basic Infinispan example
=================================
Author: Tristan Tarrant, Martin Gencur
Level: Intermediate
Technologies: Infinispan, CDI
Summary: Shows how to use Infinispan instead of a relational database.

What is it?
-----------

CarMart is a simple web application that uses Infinispan instead of a relational database.

Users can list cars, add new cars or remove them from the CarMart. Information about each car is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, etc.

The CarMart quickstart can work in two modes: "library" and "client-server", however only "library" mode is supported in JBoss Enterprise Web Server or Tomcat. In library mode, all libraries (jar files) are bundled with the application and deployed to the server. Caches are configured programmatically and run in the same JVM as the web application. In client-server mode, the web application bundles only HotRod client and communicates with a remote JBoss Data Grid (JDG) server.
 
When running this quickstart on JBoss Enterprise Web Server 2 or Tomcat 7, you must use only the "library-tomcat" maven profile. This profile only enables "library" mode.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Web Server 2 or Tomcat 7. 

 
Configure JBoss Enterprise Web Server 2 or Tomcat 7
---------------------------------------------------

Before starting EWS/Tomcat, add the following lines to `conf/tomcat-users.xml` to allow the Maven Tomcat plugin to access the manager application:

        <role rolename="manager-script"/>
        <user username="admin" password="SOMEPASSWD" roles="manager-script"/>
        
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


Add a `<server>` element into your Maven settings.xml with `<id>` equal to tomcat and correct credentials:

        <server>
            <id>tomcat</id>
            <username>admin</username>
            <password>SOMEPASSWD</password>
        </server>

        
Start JBoss Enterprise Web Server 2 or Tomcat 7
-----------------------------------------------

1. Open a command line and navigate to the root of the EWS/Tomcat server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   TOMCAT7_HOME/bin/catalina.sh run
        For Windows: TOMCAT7_HOME\bin\catalina.bat run


Build and Deploy the Application in Library Mode
-----------------------------------------------

1. Make sure you have started EWS/Tomcat as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn -Plibrary-tomcat clean package tomcat:deploy
        
4. This will deploy `target/jboss-as-carmart.war` to the running instance of EWS/tomcat.


Access the application
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-carmart/>


Undeploy the Archive
--------------------

1. Make sure you have started Tomcat/EWS as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

    `mvn -Plibrary-tomcat tomcat:undeploy `
