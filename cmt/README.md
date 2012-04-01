cmt: Container Managed Transactions - Example Using Transactions That Are Managed by the Container 
==================================================================================================
Author: Tom Jenkinson

## What is it?

This example demonstrates how to use transactions managed by the container. It is a fairly typical scenario of updating a database and sending a JMS message in the same transaction. A simple message-driven bean, or MDB, is provided that prints out the message that is sent. This MDB is not transactional and provided only for debugging purposes.

Aspects touched upon in the code:

1. XA transaction control using the container managed transaction annotations
2. XA access to a H2 database using the JPA API
3. XA access to a JMS queue

### What are container managed transactions?

Prior to EJB, getting the right incantation to ensure sound transactional operation of the business logic was a highly specialised skill. Although this still holds true to a great extent, EJB has provided a series of improvements to to allow simplified transaction demarcation notation that is therefore easier to read and test. 

With CMT, the EJB container sets the boundaries of a transaction. This differs from BMT (bean managed transactions) where the developer is responsible for initiating and completing a transaction via the methods begin, commit, rollback on a <code>javax.transaction.UserTransaction</code>.

### What makes this an example of container managed transactions?

Take a look at <code>org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJBImpl</code>. You can see that this stateless session bean has been marked up with the @javax.ejb.TransactionAttribute annotation.

The available options for this annotation are as follows:

* Required - As demonstrated in the example. If a transaction does not already exist, this will initiate a transaction and	complete it for you, otherwise the business logic will be integrated into the existing transaction.
* RequiresNew - If there is already a transaction running, it will be suspended, the work performed within a new transaction which is completed at exit of the method and then the original transaction resumed. 
* Mandatory - If there is no transaction running, calling a business method with is annotated	with this will result in an error.
* NotSupported - If there is a transaction running, it will be suspended and no transaction will be initiated for this business method.
* Supports - This will run the method within a transaction if a transaction exists, alternatively, if there is no transaction running the method will not be executed within the scope of a transaction. 
* Never - If the client has a transaction running and does not suspend it but calls a method annotated with Never then an EJB exception will be raised.


JBoss Enterprise Application Platform 6 and JBoss AS 7 ship with H2, an in-memory database written in Java. This example shows how to transactionally insert key value pairs into the H2 database and demonstrates the requirements on the developer with respect to the JPA Entity Manager.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml
 

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.html/#buildanddeploy) for complete instructions and additional options._


#### Build and Deploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

            mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-cmt.war` to the running instance of the server.

#### Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to undeploy the archive:

            mvn jboss-as:undeploy


Access the application 
---------------------

The application will be running at the following URL:  <http://localhost:8080/jboss-as-cmt/>.

You will be presented with a simple form for adding customers to a database.

After a user is successfully added to the database, a message is produced containing the details of the user. An example MDB will dequeue this message and print the following contents:
	
    Received Message: Created customer named: Tom with ID: 1



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.html/#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc




