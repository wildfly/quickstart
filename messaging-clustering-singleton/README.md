# messaging-clustering: Messaging Example that Demonstrates Clustering

Author: Flavia Rainone, Jess Sightler  
Level: Advanced  
Technologies: JMS, MDB, Clustering  
Summary: The `messaging-clustering-singleton` quickstart uses a JMS topic and a queue to demonstrate clustering using ActiveMQMessaging with MDB singleton configuration (only one node in the cluster will be active)
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `messaging-clustering` quickstart demonstrates the use of clustering with Apache ActiveMQ and ${product.name.full}. It uses the same code as in [helloworld-mdb](../helloworld-mdb/README.md) quickstart only with a difference in the configuration. Instructions are provided to run the quickstart on either a standalone server or in a managed domain.

These are the two JMS resources contained in this quickstart:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/topic/HELLOWORLDMDBTopic`

Both contain a singleton configuration as specified in the file [WEB-INF/jboss-ejb3.xml](.src/main/webapp/WEB-INF/jboss-ejb3.xml):

        <c:clustering>
            <ejb-name>*</ejb-name>
            <c:clustered-singleton>true</c:clustered-singleton>
        </c:clustering>

The wildcard in `<ejb-name>` indicates that all MDBs contained in the application will be clustered-singleton. As a result, only one node in the cluster will have those MDBs active at a specific time. If that node shuts down, another node in the cluster will become the active node with MDBs, called the singleton provider.

Also, we can find a configuration for delivery-group in the same file:

        <d:delivery>
                <ejb-name>HelloWorldTopicMDB</ejb-name>
                <d:group>abc</d:group>
        </d:delivery>

Here, we can see that only one of the MDBs, `HelloWorldTopicMDB` is associated with a delivery group. All delivery groups used by an MDB must be declared in the ejb subsystem configuration, and they can be enabled or disabled. If the delivery group is disabled in a cluster node, all MDBs belonging to that group will be inactive in that node. Notice that delivery-groups can be used in non-clustered environments as well, in that case, the MDB will be active in the server whenever the delivery-group is enabled in the server. A delivery-group can be enabled using cli as we will see in this quickstart.

If a delivery-group is used in conjunction with singleton, as is the case of this quickstart, the MDB will be active in the singleton provider node only if that node has delivery-group enabled. If not, the MDB will be inactive in that node and all remainder nodes of the cluster.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Build the project

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type this command to build the archive:

        mvn clean install


## Configure the Server and Deploy the Quickstart

You can choose to deploy and run this quickstart in a managed domain or on a standalone server. The sections below describe how to configure and start the server for both modes.

_NOTE - Before you begin:_

1. If it is running, stop the ${product.name} server.

2. If you plan to test using a standalone server, backup the file:

        ${jboss.home.name}/standalone/configuration/standalone-full-ha.xml


3. If you plan to test using a managed domain, backup the following files:

        ${jboss.home.name}/domain/configuration/domain.xml
        ${jboss.home.name}/domain/configuration/host.xml

After you have completed testing this quickstart, you can replace these files to restore the server to its original configuration.


### Configure the Server and Deploy the Quickstart to a Managed Domain

You configure the server by running the install-domain.cli script provided in the root directory of this quickstart.

#### Start the server in domain mode.
1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server in domain mode:

        For Linux:   ${jboss.home.name}/bin/domain.sh
        For Windows: ${jboss.home.name}\bin\domain.bat


#### Configure the Domain Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-domain.cli` file in the root of this quickstart directory. This script creates the server group and servers and
configures messaging clustering for testing this quickstart. You will note it does the following:
    * Stops the servers
    * Creates a server-group to test ActiveMQ Clustering
    * Adds 2 servers to the server-group
    * Configures ActiveMQ clustering in the full-ha profile
    * Creates a delivery group named `abc`, with initial active value set to `true`
    * Deploys the `${project.artifactId}.war` archive
    * Restarts the servers.


2. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to run the script:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=install-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=install-domain.cli

   You should see "outcome" => "success" for all of the commands.

3. Restart the server in domain mode as described above.

### Configure the Server and Deploy the Quickstart to a Standalone Server

If you choose to use standalone servers rather than domain mode, you will need two instances of the application server. Application
server 2 must be started with a port offset parameter provided to the startup script as `-Djboss.socket.binding.port-offset=100`.

Since both application servers must be configured in the same way, you must configure the first server and then clone it.

#### Start the Server in Standalone Mode using the Full HA Profile.

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the full-ha profile. This profile supports clustering/HA

        For Linux:   ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: ${jboss.home.name}_1\bin\standalone.bat -c standalone-full-ha.xml


#### Configure the Standalone Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-standalone.cli` file in the root of this quickstart directory. This script configures clustering for a standalone server. You will note it does the following:
    * Enables clustering and sets a cluster password
    * Creates a delivery group named `abc`, with initial active value set to `true`
    * Deploys the `${project.artifactId}.war` archive
    * Reloads the server configuration

2. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to run the script:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=install-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=install-standalone.cli

   You should see "outcome" => "success" for all of the commands.

#### Clone the ${product.name} Directory

After you have successfully configured the server, you must make a copy of this ${product.name} directory structure to use for the second server.

1. Stop the server.
2. Make a copy of this ${product.name} directory structure to use for the second server.
3. Remove the following directories from the cloned instance:

        ${jboss.home.name}_2/standalone/data/activemq/bindings
        ${jboss.home.name}_2/standalone/data/activemq/journal
        ${jboss.home.name}_2/standalone/data/activemq/largemessages

#### Start the ${product.name} Standalone Servers with the Full HA Profile

When you start the servers, you must pass the cluster password on the command line to avoid the warning "AMQ222186: unable to authorise cluster control".

If you are using Linux:

        Server 1: ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password
        Server 2: ${jboss.home.name}_2/bin/standalone.sh -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password -Djboss.socket.binding.port-offset=100

If you are using Windows:

        Server 1: ${jboss.home.name}_1\bin\standalone.bat -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password
        Server 2: ${jboss.home.name}_2\bin\standalone.bat -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password -Djboss.socket.binding.port-offset=100


## Access the Application

### Access the Application Running in Domain Dode

The application will be running at the following URL: <http://localhost:9080/${project.artifactId}/HelloWorldMDBServletClient>.

It will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:9080/${project.artifactId}/HelloWorldMDBServletClient?topic>

### Access the Application Running in Standalone Mode

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/HelloWorldMDBServletClient>.

It will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/${project.artifactId}/HelloWorldMDBServletClient?topic>

## Investigate the Server Console Output

Look at both the ${product.name} server consoles or logs, looking for log messages like the following:

        00:53:56,989 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-1 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 1
        00:53:59,691 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-5 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 1
        00:53:59,697 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-2 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 2
        00:53:59,716 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-4 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 3
        00:53:59,722 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-3 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 4
        00:53:59,743 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-1 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 5
        00:03:18,904 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-9 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 4
        00:03:18,904 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-8 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 3
        00:03:18,969 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-7 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 2
        00:03:18,972 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-6 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 1
        00:03:18,985 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-8 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 5


You will notice that only one of the nodes, elected as the singleton provider node, will be receiving the messages. For that, check both servers, only one will contain the received message log entries.

_Note:_ You will see the following warnings in the server logs. You can ignore these warnings as they are intended for production servers.

        WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the send buffer of socket DatagramSocket was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
        WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the receive buffer of socket DatagramSocket was set to 20MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
        WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the send buffer of socket MulticastSocket was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
        WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the receive buffer of socket MulticastSocket was set to 25MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)

_NOTE:_ After the server has been running for a period of time, you may see the following warnings in the server log, which are followed by a stacktrace. You can ignore these warnings as this is is a known issue and is harmless. See [JBEAP-794](https://issues.jboss.org/browse/JBEAP-794) for more information.

        WARN  [org.infinispan.topology.ClusterTopologyManagerImpl] (transport-thread--p15-t6) ISPN000197: Error updating cluster member list: org.infinispan.util.concurrent.TimeoutException: Replication timeout for <application-name>


## Electing a New Singleton Provider Server

If you reboot the singleton server node, the other node will be elected the new singleton provider, and will start receiving the MDB messages instead.

You should see the following output in the new singleton provider server:

        WFLYCLSV0003: master:quickstart-messagingcluster-nodeX elected as the singleton provider of the org.wildfly.ejb3.clustered.singleton service

Where `nodeX` will be either `node1` or `node2`, according to whether the new singleton provider is node1 or node2.

If you now try to access the servlet urls, you will see that the new provider is receiving all new messages.


### Rebooting the Singleton Provider Server Node in Domain Mode
Run the following command, replacing `nodeX` in the script name by `node1` or `node2`, according to whether the current singleton provider is node1 or node2.

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restart-nodeX-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restart-nodeX-domain.cli

### Rebooting the Singleton Provider Server Node in Standalone Mode
Stop the provider server and restart it again, using the same command you used to start the server initially.


## Disable and Enable Delivery-Group.

For disabling the delivery-group `"abc"`, to which the topic belongs, you should run the disable-delivery-group script following the instructions in the next sections,
according to the mode you are using to run the server.

After disabling the delivery-group, try sending messages to the topic, you will notice that the topic messages are not delivered when the delivery-group is inactive.

Next, enable the delivery-group, using the appropriate enable-delivery-group cli script, so that the topic messages can be delivered again.

### Disable and Enable Delivery-Group in Domain Mode

To disable the delivery-group named "abc", to which the topic belongs, run disable-delivery-group-domain.cli:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=disable-delivery-group-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=disable-delivery-group-domain.cli



Similarly, to enable the delivery-group, run the enable-delivery-group-domain.cli script:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=enable-delivery-group-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=enable-delivery-group-domain.cli


### Disable and Enable Delivery-Group in Standalone Mode

To disable the delivery-group named "abc", to which the topic belongs, run disable-delivery-group-standalone.cli in both servers:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=disable-delivery-group-standalone.cli
                   ${jboss.home.name}/bin/jboss-cli.sh --connect controller=localhost:10090 --file=disable-delivery-group-standalone.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=disable-delivery-group-standalone.cli
                     ${jboss.home.name}\bin\jboss-cli.bat --connect controller=localhost:10090 --file=disable-delivery-group-standalone.cli

Similarly, to enable the delivery-group, run the enable-delivery-group-standalone.cli script in both servers:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=enable-delivery-group-standalone.cli
                   ${jboss.home.name}/bin/jboss-cli.sh --connect controller=localhost:10090 --file=enable-delivery-group-standalone.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=enable-delivery-group-standalone.cli
                     ${jboss.home.name}\bin\jboss-cli.bat --connect controller=localhost:10090 --file=enable-delivery-group-standalone.cli


## Undeploy the Archive

When you are finished testing, use the following instructions to undeploy the quickstart.

### Undeploy the quickstart in Domain Mode

1. Make sure you have started the ${product.name} server in domain mode as described above.
2. Open a command prompt, navigate to the root directory of this quickstart, and run the following command to undeploy the ${project.artifactId} quickstart:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=undeploy-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=undeploy-domain.cli


### Undeploy the quickstart in Standalone Mode

1. Make sure you have started the ${product.name} server in standalone mode as described above.
2. Open a command prompt, navigate to the root directory of this quickstart, and run the following commands to undeploy the ${project.artifactId} quickstart:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=undeploy-standalone.cli
                   ${jboss.home.name}_1/bin/jboss-cli.sh --connect controller=localhost:10090 --file=undeploy-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=undeploy-standalone.cli
                     ${jboss.home.name}_1\bin\jboss-cli.bat --connect controller=localhost:10090 --file=undeploy-standalone.cli


## Remove the Server Configuration

### Remove the Domain Server Configuration

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script.

#### Remove the Domain Server Configuration Manually

_Note: This method ensures the server is restored to its prior configuration._

1. If it is running, stop the ${product.name} server.
2. Restore the `${jboss.home.name}/domain/configuration/domain.xml` and `${jboss.home.name}/domain/configuration/host.xml` files with the back-up copies
of the files. Be sure to replace ${jboss.home.name} with the path to your server.

#### Remove the Domain Server Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the ${product.name} server by typing the following:

        For Linux:   ${jboss.home.name}/bin/domain.sh
        For Windows: ${jboss.home.name}\bin\domain.bat

2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server.

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=remove-domain.cli
   This script removes the server configuration that was done by the `install-domain.cli` script. You should see the following result following the script commands:

        The batch executed successfully

   _Note: If the `:stop-server` command does not complete before the next commands are issued, you may see an error similar to the following:

         {"JBAS014653: Composite operation failed and was rolled back. Steps that failed:" => {"Operation step-1" => "JBAS010977: Server (quickstart-messagingcluster-node1) still running"}}
   Simply wait a few seconds and run the command a second time._

### Remove the Standalone Server Configuration

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the JBoss CLI Script.

#### Remove the Standalone Server Configuration Manually

_Note: This method ensures the server is restored to its prior configuration._

1. If they are running, stop both ${product.name} servers.
2. Restore the `${jboss.home.name}_1/standalone/configuration/standalone-full-ha.xml` file with the back-up copies of the file. Be sure to replace ${jboss.home.name}_1 with the path to your server.

#### Remove the Standalone Configuration by Running the JBoss CLI Script

_Note: This script returns the server to a default configuration and the result may not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the ${product.name} server by typing the following:

        For Linux:   ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password
        For Windows: ${jboss.home.name}_1\bin\domain.bat -c standalone-full-ha.xml -Djboss.messaging.cluster.password=password
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name}_1 with the path to your server.

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=remove-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=remove-standalone.cli
This script removes the server configuration that was done by the `install-standalone.cli` script. You should see the following result following the script commands:

        The batch executed successfully

### Delete the Cloned Standalone ${product.name} Directory

1. If it is running, stop the second instance of the ${product.name} server.
2. Delete the cloned directory.
