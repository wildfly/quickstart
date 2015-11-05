hornetq-clustering: HornetQ Demonstrating using Clustering
============================================================
Author: Jess Sightler  
Level: Intermediate  
Technologies: JMS, MDB, HornetQ  
Summary: The `hornetq-clustering` quickstart does not contain any code and instead uses the `helloworld-mdb` quickstart to demonstrate clustering using HornetQ.  
Prerequisites: helloworld-mdb  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `hornetq-clustering` quickstart demonstrates the use of clustering with HornetQ and Red Hat JBoss Enterprise Application Platform. It uses the [helloworld-mdb](../helloworld-mdb/README.md) quickstart for its tests, so there is no code associated with this quickstart. Instructions are provided to run the quickstart on either a standalone server or in a managed domain.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Prerequisites
---------------

**IMPORTANT:** This quickstart depends on the deployment of the `helloworld-mdb` quickstart WAR for its tests. Before you continue, you must build the `helloworld-mdb` quickstart WAR.

Open a command prompt and navigate to the root directory of the helloworld-mdb quickstart.
Type this command to build the WAR archive:

        mvn clean install

See the helloworld-mdb [README](../helloworld-mdb/README.md) for further information about this quickstart.


Configure the Server and Deploy the Quickstart
---------------

You can choose to deploy and run this quickstart in a managed domain or on a standalone server. The sections below describe how to configure and start the server for both modes. 

_NOTE - Before you begin:_

1. If it is running, stop the WildFly server.

2. If you plan to test using a standalone server, backup the file:

        WILDFLY_HOME/standalone/configuration/standalone-full-ha.xml


3. If you plan to test using a managed domain, backup the following files:

        WILDFLY_HOME/domain/configuration/domain.xml
        WILDFLY_HOME/domain/configuration/host.xml

After you have completed testing this quickstart, you can replace these files to restore the server to its original configuration.


### Configure the Server and Deploy the Quickstart to a Managed Domain

You configure the server by running the install-domain.cli script provided in the root directory of this quickstart.

#### Start the server in domain mode.
1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server in domain mode:

        For Linux:   WILDFLY_HOME/bin/domain.sh
        For Windows: WILDFLY_HOME\bin\domain.bat


#### Configure the Domain Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-domain.cli` file in the root of this quickstart directory. This script creates the server group and servers and configures HornetQ Clustering for testing this quickstart. You will note it does the following:
    * Stops the servers
    * Creates a server-group to test HornetQ Clustering
    * Adds 2 servers to the server-group
    * Configures HornetQ clustering in the full-ha profile
    * Deploys the `helloworld-mdb.war` archive
    * Restarts the servers.
    
    _NOTE: If your `helloworld-mdb` quickstart is not located at the same level in the file structure as this quickstart, you
    must modify its path in this script. Find the 'NOTE:' in the file for instructions._

2. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to run the script:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=install-domain.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=install-domain.cli
        
   You should see "outcome" => "success" for all of the commands.
3. Restart the server in domain mode as described above.


### Configure the Server and Deploy the Quickstart to a Standalone Server

If you choose to use standalone servers rather than domain mode, you will need two instances of the application server. Application
server 2 must be started with a port offset parameter provided to the startup script as `-Djboss.socket.binding.port-offset=100`. 

Since both application servers must be configured in the same way, you must configure the first server and then clone it.

#### Start the Server in Standalone Mode using the Full HA Profile.

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server with the full-ha profile. This profile supports clustering/HA

        For Linux:   WILDFLY_HOME_1/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: WILDFLY_HOME_1\bin\standalone.bat -c standalone-full-ha.xml


#### Configure the Standalone Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-standalone.cli` file in the root of this quickstart directory. This script configures clustering for a standalone server. You will note it does the following:
    * Enables console logging. By default, the full HA profile does not log to the console, so this script enables it.
    * Enables clustering and sets a cluster password
    * Enables clustering in the RemoteConnectionFactory
    * Deploys the `helloworld-mdb.war` archive
    * Reloads the server configuration
    
    _NOTE: If your `helloworld-mdb` quickstart is not located at the same level in the file structure as this quickstart, you
    must modify its path in this script. Find the 'NOTE:' in the file for instructions._
2. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to run the script:

        For Linux: WILDFLY_HOME_1/bin/jboss-cli.sh --connect --file=install-standalone.cli
        For Windows: WILDFLY_HOME_1\bin\jboss-cli.bat --connect --file=install-standalone.cli
        
   You should see "outcome" => "success" for all of the commands.

#### Clone the WildFly Directory     

After you have successfully configured the server, you must make a copy of this WildFly directory structure to use for the second server.

1. Stop the server.
2. Make a copy of this WildFly directory structure to use for the second server.
3. Remove the following directories from the cloned instance:

        WILDFLY_HOME_2/standalone/data/messagingbindings
        WILDFLY_HOME_2/standalone/data/messagingjournal
        WILDFLY_HOME_2/standalone/data/messaginglargemessages

#### Start the WildFly Standalone Servers with the Full HA Profile

If you are using Linux:

        Server 1: WILDFLY_HOME_1/bin/standalone.sh -c standalone-full-ha.xml
        Server 2: WILDFLY_HOME_2/bin/standalone.sh -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=100

If you are using Windows:

        Server 1: WILDFLY_HOME_1\bin\standalone.bat -c standalone-full-ha.xml
        Server 2: WILDFLY_HOME_2\bin\standalone.bat -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=100


Access the application 
---------------------

### Access the Application Running in Domain Dode

The application will be running at the following URL: <http://localhost:9080/jboss-helloworld-mdb/HelloWorldMDBServletClient>. 

It will send some messages to the queue. 

To send messages to the topic, use the following URL: <http://localhost:9080/jboss-helloworld-mdb/HelloWorldMDBServletClient?topic>

### Access the Application Running in Standalone Mode

The application will be running at the following URL: <http://localhost:8080/wildfly-helloworld-mdb/HelloWorldMDBServletClient>. 

It will send some messages to the queue. 

To send messages to the topic, use the following URL: <http://localhost:8080/wildfly-helloworld-mdb/HelloWorldMDBServletClient?topic>


Investigate the Server Console Output
-------------------------

Look at the WildFly server console or log and you should see log messages like the following:

        [Server:jdf-hornetqcluster-node1] 16:34:41,165 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 1
        [Server:jdf-hornetqcluster-node1] 16:34:41,274 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 3
        [Server:jdf-hornetqcluster-node1] 16:34:41,323 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-6 (HornetQ-client-global-threads-1067469862)) Received Message from queue: This is message 5
        [Server:jdf-hornetqcluster-node2] 16:34:41,324 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-8 (HornetQ-client-global-threads-1771031398)) Received Message from queue: This is message 2
        [Server:jdf-hornetqcluster-node2] 16:34:41,330 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-7 (HornetQ-client-global-threads-1771031398)) Received Message from queue: This is message 4

Note that the logging indicates messages have arrived from both node 1 (jdf-hornetqcluster-node1) as well as node 2 (jdf-hornetqcluster-node2).

Undeploy the Archive
--------------------

When you are finished testing, use the following instructions to undeploy the quickstart.

### Undeploy the quickstart in Domain Mode

1. Make sure you have started the WildFly server in domain mode as described above.
3. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to undeploy the helloworld-mdb quickstart:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=undeploy-domain.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=undeploy-domain.cli

        
### Undeploy the quickstart in Standalone Mode

1. Make sure you have started the WildFly server in standalone mode as described above.
3. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to undeploy the helloworld-mdb quickstart:

        For Linux: WILDFLY_HOME_1/bin/jboss-cli.sh --connect --file=undeploy-standalone.cli
        For Windows: WILDFLY_HOME_1\bin\jboss-cli.bat --connect --file=undeploy-standalone.cli



Remove the Server Configuration
--------------------

### Remove the Domain Server Configuration

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script. 

#### Remove the Domain Server Configuration Manually           

_Note: This method ensures the server is restored to its prior configuration._

1. If it is running, stop the WildFly server.
2. Restore the `WILDFLY_HOME/domain/configuration/domain.xml` and `WILDFLY_HOME/domain/configuration/host.xml` files with the back-up copies of the files. Be sure to replace WILDFLY_HOME with the path to your server.

#### Remove the Domain Server Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the WildFly server by typing the following: 

        For Linux:   WILDFLY_HOME/bin/domain.sh
        For Windows: WILDFLY_HOME\bin\domain.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server.

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=remove-domain.cli 
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=remove-domain.cli 
   This script removes the server configuration that was done by the `install-domain.cli` script. You should see the following result following the script commands:

        The batch executed successfully.
        
   _Note: If the `:stop-server` command does not complete before the the next commands are issued, you may see an error similar to the following:
   
         {"JBAS014653: Composite operation failed and was rolled back. Steps that failed:" => {"Operation step-1" => "JBAS010977: Server (jdf-hornetqcluster-node1) still running"}}
   Simply wait a few seconds and run the command a second time.

### Remove the Standalone Server Configuration

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script. 

#### Remove the Standalone Server Configuration Manually           

_Note: This method ensures the server is restored to its prior configuration._

1. If they are running, stop both WildFly servers.
2. Restore the `WILDFLY_HOME_1/standalone/configuration/standalone-full-ha.xml` file with the back-up copies of the file. Be sure to replace WILDFLY_HOME_1 with the path to your server.

#### Remove the Standalone Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the WildFly server by typing the following: 

        For Linux:   WILDFLY_HOME_1/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: WILDFLY_HOME_1\bin\domain.bat -c standalone-full-ha.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME_1 with the path to your server.

        For Linux: WILDFLY_HOME_1/bin/jboss-cli.sh --connect --file=remove-standalone.cli 
        For Windows: WILDFLY_HOME_1\bin\jboss-cli.bat --connect --file=remove-standalone.cli 
This script removes the server configuration that was done by the `install-standalone.cli` script. You should see the following result following the script commands:

        The batch executed successfully.

### Delete the Cloned Standalone WildFly Directory

1. If it is running, stop the second instance of the WildFly server.
2. Delete the cloned directory.



