# messaging-clustering-singleton: Messaging Example that Demonstrates Clustering

Author: Flavia Rainone, Jess Sightler  
Level: Advanced  
Technologies: JMS, MDB, Clustering  
Summary: The `messaging-clustering-singleton` quickstart uses a JMS topic and a queue to demonstrate clustering using ${product.name} messaging with MDB singleton configuration where only one node in the cluster will be active.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `messaging-clustering-singleton` quickstart demonstrates the use of clustering with integrated Apache ActiveMQ Artemis. It uses the same code as in [helloworld-mdb](../helloworld-mdb/README.md) quickstart, with only a difference in the configuration to run it as a clustered singleton. Instructions are provided to run the quickstart on either a standalone server or in a managed domain.

These are the two JMS resources contained in this quickstart:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/topic/HELLOWORLDMDBTopic`

Both contain a singleton configuration as specified in the file [WEB-INF/jboss-ejb3.xml](src/main/webapp/WEB-INF/jboss-ejb3.xml):

    <c:clustering>
        <ejb-name>*</ejb-name>
        <c:clustered-singleton>true</c:clustered-singleton>
    </c:clustering>

The wildcard (*) in the `<ejb-name>` element indicates that all MDBs contained in the application will be clustered singleton. As a result, only one node in the cluster will have those MDBs active at a specific time. If that node shuts down, another node in the cluster will become the active node with MDBs, called the singleton provider.

Also, we can find a configuration for delivery group in the same file:

    <d:delivery>
        <ejb-name>HelloWorldTopicMDB</ejb-name>
        <d:group>my-mdb-delivery-group</d:group>
    </d:delivery>

Here, you can see that only one of the MDBs, `HelloWorldTopicMDB`, is associated with a delivery group. All delivery groups used by an MDB must be declared in the `ejb` subsystem configuration, and they can be enabled or disabled. If the delivery group is disabled in a cluster node, all MDBs belonging to that group will be inactive in that node. Notice that delivery groups can be used in non-clustered environments as well. In that case, the MDB will be active in the server whenever the delivery group is enabled in the server. A delivery group can be enabled using the management CLI as you will see in this quickstart.

If a delivery group is used in conjunction with singleton, as is the case of this quickstart, the MDB will be active in the singleton provider node only if that node has `delivery-group` enabled. If not, the MDB will be inactive in that node and all remainder nodes of the cluster.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Build the Project

Follow these steps to build the project without deploying it.

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type this command to build the archive:

        mvn clean install


## Configure the Server and Deploy the Quickstart

You can choose to configure and deploy this quickstart to a [managed domain](#configure-the-server-and-deploy-the-quickstart-to-a-managed-domain) or to a [standalone server](#configure-the-server-and-deploy-the-quickstart-to-a-standalone-server). The sections below describe how to configure and start the server for each configuration.


### Configure the Server and Deploy the Quickstart to a Managed Domain

You configure the server by running the `install-domain.cli` script provided in the root directory of this quickstart.

#### Back up the Server Configuration Files for a Managed Domain

Before you begin, back up your server configuration files.

1. If it is running, stop the ${product.name} server.

2. Back up the following files:

        ${jboss.home.name}/domain/configuration/domain.xml
        ${jboss.home.name}/domain/configuration/host.xml

After you have completed testing this quickstart, you can replace these files to restore the server to its original configuration.


#### Start the Managed Domain
1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server in a managed domain:

        For Linux:   ${jboss.home.name}/bin/domain.sh
        For Windows: ${jboss.home.name}\bin\domain.bat


#### Configure the Domain Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-domain.cli` file located in the root of this quickstart directory. This script creates the server group and servers and configures messaging clustering for testing this quickstart. You will note it does the following:
    * Stops the servers.
    * Creates the `quickstart-messaging-clustering-singleton-group` server group to test ActiveMQ clustering.
    * Enables console logging to allow you to view the quickstart output.
    * Adds two servers to the `server-group`.
    * Configures ActiveMQ clustering in the `full-ha` profile.
    * Creates a delivery group named `my-mdb-delivery-group`, with initial active value set to `true`.
    * Deploys the `${project.artifactId}.war` archive.
    * Starts the servers that were added to the managed domain.


2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=install-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=install-domain.cli

    You should see the following output:

        {
            "outcome" => "success",
            "result" => undefined,
            "server-groups" => undefined
        }
        The batch executed successfully
        {
            "outcome" => "success",
            "result" => "STARTED"
        }
        {
            "outcome" => "success",
            "result" => "STARTED"
        }


### Configure the Server and Deploy the Quickstart to a Standalone Server

If you choose to use standalone servers rather than a managed domain, you need two instances of the application server. The second server must be started with a port offset parameter provided to the startup script as `-Djboss.socket.binding.port-offset=100`.

Since both application servers must be configured in the same way, you must configure the first server and then clone it.

#### Back up the Server Configuration File for a Standalone Server

Before you begin, back up your server configuration file.

1. If it is running, stop the ${product.name} server.

2. Back up the following file:

        ${jboss.home.name}/standalone/configuration/standalone-full-ha.xml

After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.


#### Start the Standalone Server Using the Full HA Profile.

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the `full-ha` profile. This profile supports HA clustering.

        For Linux:   ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: ${jboss.home.name}_1\bin\standalone.bat -c standalone-full-ha.xml


#### Configure the Standalone Server and Deploy the Quickstart Using the JBoss CLI

1. Review the `install-standalone.cli` file located in the root of this quickstart directory. This script configures clustering for a standalone server. You will note it does the following:
    * Because the console is disabled by default in the Full HA profile, it enables console logging to allow you to view the quickstart output.
    * Enables clustering and sets a cluster password.
    * Creates a delivery group named `my-mdb-delivery-group`, with initial active value set to `true`.
    * Deploys the `${project.artifactId}.war` archive.
    * Reloads the server configuration.

2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=install-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=install-standalone.cli

    You should see the following output:

        The batch executed successfully
        process-state: reload-required

#### Clone the ${product.name} Directory

After you have successfully configured the server, you must make a copy of this ${product.name} directory structure to use for the second server.

1. Stop the server.
2. Make a copy of this ${product.name} directory structure to use for the second server.
3. Remove the following directories from the cloned instance:

        ${jboss.home.name}_2/standalone/data/activemq/bindings
        ${jboss.home.name}_2/standalone/data/activemq/journal
        ${jboss.home.name}_2/standalone/data/activemq/largemessages

#### Start the ${product.name} Standalone Servers with the Full HA Profile

If you are using Linux:

    Server 1: ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml
    Server 2: ${jboss.home.name}_2/bin/standalone.sh -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=100

If you are using Windows:

    Server 1: ${jboss.home.name}_1\bin\standalone.bat -c standalone-full-ha.xml
    Server 2: ${jboss.home.name}_2\bin\standalone.bat -c standalone-full-ha.xml -Djboss.socket.binding.port-offset=100


## Access the Application

### Access the Application Running in a Managed Domain

The application will be running at the following URL: <http://localhost:9080/${project.artifactId}/HelloWorldMDBServletClient>.

It will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:9080/${project.artifactId}/HelloWorldMDBServletClient?topic>

### Access the Application Running in a Standalone Server

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/HelloWorldMDBServletClient>.

It will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/${project.artifactId}/HelloWorldMDBServletClient?topic>

## Investigate the Server Console Output

Review the messages in both ${product.name} server consoles or logs.

The following messages are sent to the queue:

    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-0 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 1
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-2 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 3
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-4 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 5
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-3 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 4
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-1 (ActiveMQ-client-global-threads)) Received Message from queue: This is message 2

The following messages are sent to the topic:

    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-5 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 1
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-6 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 2
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-8 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 4
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-7 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 3
    INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldTopicMDB] (Thread-9 (ActiveMQ-client-global-threads)) Received Message from topic: This is message 5


You will notice that only one of the nodes, elected as the singleton provider node, will be receiving the messages. For that, check both servers, only one will contain the received message log entries.

_Note:_ You will see the following warnings in the server logs. You can ignore these warnings as they are intended for production servers.

    WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the send buffer of socket DatagramSocket was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
    WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the receive buffer of socket DatagramSocket was set to 20MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
    WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the send buffer of socket MulticastSocket was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
    WARNING [org.jgroups.protocols.UDP] (Thread-0 (ActiveMQ-server-ActiveMQServerImpl::serverUUID=c79278db-56e6-11e5-af50-69dd76236ee8-1573164340)) JGRP000015: the receive buffer of socket MulticastSocket was set to 25MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)


## Electing a New Singleton Provider Server

If you reboot the singleton server node, the other node will be elected the new singleton provider, and will start receiving the MDB messages instead.

You should see the following output in the new singleton provider server:

    WFLYCLSV0003: master:quickstart-messagingcluster-nodeX elected as the singleton provider of the org.wildfly.ejb3.clustered.singleton service

Where `nodeX` will be either `node1` or `node2`, depending on which node is the new singleton provider.

If you now try to access the servlet urls, you will see that the new provider is receiving all new messages.

_Note:_ You will see the following warnings in the log of the server that is _not_ the singleton provider. These messages show that the other node went down unexpectedly, which is exactly the scenario we are reproducing in this quickstart. For that reason, those warnings can be ignored.

        WARN  [org.apache.activemq.artemis.core.client] (Thread-2 (ActiveMQ-client-global-threads)) AMQ212037: Connection failure has been detected: AMQ119015: The connection was disconnected because of server shutdown [code=DISCONNECTED]
        WARN  [org.apache.activemq.artemis.core.server] (Thread-2 (ActiveMQ-client-global-threads)) AMQ222095: Connection failed with failedOver=false


_Note:_ You may see the following log message as well. When a server is restarted, it may broadcast that it is up and running (with its nodeID) while other nodes still reference the previous server instance for the same nodeID. Eventually, the cluster will be informed of the new instance representing the given nodeID but as the warning explains, it is possible to see this log (once or more) when a server is restarted.

        WARN  [org.apache.activemq.artemis.core.client] (activemq-discovery-group-thread-dg-group1) AMQ212034: There are more than one servers on the network broadcasting the same node id. You will see this message exactly once (per node) if a node is restarted, in which case it can be safely ignored. But if it is logged continuously it means you really do have more than one node on the same network active concurrently with the same node id. This could occur if you have a backup node active at the same time as its live node. nodeID=a114b652-689e-11e7-a2f4-54ee751c6182

_Note:_ The next error message is a [known issue](https://issues.jboss.org/browse/WFLY-9261). You can ignore it, as it does not affect the scenario that this quickstart reproduces:

        ERROR [org.apache.activemq.artemis.core.server] (Thread-3 (ActiveMQ-client-global-threads)) AMQ224037: cluster connection Failed to handle message: java.lang.IllegalStateException: Cannot find binding for jms.queue.HelloWorldMDBQueuedea3e995-713c-11e7-85f2-b8f6b112daf7 on ClusterConnectionImpl@1129705701[nodeUUID=dabaa1fa-713c-11e7-8f3a-b8f6b112daf7, connector=TransportConfiguration(name=http-connector, factory=org-apache-activemq-artemis-core-remoting-impl-netty-NettyConnectorFactory) ?httpUpgradeEndpoint=http-acceptor&activemqServerName=default&httpUpgradeEnabled=true&port=9080&host=localhost, address=jms, server=ActiveMQServerImpl::serverUUID=dabaa1fa-713c-11e7-8f3a-b8f6b112daf7]

	at org.apache.activemq.artemis.core.server.cluster.impl.ClusterConnectionImpl$MessageFlowRecordImpl.doConsumerCreated(ClusterConnectionImpl.java:1294)

	at org.apache.activemq.artemis.core.server.cluster.impl.ClusterConnectionImpl$MessageFlowRecordImpl.handleNotificationMessage(ClusterConnectionImpl.java:1029)

	at org.apache.activemq.artemis.core.server.cluster.impl.ClusterConnectionImpl$MessageFlowRecordImpl.onMessage(ClusterConnectionImpl.java:1004)

	at org.apache.activemq.artemis.core.client.impl.ClientConsumerImpl.callOnMessage(ClientConsumerImpl.java:1001)

	at org.apache.activemq.artemis.core.client.impl.ClientConsumerImpl.access$400(ClientConsumerImpl.java:49)

	at org.apache.activemq.artemis.core.client.impl.ClientConsumerImpl$Runner.run(ClientConsumerImpl.java:1124)

	at org.apache.activemq.artemis.utils.OrderedExecutorFactory$OrderedExecutor$ExecutorTask.run(OrderedExecutorFactory.java:101)

	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)

	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)

	at java.lang.Thread.run(Thread.java:745)





### Rebooting the Singleton Provider Server Node in a Managed Domain

Run the following command, replacing ${jboss.home.name} with the path to your server, and replacing `NODE_X` in the script name with either `node1` or `node2`, depending on whether the current singleton provider is `node1` or `node2`.

    For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restart-NODE_X-domain.cli
    For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restart-NODE_X-domain.cli

### Rebooting the Singleton Provider Server Node in a Standalone Server
Stop the provider server and restart it again, using the same command you used to start the server initially.


## Disable and Enable the Delivery Group

To disable the delivery group "my-mdb-delivery-group" to which the topic belongs, run the `disable-delivery-group-domain.cli` or `disable-delivery-group-standalone.cli` script, located in the root directory of this quickstart. Follow the instructions in the next sections, depending on the server configuration you choose to run.

After disabling the delivery group, try sending messages to the topic, You should notice that the topic messages are not delivered when the delivery group is inactive.

Next, enable the delivery group using the appropriate `enable-delivery-group-domain.cli` or `enable-delivery-group-standalone.cli` script, also located in the root directory of this quickstart, so that the topic messages can be delivered again.

### Disable and Enable Delivery Group in a Managed Domain

To disable the delivery group named "my-mdb-delivery-group" to which the topic belongs, run the `disable-delivery-group-domain.cli` script, replacing ${jboss.home.name} with the path to your server:

    For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=disable-delivery-group-domain.cli
    For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=disable-delivery-group-domain.cli


Similarly, to enable the delivery group, run the `enable-delivery-group-domain.cli` script:

    For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=enable-delivery-group-domain.cli
    For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=enable-delivery-group-domain.cli


### Disable and Enable Delivery Group in a Standalone Server

To disable the delivery group named "my-mdb-delivery-group" to which the topic belongs, run the `disable-delivery-group-standalone.cli` script on both servers, replacing ${jboss.home.name} with the path to your server:

    For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=disable-delivery-group-standalone.cli
               ${jboss.home.name}_2/bin/jboss-cli.sh --connect controller=localhost:10090 --file=disable-delivery-group-standalone.cli
    For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=disable-delivery-group-standalone.cli
                   ${jboss.home.name}_2\bin\jboss-cli.bat --connect controller=localhost:10090 --file=disable-delivery-group-standalone.cli

Similarly, to enable the delivery group, run the `enable-delivery-group-standalone.cli` script in both servers:

    For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=enable-delivery-group-standalone.cli
               ${jboss.home.name}_2/bin/jboss-cli.sh --connect controller=localhost:10090 --file=enable-delivery-group-standalone.cli
    For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=enable-delivery-group-standalone.cli
                     ${jboss.home.name}_2\bin\jboss-cli.bat --connect controller=localhost:10090 --file=enable-delivery-group-standalone.cli


## Undeploy the Archive

When you are finished testing, use the following instructions to undeploy the quickstart.

### Undeploy the quickstart in a Managed Domain

1. Make sure you have started the managed domain as described above.
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the  `undeploy-domain.cli` script, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=undeploy-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=undeploy-domain.cli


### Undeploy the quickstart in a Standalone Server

1. Make sure you have started the standalone server as described above.
2. Open a command prompt, navigate to the root directory of this quickstart, and run the `undeploy-standalone.cli` script, replacing ${jboss.home.name}_1 and ${jboss.home.name}_2 with the path to the appropriate server:

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=undeploy-standalone.cli
                   ${jboss.home.name}_2/bin/jboss-cli.sh --connect controller=localhost:10090 --file=undeploy-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=undeploy-standalone.cli
                     ${jboss.home.name}_2\bin\jboss-cli.bat --connect controller=localhost:10090 --file=undeploy-standalone.cli


## Remove the Server Configuration

### Remove the Domain Server Configuration

You can remove the domain configuration by manually restoring the backup configuration files or by running the management CLI script.

#### Remove the Domain Server Configuration Manually

_Note: This method ensures the server is restored to its prior configuration._

1. If it is running, stop the ${product.name} server.
2. Restore the `${jboss.home.name}/domain/configuration/domain.xml` and `${jboss.home.name}/domain/configuration/host.xml` files with the back-up copies
of the files. Be sure to replace ${jboss.home.name} with the path to your server.

#### Remove the Domain Server Configuration by Running the Management CLI Script

_Note: This script returns the server to a default configuration and the result might not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the ${product.name} server by typing the following:

        For Linux:   ${jboss.home.name}/bin/domain.sh
        For Windows: ${jboss.home.name}\bin\domain.bat

2. Open a new command prompt, navigate to the root directory of this quickstart, and run the `remove-domain.cli` script, replacing ${jboss.home.name} with the path to your server.

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=remove-domain.cli

This script removes the server configuration that was done by the `install-domain.cli` script. You should see the following result following the script commands:

    The batch executed successfully


### Remove the Standalone Server Configuration

You can remove the domain configuration by manually restoring the back-up copies the configuration files or by running the management CLI script.

#### Remove the Standalone Server Configuration Manually

_Note: This method ensures the server is restored to its prior configuration._

1. If they are running, stop both ${product.name} servers.
2. Restore the `${jboss.home.name}_1/standalone/configuration/standalone-full-ha.xml` file with the back-up copies of the file. Be sure to replace ${jboss.home.name}_1 with the path to your server.

#### Remove the Standalone Configuration by Running the Management CLI Script

_Note: This script returns the server to a default configuration and the result might not match the server configuration prior to testing this quickstart. If you were not running with the default configuration before testing this quickstart, you should follow the intructions above to manually restore the configuration to its previous state._

1. Start the ${product.name} server by typing the following, replacing ${jboss.home.name}_1 with the path to your first server:

        For Linux:   ${jboss.home.name}_1/bin/standalone.sh -c standalone-full-ha.xml
        For Windows: ${jboss.home.name}_1\bin\domain.bat -c standalone-full-ha.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the `remove-standalone.cli` script, replacing ${jboss.home.name}_2 with the path to your second server.

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=remove-standalone.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=remove-standalone.cli
This script removes the server configuration that was done by the `install-standalone.cli` script. You should see the following result following the script commands:

    The batch executed successfully

### Delete the Cloned Standalone ${product.name} Directory

1. If it is running, stop the second instance of the ${product.name} server.
2. Delete the cloned directory.
