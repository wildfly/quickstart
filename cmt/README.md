cmt: Container Managed Transactions - Example Using Transactions That Are Managed by the Container 
==================================================================================================
Author: Tom Jenkinson
Level: Intermediate
Technologies: EJB, Container Managed Transactions (CMT)
Summary: EJB that demonstrates container-managed transactions (CMT)
Target Product: EAP

## What is it?

This quickstart demonstrates using transactions managed by the container. It is a fairly typical scenario of updating a database and sending a JMS message in the same transaction. A simple MDB is provided that prints out the message sent but this is not a transactional MDB and is purely provided for debugging purposes.

Aspects touched upon in the code:

1. XA transaction control using the container managed transaction annotations
2. XA access to a PostgreSQL database using the JPA API
3. XA access to a JMS queue

After users complete this quickstart, they are invited to run through the following quickstarts:

1. _jts_ - The JTS quickstart builds upon this quickstart by distributing the CustomerManager and InvoiceManager
2. _jts-distributed-crash-rec_ - The crash recovery quickstart builds upon the _jts_ quickstart by demonstrating the fault tolerance of JBossAS

### What are container managed transactions?

Prior to EJB, getting the right incantation to ensure sound transactional operation of the business logic was a highly specialised skill. Although this still holds true to a great extent, EJB has provided a series of improvements to to allow simplified transaction demarcation notation that is therefore easier to read and test. 

With CMT, the EJB container sets the boundaries of a transaction. This differs from BMT (bean managed transactions) where the developer is responsible for initiating and completing a transaction via the methods begin, commit, rollback on a <code>javax.transaction.UserTransaction</code>.

### What makes this an example of container managed transactions?

Take a look at <code>org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJBImpl</code>. You can see that this stateless session bean has been marked up with the @javax.ejb.TransactionAttribute annotation.

The available options for this annotation are as follows:

* Required - As demonstrated in the quickstart. If a transaction does not already exist, this will initiate a transaction and complete it for you, otherwise the business logic will be integrated into the existing transaction
* RequiresNew - If there is already a transaction running, it will be suspended, the work performed within a new transaction which is completed at exit of the method and then the original transaction resumed. 
* Mandatory - If there is no transaction running, calling a business method with is annotated with this will result in an error
* NotSupported - If there is a transaction running, it will be suspended and no transaction will be initiated for this business method
* Supports - This will run the method within a transaction if a transaction exists, alternatively, if there is no transaction running the method will not be executed within the scope of a transaction 
* Never - If the client has a transaction running and does not suspend it but calls a method annotated with Never then an EJB exception will be raised.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Configure the application server to use PostgreSQL
--------------------------------------------------

This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Install and Configure the PostgreSQL Database](../README.md#postgresql)

_Note_: For the purpose of this quickstart, replace the word QUICKSTART_DATABASENAME with `cmt-quickstart-database` in the PostgreSQL instructions.

1. Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.
2. [Add the PostgreSQL Module](../README.md#addpostgresqlmodule) to the JBoss server `modules/` directory.
3. [Add the PostgreSQL driver](../README.md#addpostgresqldriver) to the JBoss server configuration file.

Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml
 

Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._


1. Make sure you have started the JBoss Server with the PostgreSQL driver
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-cmt.war` to the running instance of the server.

Access the application 
---------------------
To access the application type the following into a browser: <http://localhost:8080/jboss-as-cmt/>

The application will be running at the following URL: <http://localhost:8080/jboss-as-cmt/>.

You will be presented with a simple form for adding customers to a database.

After a user is successfully added to the database, a message is produced containing the details of the user. An example MDB will dequeue this message and print the following contents:

    Received Message: Created invoice for customer named:  Tom


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
