# jts: Java Transaction Service - Distributed EJB Transactions

Author: Tom Jenkinson  
Level: Intermediate  
Technologies: JTS, EJB, JMS  
Summary: The `jts` quickstart shows how to use JTS to perform distributed transactions across multiple containers, fulfilling the properties of an ACID transaction.  
Prerequisites: cmt  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The `jts` quickstart demonstrates how to perform distributed transactions across multiple containers in an application deployed to ${product.name.full}. A distributed transaction is a set of operations performed by two or more nodes, participating in an activity coordinated as a single entity of work, and fulfilling the properties of an ACID transaction.

ACID is a set of 4 properties that guarantee the resources are processed in the following manner:

* Atomic - if any part of the transaction fails, all resources remain unchanged.
* Consistent - the state will be consistent across resources after a commit
* Isolated - the execution of the transaction for each resource is isolated from each others
* Durable - the data will persist after the transaction is committed


The example uses Java Transaction Service (JTS) to propagate a transaction context across two Container-Managed Transaction (CMT) EJBs that, although deployed in separate servers, participate in the same transaction. In this example, one server processes the Customer and Account data and the other server processes the Invoice data.

The code base is essentially the same as the [cmt](../cmt/README.md) quickstart, however in this case the `InvoiceManager` has been separated to a different deployment archive to demonstrate the usage of JTS. You can see the changes in the
following ways:

1. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/InvoiceManagerEJB.java` has been moved to `application-component-2/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/InvoiceManagerEJB`
2. `cmt/src/main/java/org/jboss/as/quickstarts/cmt/ejb/CustomerManagerEJB.java` has been moved to `jts/application-component-1/src/main/java/org/jboss/as/quickstarts/cmt/jts/ejb/CustomerManagerEJB.java`

The changes to `CustomerManagerEJB` are purely to accommodate the fact that `InvoiceManager` is now distributed.

You will see that the `CustomerManagerEJB` uses the EJB home for the remote EJB, this is expected to connect to remote EJBs. The example expects the EJBs to be deployed onto the same physical machine. This is not a restriction of JTS and the example can easily be converted to run on separate machines by editing the hostname value for the `InvoiceManagerEJB` in `org.jboss.as.quickstarts.cmt.jts.ejb.CustomerManagerEJB`.

A simple MDB has been provided that prints out the messages sent but this is not a transactional MDB and is purely provided for debugging purposes.

Also, while the `cmt` quickstart uses the Java EE container default datasource, which is not distributed, this quickstart instead uses an external PostgreSQL database.

After  you complete this quickstart, you are invited to run through the [jts-distributed-crash-rec](../jts-distributed-crash-rec/README.md) quickstart. The crash recovery quickstart builds upon this quickstart by demonstrating the fault tolerance of ${product.name.full}.

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in ${product.name} and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Configuration Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for ${product.name.full}._


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Prerequisites

Developers should be familiar with the concepts introduced in the `cmt` quickstart.

This quickstart requires the configuration of two servers. The first server must be configured to use the PostgreSQL database. Instructions to install and configure PostgreSQL are below.


## Configure the PostgreSQL Database for Use with this Quickstart

This quickstart requires the PostgreSQL database.

Instructions to install and configure PostgreSQL can be found here: [Download and Install PostgreSQL](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#download-and-install-postgresql)

_Note_: For the purpose of this quickstart, replace the word `QUICKSTART_DATABASE_NAME` with `jts-quickstart-database` in the PostgreSQL instructions.

Be sure to [Create a Database User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#create-a-database-user) for the PostgeSQL database.

When you have completed these steps, be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions to start the database server for your operating system every time you reboot your machine.

Wait until later in these instructions to add the PostgreSQL module and driver configuration to the first ${product.name} server.


## Configure the Servers

For this example, you will need two instances of the application server, with a subtle startup configuration difference. Application server 2 must be started up with a port offset parameter provided to the startup script as `-Djboss.socket.binding.port-offset=100`.

Since both application servers must be configured in the same way, you must configure the first server and then clone it. After you clone the second server, the first server must be configured for PostgreSQL.

_Note:_ This quickstart README file use the following replaceable values. When you encounter these values in a README file, be sure to replace them with the actual path to the correct ${product.name} server.

  * `${jboss.home.name}` denotes the path to the original ${product.name} installation.
  * `${jboss.home.name}_1` denotes the path to the modified ${product.name} server 1 configuration.
  * `${jboss.home.name}_2` denotes the path to the modified ${product.name} server 2 configuration.

### Configure the First Server

You configure JTS transactions by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-jts-transactions.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the ${product.name} server with the full profile, passing a unique node ID by typing the following command. Be sure to replace `UNIQUE_NODE_ID_1` with a node identifier that is unique to both servers.

        For Linux:  ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        For Windows:  ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml  -Djboss.tx.node.id=UNIQUE_NODE_ID_1
3. Review the `configure-jts-transactions.cli` file in the root of this quickstart directory. This script configures the server to use jts transaction processing.
4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-jts-transactions.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-jts-transactions.cli
 You should see the following result when you run the script:

        process-state: restart-required
        {
        "outcome" => "success",
        "result" => undefined
        }
5. Stop the ${product.name} server.

_NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the ${product.name} Server](#remove-the-jts-configuration-from-the-server).


### Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone-full.xml` file and review the changes.

1. The orb initializers `transactions` attribute is changed from `spec` to `full` in the  `iiop-openjdk` subsystem to enable JTS.

        <subsystem xmlns="urn:jboss:domain:iiop-openjdk:1.0">
            <initializers transactions="full" security="identity"/>
        </subsystem>

2. An empty `<jts/>` element is added to the end of the `transactions` subsystem to enable JTS.

        <subsystem xmlns="urn:jboss:domain:transactions:3.0">
            <core-environment node-identifier="${jboss.tx.node.id}">
                <process-id>
                    <uuid/>
                </process-id>
            </core-environment>
            <recovery-environment socket-binding="txn-recovery-environment" status-socket-binding="txn-status-manager"/>
            <jts/>
        </subsystem>

_NOTE:_ When you have completed testing this quickstart, it is important to [Remove the JTS Configuration from the ${product.name} Server](#remove-the-jts-configuration-from-the-server).

### Clone the Server Directory     

Make a copy of this ${product.name} directory structure to use for the second server.

### Configure Server1 to use PostgreSQL

Application server 1 must be now configured to use the PostgreSQL database created previously in the [Configure the PostgreSQL Database for Use with this Quickstart](#configure-the-postgresql-database-for-use-with-this-quickstart) section.

1. Be sure to start the PostgreSQL database. Unless you have set up the database to automatically start as a service, you must repeat the instructions "Start the database server" for your operating system every time you reboot your machine.
2. Follow the instructions to [Add the PostgreSQL Module to the ${product.name} Server](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#add-the-postgresql-module-to-the-red-hat-jboss-enterprise-application-platform-server) to the server 1 install only.
3. Follow the instructions to [Configure the PostgreSQL Driver in the ${product.name} Server](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_POSTGRESQL_EAP7.md#configure-the-postgresql-driver-in-the-red-hat-jboss-enterprise-application-platform-server) for the server 1 configuration. Be sure to pass the `-Djboss.tx.node.id=UNIQUE_NODE_ID_1` on the command line when you start the first server to configure PostgreSQL.


## Start the Servers

Start the two ${product.name} servers with the full profile, passing a unique node ID by typing the following command. You must pass a socket binding port offset on the command to start the second server. Be sure to replace `UNIQUE_NODE_ID` with a node identifier that is unique to both servers.

If you are using Linux:

        Server 1: ${jboss.home.name}_1/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        Server 2: ${jboss.home.name}_2/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_2 -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: ${jboss.home.name}_1\bin\standalone.bat -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        Server 2: ${jboss.home.name}_2\bin\standalone.bat -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_2 -Djboss.socket.binding.port-offset=100


## Build and Deploy the Quickstart

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.


1. Make sure you have started the ${product.name} server with the PostgreSQL driver
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `application-component-1/target/${project.artifactId}-1.war` and `application-component-2/target/${project.artifactId}-2.jar` to the running instance of the server.

## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}-1/>.

When you enter a name and click to `Add` that customer, you will see the following in the application server 1 console:

    INFO  [org.hibernate.hql.internal.QueryTranslatorFactoryInitiator] (default task-2) HHH000397: Using ASTQueryTranslatorFactory
    INFO  [org.jboss.ejb.client] (default task-4) JBoss EJB Client version 2.1.4.Final-redhat-1

You will also see the following in application-server-2 console:

    INFO  [org.jboss.ejb.client] (p: default-threadpool; w: Idle) JBoss EJB Client version 2.1.4.Final-redhat-1
    INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-97 (ActiveMQ-client-global-threads-6840624)) Received Message: Created invoice for customer named: Tom

The web page will also change and show you the new list of customers.


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    WFLYJCA0091: -ds.xml file deployments are deprecated. Support may be removed in a future version.

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package wildfly:undeploy


## Remove the JTS Configuration From the Server

You must remove the JTS server configuration you did during setup because it interferes with the JTA quickstarts.

You can modify the server configuration by running the `remove-jts-transactions.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

### Remove the JTS Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server with the full profile.

        For Linux:  ${jboss.home.name}_1/bin/standalone.sh -c standalone-full.xml  -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        For Windows:  ${jboss.home.name}_1\bin\standalone.bat -c standalone-full.xml  -Djboss.tx.node.id=UNIQUE_NODE_ID_1
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=remove-jts-transactions.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=remove-jts-transactions.cli
This script removes the JTS configuration from the `iiop-openjdk` and `transactions` subsystems in the server configuration. You should see the following result when you run the script:

        The batch executed successfully
        process-state: restart-required
        {
            "outcome" => "success",
            "result" => undefined
        }

### Remove the JTS Server Configuration using the JBoss CLI Tool

1. Start the ${product.name} server with the full profile.

        For Linux:  ${jboss.home.name}_1/bin/standalone.sh -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
        For Windows:  ${jboss.home.name}_1\bin\standalone.bat -c standalone-full.xml -Djboss.tx.node.id=UNIQUE_NODE_ID_1
2. To start the JBoss CLI tool, open a new command prompt, navigate to the ${jboss.home.name} directory, and type the following:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        /subsystem=iiop-openjdk/:write-attribute(name=transactions,value=spec)
        /subsystem=transactions/:undefine-attribute(name=jts)
        /subsystem=transactions/:undefine-attribute(name=node-identifier)
 You should see the following result when you run the script:

        {
            "outcome" => "success",
            "response-headers" => {
                "operation-requires-reload" => true,
                "process-state" => "restart-required"
            }
        }

        {

            "outcome" => "success",
            "response-headers" => {
                "operation-requires-restart" => true,
                "process-state" => "restart-required"
            }
        }

        {
            "outcome" => "success",
            "response-headers" => {
                "operation-requires-reload" => true,
                "process-state" => "restart-required"
            }
        }


### Remove the JTS Server Configuration Manually

1. Stop the server.
2. If you backed up the ${jboss.home.name}/standalone/configuration/standalone-full.xml,simply replace the edited configuration file with the backup copy.
3. If you did not make a backup copy, open the file ${jboss.home.name}/standalone/configuration/standalone-full.xml and disable JTS as follows:

    * Find the `orb` subsystem and change the configuration back to its original state.

            <subsystem xmlns="urn:jboss:domain:iiop-openjdk:1.0">
                <initializers transactions="spec" security="identity"/>
            </subsystem>

    * Find the `transaction` subsystem and remove the `node-identifier` attribute from the `core-environment` element. Also remove the `<jts/>` element.

            <subsystem xmlns="urn:jboss:domain:transactions:3.0">
                <core-environment>
                    <process-id>
                        <uuid/>
                    </process-id>
                </core-environment>
                <recovery-environment socket-binding="txn-recovery-environment" status-socket-binding="txn-status-manager"/>
            </subsystem>
