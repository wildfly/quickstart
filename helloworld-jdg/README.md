helloworld-jdg: Basic Infinispan example
====================================
Author: Burr Sutter, Martin Gencur
Level: Intermediate
Technologies: Infinispan, CDI
Summary: Shows how to use Infinispan in clustered mode, with expiration enabled.
Target Product: JDG

What is it?
-----------

HelloWorld-JDG is a basic example that shows how to store and retrieve data to/from the cache. Users can access the cache
either from a servlet or from a JSF page through request scoped beans.

Infinispan is configured in clustered distributed mode with synchronous replication. Entries have their lifespan (expiration)
and are removed from the cache after 60 seconds since last update.

HelloWorld-JDG example works in _Library mode_. In this mode, the application and the data grid are running in the same
JVM. All libraries (JAR files) are bundled with the application and deployed to JBoss Enterprise Application Platform 6
or JBoss AS 7.  The library usage mode only allows local access to a single node in a distributed cluster. This usage
mode gives the application access to data grid functionality within a virtual machine in the container being used.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start first instance of JBoss Enterprise Application Platform 6 or JBoss AS 7
-----------------------------------------------------------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Start second instance of JBoss Enterprise Application Platform 6 or JBoss AS 7
------------------------------------------------------------------------------

1. Make a second copy of the JBoss Server
2. Open a command line and navigate to the root of the second JBoss server directory.
3. Start the server with pre-configured port offset so that the server can run on the same host

        For Linux:   JBOSS_HOME2/bin/standalone.sh -Djboss.socket.binding.port-offset=100
        For Windows: JBOSS_HOME2\bin\standalone.bat -Djboss.socket.binding.port-offset=100

 
Build and Deploy the Quickstart
-------------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must
include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy)
for complete instructions and additional options._

1. Make sure you have started both instances of the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive to the first server:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-helloworld-jdg.war` to the first running instance of the server.
5. Type this command to build and deploy the archive to the second server (running on different ports):

        mvn clean package jboss-as:deploy -Djboss-as.port=10099

6. This will deploy `target/jboss-as-helloworld-jdg.war` to the second running instance of the server.


Access the application 
----------------------

The application will be running at the following URLs:

   <http://localhost:8080/jboss-as-helloworld-jdg>  (first server instance)
   <http://localhost:8180/jboss-as-helloworld-jdg>  (second server instance)

You can test replication of entries in the following way:

1. Access first server at <http://localhost:8080/jboss-as-helloworld-jdg> and insert key "foo" with value "bar"
2. Access second server at <http://localhost:8180/jboss-as-helloworld-jdg> and do the following:
   * Click on "Get Some"
   * Get the value for key "foo"
   * Click "Put Some More"
   * Insert key "mykey" with value "myvalue"
3. Access the first server at <http://localhost:8080/jboss-as-helloworld-jdg> and do the following:
   * Click on "Get Some"
   * Get all mappings by clicking on "Get All"
4. All data entered on each server was replicated to the other server

NOTE: Entries expire and simply disappear after 60 seconds from last update.

To access predefined servlets and directly store/retrieve a key in the cache, access the following URLs:

<http://localhost:8080/jboss-as-helloworld-jdg/TestServletPut>
<http://localhost:8180/jboss-as-helloworld-jdg/TestServletGet>  (note the different port 8180)


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive from both running servers:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy -Ddeploy.port=10099


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information,
see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following
commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

