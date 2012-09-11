bmt: Bean Managed Transactions - Stepping Outside the Container (with JPA and JTA)
=================================================================================
Author: Mike Musgrove
Level: Intermediate
Technologies: EJB, Bean Managed Transactions (BMT)
Summary: EJB that demonstrates bean-managed transactions (BMT)
Target Product: EAP

What is it?
-----------

On occasion, the application developer requires finer grained control over the lifecycle of JTA transactions and JPA Entity Managers than the defaults provided by the Java EE container. This example shows how the developer can override these defaults and take control of aspects of the lifecycle of JPA and transactions.

This example demonstrates how to manually manage transaction demarcation while accessing JPA entities in JBoss Enterprise Application Platform 6 or JBoss AS 7.

When you run this example, you will be provided with a `Use bean managed Entity Managers` checkbox.
* If you check the checkbox, it shows the developer responsibilities when injecting an Entity Manager into a managed (stateless) bean.
* If you uncheck the checkbox, shows the developer responsibilities when using JPA and transactions with an unmanaged component.

JBoss Enterprise Application Platform 6 and JBoss AS 7 ship with H2, an in-memory database written in Java. This example shows how to transactionally insert key value pairs into the H2 database and demonstrates the requirements on the developer with respect to the JPA Entity Manager.

_NOTE: A Java EE container is designed with robustness in mind, so you should carefully analyse the scaleabiltiy, concurrency and performance needs of you application before taking advantage of these techniques in your own applications._


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
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

4. This will deploy `target/jboss-as-bmt.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-bmt/>.

You will be presented with a simple form for adding key/value pairs and a checkbox to indicate whether the updates should be executed using an unmanaged component. Effectively this will run the transaction and JPA updates in the servlet, not session beans. If the box is checked then the updates will be executed within a session bean method.

1. To list all pairs leave the key input box empty. 
2. To add or update the value of a key fill in the key and value input boxes. 
3. Press the submit button to see the results.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
