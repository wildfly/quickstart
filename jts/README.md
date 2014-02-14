jts: Java Transaction Service - Distributed EJB Transactions Across Multiple Containers 
======================================================================================
Author: Tom Jenkinson
Level: Intermediate
Technologies: JTS
Summary: Uses Java Transaction Service (JTS) to coordinate distributed transactions
Prerequisites: cmt
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>


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


Prerequisites
------------------

Developers should be familiar with the concepts introduced in the _cmt_ quickstart.

This quickstart requires the configuration of two servers. The first server must be configured to use the PostgreSQL database. Instructions to install and configure PostgreSQL are below.


Install the PostgreSQL Database
-------------------------------

This quickstart requires the PostgreSQL database. Instructions to install an configure PostgreSQL can be found here: [Install and Configure the PostgreSQL Database](../README.md#postgresql)

_Note_: For the purpose of this quickstart, replace the word QUICKSTART_DATABASENAME with `jts-quickstart-database` in the PostgreSQL instructions.

Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.

Wait until later in these instructions to add the PostgreSQL module and driver configuration to the first JBoss server.


Configure the JBoss Servers
---------------------------

For this example, you will need two instances of the application server, with a subtle startup configuration difference. Application server 2 must be started up with a port offset parameter provided to the startup script as "-Djboss.socket.binding.port-offset=100". 

Since both application servers must be configured in the same way, you must configure the first server and then clone it. After you clone the second server, the first server must be configured for PostgreSQL. 

### Modify the Server Configuration file. 

You can configure the server by running the  `configure-jts-transactions.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Modify the Server Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-jts-transactions.cli
This script configures the server to use jts transaction processing. You should see the following result when you run the script:

        #1 /subsystem=jacorb:write-attribute(name=transactions,value=on)
        #2 /subsystem=transactions:write-attribute(name=jts,value=true)
        #3 /subsystem=transactions:write-attribute(name=node-identifier,value=UNIQUE_IDENTIFER)
        The batch executed successfully.
        {"outcome" => "success"}

#### Modify the Server Configuration Using the JBoss CLI Tool Interactively

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following (replace the words UNIQUE_IDENTIFER with values unique to both servers):

        [standalone@localhost:9999 /] /subsystem=jacorb/:write-attribute(name=transactions,value=on)
        [standalone@localhost:9999 /] /subsystem=transactions/:write-attribute(name=jts,value=true)
        [standalone@localhost:9999 /] /subsystem=transactions/:write-attribute(name=node-identifier,value=UNIQUE_IDENTIFER)
4. _NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the JBoss Server](#remove-jts-configuration).

#### Modify the Server Configuration Manually

1. Make a backup copy of the `JBOSS_HOME/standalone/configuration/standalone-full.xml` file.
2. Open the file JBOSS_HOME/standalone/configuration/standalone-full.xml
3. Enable JTS as follows:
    * Find the orb subsystem and change the configuration to:  

            <subsystem xmlns="urn:jboss:domain:jacorb:1.2">
                <orb>
                    <initializers security="on" transactions="on"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and set a unique node-identifier, (replace the words UNIQUE_IDENTIFER with values unique to both servers) and append the `<jts/>` element:  

            <subsystem xmlns="urn:jboss:domain:transactions:1.2">
                <core-environment node-identifier="UNIQUE_IDENTIFIER">
                <!-- LEAVE EXISTING CONFIG AND APPEND THE FOLLOWING -->
                <jts/>
            </subsystem>
4.  _NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the JBoss Server](#remove-jts-configuration).
  
### Clone the JBOSS_HOME Directory     

Make a copy of this JBoss directory structure to use for the second server.

### Configure Server1 to use PostgreSQL

2. Application server 1 must be configured to use PostgreSQL as per the instructions in [Install and Configure the PostgreSQL Database] (../README.md#postgresql).
    * Be sure to start the PostgreSQL database.
    * [Add the PostgreSQL Module](../README.md#addpostgresqlmodule) to the Application 1 server `modules/` directory.
    * [Add the PostgreSQL driver](../README.md#addpostgresqldriver) to the Application 1 server configuration file.


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
Remove the JTS Configuration from the JBoss Server
---------------------------

You must remove the JTS server configuration you did during setup because it interferes with the JTA quickstarts. 

You can modify the server configuration by running the `remove-jts-transactions.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

### Remove the JTS Server Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-jts-transactions.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 /subsystem=jacorb:write-attribute(name=transactions,value=spec)
        #2 /subsystem=transactions:undefine-attribute(name=jts)
        #3 /subsystem=transactions:undefine-attribute(name=node-identifier)
        The batch executed successfully.
        {"outcome" => "success"}


### Remove the JTS Server Configuration using the JBoss CLI Tool

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following. 

        If you are using Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        If you are using Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        [standalone@localhost:9999 /] /subsystem=jacorb/:write-attribute(name=transactions,value=spec)
        [standalone@localhost:9999 /] /subsystem=transactions/:undefine-attribute(name=jts)
        [standalone@localhost:9999 /] /subsystem=transactions/:undefine-attribute(name=node-identifier)

### Remove the JTS Server Configuration Manually

1. Stop the server.
2. If you backed up the JBOSS_HOME/standalone/configuration/standalone-full.xml,simply replace the edited configuration file with the backup copy.
3. If you did not make a backup copy, open the file JBOSS_HOME/standalone/configuration/standalone-full.xml and disable JTS as follows:

    * Find the orb subsystem and change the configuration back to:  

            <subsystem xmlns="urn:jboss:domain:jacorb:1.2">
                <orb>
                    <initializers security="on" transactions="spec"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and remove the `<jts/>` element:  

            <subsystem xmlns="urn:jboss:domain:transactions:1.2">
                <!-- REMOVE node-identifier ATTRIBUTE FROM core-environment ELEMENT -->
                <!-- LEAVE EXISTING CONFIG AND REMOVE THE </jts> -->
            </subsystem>

