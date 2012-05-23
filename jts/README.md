jts: Java Transaction Service - Distributed EJB Transactions Across Multiple Containers 
======================================================================================
Author: Tom Jenkinson
Level: Intermediate
Technologies: JTS
Summary: Uses Java Transaction Service (JTS) to coordinate distributed transactions
Prerequisites: cmt

Pre-requisites
--------------

Developers should be familiar with the concepts introduced in the _cmt_ quickstart.


What is it?
-----------

This example demonstrates how to perform distributed transactions in an application. A distributed transaction is a set of operations performed by two or more nodes, participating in an activity coordinated as a single entity of work, and fulfilling the properties of an ACID transaction. 

ACID is a set of 4 properties that guarantee the resources are processed in the following manner:

* Atomic - if any part of the transaction fails, all resources remain unchanged. 
* Consistent - the state will be consistent across resources after a commit
* Isolated - the execution of the transaction for each resource is isolated from each others
* Durable - the data will persist after the transaction is committed


The example uses Java Transaction Service (JTS) to propagate a transaction context across two Container-Managed Transaction (CMT) EJBs that, although deployed in separate servers, participate in the same transaction. In this example, one server processes the Customer and Account data and the other server processes the Invoice data.

The code base is essentially the same as the _cmt_ quickstart, however in this case the <code>InvoiceManager</code>
has been separated to a different deployment archive to demonstrate the usage of JTS. You can see the changes in the 
following ways:

1. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/InvoiceManagerEJB.java` has been moved to `application-component-2/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/InvoiceManagerEJB`
2. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/CustomerManagerEJB.java` has been moved to `jts/application-component-1/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/CustomerManagerEJB.java`

The changes to `CustomerManagerEJB` are purely to accommodate the fact that `InvoiceManager` is now distributed.

You will see that the `CustomerManagerEJB` uses the EJB home for the remote EJB, this is expected to connect to remote EJBs. The example expects the EJBs to be deployed onto the same physical machine. This is not a restriction of JTS and the example can easily be converted to run on separate machines by editing the hostname value for the `InvoiceManagerEJB` in `org.jboss.as.quickstarts.cmt.jts.ejb.CustomerManagerEJB`.

A simple MDB has been provided that prints out the messages sent but this is not a transactional MDB and is purely provided for debugging purposes.

After users complete this quickstart, they are invited to run through the following quickstart:

1. _jts-distributed-crash-rec_ - The crash recovery quickstart builds upon the _jts_ quickstart by demonstrating the fault tolerance of JBossAS.


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

_Note_: For the purpose of this quickstart, replace the word QUICKSTART_DATABASENAME with cmt-quickstart-database in the PostgreSQL instructions.

Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.

Wait until a later in these instructions to add the PostgreSQL module and driver configuration to the JBoss server.

Prerequisites
------------------

This quickstart requires the configuration of two servers. The first server must be configured to use the PostgreSQL database. Instructions to install and configure PostgreSQL for will be provided when you configure the JBoss servers.


Configure the JBoss servers
---------------------------

For this example, you will need two instances of the application server, with a subtle startup configuration difference. Application server 2 must be started up with a port offset parameter provided to the startup script as "-Djboss.socket.binding.port-offset=100"

The application servers should both be configured as follows:

1. Open the file JBOSS_HOME/standalone/configuration/standalone-full.xml
2. Enable JTS as follows:
    * Find the orb subsystem and change the configuration to:  

            <subsystem xmlns="urn:jboss:domain:jacorb:1.2">
                <orb>
                    <initializers security="on" transactions="on"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and append the `<jts/>` element:  

            <subsystem xmlns="urn:jboss:domain:transactions:1.2">
                <!-- LEAVE EXISTING CONFIG AND APPEND THE FOLLOWING -->
                <jts/>
            </subsystem>
3. Make a copy of this JBoss directory structure to use for the second server.

4. Application server 1 must be configured to use PostgreSQL as per the instructions in [Install and Configure the PostgreSQL Database] (../README.md#postgresql).
    * Be sure to start the PostgreSQL database.
    * Be sure to [add the PostgreSQL Module](../README.md#addpostgresqlmodule) to the Application 1 server.
    * Be sure to [add the PostgreSQL driver](../README.md#addpostgresqlmodule) to the Application 1 server configuration file.

_Note_: For the purpose of this quickstart, replace the word QUICKSTART_DATABASENAME with jts-quickstart-database in the PostgreSQL instructions.

_IMPORTANT_: After you have finished with the quickstart, if you no longer wish to use JTS, it is important to restore your backup from step 1 above.


Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Servers
-------------------------

If you are using Linux:

        Server 1: JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        Server 2: JBOSS_HOME_SERVER_2/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
        Server 2: JBOSS_HOME_SERVER_2\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100


Build and Deploy the Quickstart
-------------------------

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.


1. Make sure you have started the JBoss server with the PostgreSQL driver
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `application-component-1/target/jboss-as-jts-application-component-1.war` and `application-component-2/target/jboss-as-jts-application-component-2.jar` to the running instance of the server.

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-jts-application-component-1/>.

When you enter a name and click to "Add" that customer, you will see the following in the application server 1 console:
    
    14:31:48,334 WARNING [javax.enterprise.resource.webcontainer.jsf.renderkit] (http-localhost-127.0.0.1-8080-1) Unable to find component with ID name in view.
    14:31:50,457 ERROR [jacorb.orb] (http-localhost-127.0.0.1-8080-1) no adapter activator exists for jts-quickstart&%InvoiceManagerEJBImpl&%home
    14:31:50,767 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.5.Final
    14:31:51,430 WARN  [com.arjuna.ats.jts] (RequestProcessor-5) ARJUNA022261: ServerTopLevelAction detected that the transaction was inactive

You will also see the following in application-server-2 console:

    14:31:50,750 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.5.Final
    14:31:51,395 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1567863645)) Received Message: Created invoice for customer named: Tom

The web page will also change and show you the new list of customers.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package jboss-as:undeploy


<a id="remove-jts-configuration"></a>
Remove the JTS Configuration from the JBoss server
---------------------------

You must remove the JTS server configuration you did during setup because it interferes with the JTA quickstarts.

1. Stop the server.
2. Open the file JBOSS_HOME/standalone/configuration/standalone-full.xml
3. Disable JTS as follows:
    * Find the orb subsystem and change the configuration back to:  

            <subsystem xmlns="urn:jboss:domain:jacorb:1.2">
                <orb>
                    <initializers security="on" transactions="spec"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and remove the `<jts/>` element:  

            <subsystem xmlns="urn:jboss:domain:transactions:1.2">
                <!-- LEAVE EXISTING CONFIG AND REMOVE THE </jts> -->
            </subsystem>

