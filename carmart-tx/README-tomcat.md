Transactional CarMart: Basic infinispan example with transactions
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


Build and Deploy to Tomcat 7
----------------------------------

1) This build assumes you will be running Tomcat 7 in its default configuration, with a hostname of localhost and port 8080. Before starting Tomcat, add the following lines to `conf/tomcat-users.xml` to allow the Maven Tomcat plugin to access the manager application:

        <role rolename="manager-script"/>
        <user username="admin" password="SOMEPASSWD" roles="manager-script"/>
    
2) Start Tomcat 7

        $CATALINA_HOME/bin/catalina.sh run

3) Build the application

        mvn clean package -Plibrary-tomcat

4) Add a `<server>` element into your Maven settings.xml with `<id>` equal to tomcat and correct credentials:

        <server>
            <id>tomcat</id>
             <username>admin</username>
             <password>SOMEPASSWD</password>
        </server>

5) Deploy the application via tomcat Maven plugin

        mvn tomcat:deploy -Plibrary-tomcat

6) Go to <http://localhost:8080/jboss-as-carmart-tx>

7) Undeploy the application

        mvn tomcat:undeploy -Plibrary-tomcat
