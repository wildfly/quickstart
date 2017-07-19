# ha-singleton-service: Deploying Cluster-wide Singleton MSC Services

Author: Radoslav Husar  
Level: Advanced  
Technologies: MSC, Singleton Service, Clustering  
Summary: The `ha-singleton-service` quickstart demonstrates how to deploy a cluster-wide singleton MSC service.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The `ha-singleton-services` quickstart demonstrates two patterns, or ways, to deploy a cluster-wide singleton MSC service.

1. The first example, located in the `primary-only/` directory of the quickstart, demonstrates a singleton service and a querying service deployed on all nodes that regularly queries for the value provided by the singleton service.
2. The second example, located in the `with-backups/` directory of the quickstart, demonstrates a singleton service that is installed with a backup service. The backup services are running on all nodes that were not elected to be running the singleton service itself.

Singleton service's `getValue()` always returns the value of the primary node unless a backup service is installed.
If no backup service is installed, a default backup service is used whose `getValue()` returns the service value of the primary node. 
Should a backup service be installed then the `getValue()` is delegated to the primary or backup service depending on the state of the local node.

Be sure to inspect the `activate()` method of the `ServiceActivator` class for each example. Although the default election policy is used to build the singleton services for each of these examples, scripts and instructions are provided later in this document to demonstrate how to [configure other election policies](#configuring-election-policies).

These examples are built and packaged as JAR archives.

For more information about clustered singleton services, see _HA Singleton Service_ in the [Development Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for JBoss EAP.

## System Requirements

The deployments this project produces are designed to be run on ${product.name.full} ${product.version} or later.

Everything needed to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure the environment is configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}_1 and ${jboss.home.name}_2

This quickstart requires that you clone your `${jboss.home.name}` installation directory and run two servers. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

In the following instructions, replace `${jboss.home.name}_1` with the path to your first ${product.name} server and replace `${jboss.home.name}_2` with the path to your second cloned ${product.name} server.

## Clone the ${product.name} Directory

While you can run this example starting only one instance of the server, if you want to see the singleton behavior, you must start at least two instances of the server. Copy the entire ${product.name} directory to a new location to use for the second cluster member.

## Start the Servers with the HA Profile

_Note: You must start the server using the HA profile or the singleton service will not start correctly._

Start the two ${product.name} servers with the HA profile, passing a unique node ID. These logical node names are used in the log to identify which node is elected. If you are running the servers on the same host, you must also pass a socket binding port offset on the command line to start the second server.
To start the servers, type the following commands.

For Linux:

    Server 1: ${jboss.home.name}_1/bin/standalone.sh -c standalone-ha.xml -Djboss.node.name=node1
    Server 2: ${jboss.home.name}_2/bin/standalone.sh -c standalone-ha.xml -Djboss.node.name=node2 -Djboss.socket.binding.port-offset=100

For Windows:

    Server 1: ${jboss.home.name}_1\bin\standalone.bat -c standalone-ha.xml -Djboss.node.name=node1
    Server 2: ${jboss.home.name}_2\bin\standalone.bat -c standalone-ha.xml -Djboss.node.name=node2 -Djboss.socket.binding.port-offset=100

This example is not limited to two servers. Additional servers can be started by specifying a unique node name and port offset for each one.

## Run the primary-only Example

This example demonstrates a singleton service and a querying service that regularly queries for the value that the singleton service provides.

### Build and Deploy primary-only to Server 1

1. Start the ${product.name} servers as described in the above section.
2. Open a command prompt and navigate to the `primary-only/` directory located in the root directory of this quickstart.
3. Use the following command to clean up any previously built artifacts, and to build and deploy the JAR archive.

        mvn clean install wildfly:deploy

4. Investigate the primary-only Console Output for Server 1. Verify that the `target/ha-singleton-service-primary-only.jar` archive is deployed to `node1`, which is the first server started without port offset, by checking the server log.

        INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "ha-singleton-service-primary-only.jar" (runtime-name: "ha-singleton-service-primary-only.jar")
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.ServiceActivator] (MSC service thread 1-5) Singleton and querying services activated.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-3) Querying service is starting.
        ...
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-7) Singleton service is starting on node 'node1'.
        ...
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.

_NOTE:_ You might see the following warnings in the server log after the applications are deployed. These warnings can be ignored in a development environment.

    WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 20MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
    WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 25MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)

### Deploy the primary-only Archive to Server 2

1. Use the following command to deploy the same archive to the second server. Because the default socket binding port for deployment is `9990` and the second server ports are offset by `100`, you must pass the sum, `10090`, for the socket binding port as the argument to the `deploy` Maven goal.

        mvn wildfly:deploy -Dwildfly.port=10090

2. Investigate the primary-only console output for both servers. Verify that the `target/ha-singleton-service-primary-only.jar` archive is deployed to `node2` by checking the server log.

        INFO  [org.jboss.as.repository] (management-handler-thread - 4) WFLYDR0001: Content added at location /Users/rhusar/wildfly/build/target/y/standalone/data/content/18/6efcc6c07b471f641cfcc97f9120505726e6bd/content
        INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) WFLYSRV0027: Starting deployment of "ha-singleton-service-primary-only.jar" (runtime-name: "ha-singleton-service-primary-only.jar")
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.ServiceActivator] (MSC service thread 1-6) Singleton and querying services activated.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-5) Querying service is starting.
        ...
        INFO  [org.jboss.as.server] (management-handler-thread - 4) WFLYSRV0010: Deployed "ha-singleton-service-primary-only.jar" (runtime-name : "ha-singleton-service-primary-only.jar")
        ...
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.

3. Inspect the server log of the first node. Since the cluster membership has changed, the election policy determines which node will run the singleton.

        INFO  [org.infinispan.CLUSTER] (remote-thread--p7-t1) ISPN000336: Finished cluster-wide rebalance for cache default, topology id = 5
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node1 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service

4. Verify that the querying service is running on all nodes and that all are querying the same singleton service instance by confirming that the same node name is printed in the log. Both nodes will output the following message every 5 seconds:

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.

### Test Singleton Service Failover for the primary-only Example

1. To verify failover of the singleton service, shut down the server operating as the singleton master by using the `Ctrl` + `C` key combination in the command prompt. The following messages confirm that the node is shut down.

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.
        INFO  [org.jboss.as.server] (Thread-2) WFLYSRV0220: Server shutdown has been requested via an OS signal
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-3) Singleton service is stopping on node 'node1'.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-6) Querying service is stopping.
        ...
        INFO  [org.jboss.as] (MSC service thread 1-6) WFLYSRV0050: JBoss EAP 7.1.0.Beta1 (WildFly Core 3.0.0.Beta26-redhat-1) stopped in 66ms

2. Now observe the log messages on the second server. The second node is now elected as the singleton master.

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node2 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-8) Singleton service is starting on node 'node2'.

### Undeploy the primary-only Example

1. Start the ${product.name} servers as described in the above section.
2. Open a command prompt and navigate to the `primary-only/` directory located in the root directory of this quickstart.
3. Use the following command to undeploy the JAR archive from Server 1.

        mvn wildfly:undeploy

4. Use the following command to undeploy the JAR archive from Server 2.

        mvn wildfly:undeploy -Dwildfly.port=10090

## Run the with-backups Example

### Build and Deploy with-backups to Server 1
This example demonstrates a singleton service that is installed with a backup service. The backup service is running on all nodes that are _not_ elected to be running the singleton service.

1. Start the ${product.name} servers as described in the above section.
2. Open a command prompt and navigate to the `with-backups/` directory located in the root directory of this quickstart.
3. Use the following command to clean up any previously built artifacts, and build and deploy the JAR archive.

        mvn clean install wildfly:deploy

### Deploy the with-backups Archive to Server 2

Use the following command to deploy the same archive to the second server. Because the default socket binding port for deployment is `9990` and the second server ports are offset by `100`, you must pass the sum, `10090`, for the socket binding port as the argument to the `deploy` Maven goal.

    mvn wildfly:deploy -Dwildfly.port=10090

### Investigate the with-backups Console Output for Both Servers

Verify that the `target/ha-singleton-service-with-backups.jar` archive is deployed to `node1`, which is the first server started without port offset, by checking the server log to see that the primary service is running.

    INFO  [org.jboss.as.quickstarts.ha.singleton.service.backups.SingletonService] (pool-18-thread-1) Primary singleton service is running on node 'node1'.


All other nodes log that the backup singleton service is running.

    INFO  [org.jboss.as.quickstarts.ha.singleton.service.backups.SingletonService] (pool-20-thread-1) Backup singleton service is running on node 'node2'.

### Undeploy the with-backups Example

1. Start the ${product.name} servers as described in the above section.
2. Open a command prompt and navigate to the `primary-only/` directory located in the root directory of this quickstart.
3. Use the following command to undeploy the JAR archive from Server 1.

        mvn wildfly:undeploy

4. Use the following command to undeploy the JAR archive from Server 2.

        mvn wildfly:undeploy -Dwildfly.port=10090


## Configuring Election Policies

As mentioned previously, the `activate()` method in the `ServiceActivator` class for each example in this quickstart uses the default election policy to build the singleton services. Once you have successfully deployed and verified these examples, you might want to test different election policy configurations to see how they work.

Election policies are configured using ${product.name} management CLI commands. Scripts are provided to configure a simple [name preference election policy](#configure-a-name-preference-election-policy) and a [random election policy](#configure-a-random-election-policy). A script is also provided to configure a [quorum for the singleton policy](#configure-a-singleton-policy-that-defines-a-quorum).

### Configure a Name Preference Election Policy

This example configures the default election policy to be based on logical names.

1. If you have tested other election policies that configured the `singleton` subsystem, see [Restoring the Default Singleton Subsystem Configuration](#restoring-the-default-singleton-subsystem-configuration) for instructions to restore the singleton election policy to the default configuration.
2. Start the two servers with the HA profile as described above.
3. Review the contents of the `name-preference-election-policy-add.cli` file located in the root of this quickstart directory. This script configures the default election policy to choose nodes in a preferred order of `node3`, `node2`, and `node1` using this command.

        /subsystem=singleton/singleton-policy=default/election-policy=simple:write-attribute(name=name-preferences,value=[node3,node2,node1])
4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command to execute the script for Server 1. Be sure to replace ${jboss.home.name}_1 with the path to the target Server 1.

        For Linux: ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=name-preference-election-policy-add.cli
        For Windows: ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=name-preference-election-policy-add.cli

    You should see the following result when you run the script.

        The batch executed successfully
        process-state: reload-required

    Note that the `name-preference-election-policy-add.cli` batch script executes the `reload` command, so a reload is not required.

5. Stop the server and review the changes made to the `standalone-ha.xml` server configuration file by the management CLI commands. The `singleton` subsystem now contains a  `name-preferences` element under the `simple-election-policy` that specifies the preferences `node3 node2 node1`.

        <subsystem xmlns="urn:jboss:domain:singleton:1.0">
            <singleton-policies default="default">
                <singleton-policy name="default" cache-container="server">
                    <simple-election-policy>
                        <name-preferences>node3 node2 node1</name-preferences>
                    </simple-election-policy>
                </singleton-policy>
            </singleton-policies>
        </subsystem>

6. Repeat these steps for the second server. Note that if the second server is using a port offset, you must specify the controller address on the command line by adding `--controller=localhost:10090`.

        For Linux: ${jboss.home.name}_2/bin/jboss-cli.sh --connect --controller=localhost:10090 --file=name-preference-election-policy-add.cli
        For Windows: ${jboss.home.name}_2\bin\jboss-cli.bat --connect --controller=localhost:10090 --file=name-preference-election-policy-add.cli

7. Be sure both servers are started, deploy one of the examples to both servers, and verify that the election policy is now in effect. The server running the election policy should now log the following message.

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node2 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service

    The other nodes should log the following message.

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-7-thread-1) Singleton service is running on node 'node2'.


### Configure a Random Election Policy

This example configures an election policy that elects a random cluster member when the cluster membership changes.

1. If you have tested other election policies that configured the `singleton` subsystem, see [Restoring the Default Singleton Subsystem Configuration](#restoring-the-default-singleton-subsystem-configuration) for instructions to restore the singleton election policy to the default configuration.
2. Start the two servers with the HA profile as described above.
3. Review the contents of the `random-election-policy-add.cli` file located in the root of this quickstart directory. This script removes the default simple election policy and configures the default election policy to elect a random cluster member using these commands.

        /subsystem=singleton/singleton-policy=default/election-policy=simple:remove(){allow-resource-service-restart=true}
        /subsystem=singleton/singleton-policy=default/election-policy=random:add()

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command to execute the script for Server 1. Be sure to replace ${jboss.home.name}-1 with the path to the target Server 1.

        For Linux: ${jboss.home.name}-1/bin/jboss-cli.sh --connect --file=random-election-policy-add.cli
        For Windows: ${jboss.home.name}-1\bin\jboss-cli.bat --connect --file=random-election-policy-add.cli

    You should see the following result when you run the script.

        The batch executed successfully
        process-state: reload-required

    Note that the `random-election-policy-add.cli` batch script executes the `reload` command, so a reload is not required.

5. Stop the server and review the changes made to the `standalone-ha.xml` server configuration file by the management CLI commands. The `singleton` subsystem now contains a  `random-election-policy` element under the `singleton-policy` that specifies the preferences `node3 node2 node1`.

        <subsystem xmlns="urn:jboss:domain:singleton:1.0">
            <singleton-policies default="default">
                <singleton-policy name="default" cache-container="server">
                    <random-election-policy/>
                </singleton-policy>
            </singleton-policies>
        </subsystem>

6. Repeat these steps for the second server. Note that if the second server is using a port offset, you must specify the controller address on the command line by adding `--controller=localhost:10090`.

        For Linux: ${jboss.home.name}_2/bin/jboss-cli.sh --connect --controller=localhost:10090 --file=random-election-policy-add.cli
        For Windows: ${jboss.home.name}_2\bin\jboss-cli.bat --connect --controller=localhost:10090 --file=random-election-policy-add.cli

7. Be sure both servers are started, deploy one of the examples to both servers, and verify that the election policy is now in effect.

### Configure a Quorum for the Singleton Policy

A quorum specifies the minimum number of cluster members that must be present for the election to even begin. This mechanism is used to mitigate a split brain problem by sacrificing the availability of the singleton service. If there are less members than the specified quorum, no election is performed and the singleton service is not run on any node.

1. If you have tested other election policies that configured the `singleton` subsystem, see [Restoring the Default Singleton Subsystem Configuration](#restoring-the-default-singleton-subsystem-configuration) for instructions to restore the singleton election policy to the default configuration.
2. Start the two servers with the HA profile as described above.
3. Review the contents of the `quorum-add.cli` file located in the root of this quickstart directory. This script specifies the minimum number of cluster members required for the singleton policy using this command.

        /subsystem=singleton/singleton-policy=default:write-attribute(name=quorum,value=2)

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command to execute the script for Server 1. Be sure to replace ${jboss.home.name}-1 with the path to the target Server 1.

        For Linux: ${jboss.home.name}-1/bin/jboss-cli.sh --connect --file=quorum-add.cli
        For Windows: ${jboss.home.name}-1\bin\jboss-cli.bat --connect --file=quorum-add.cli

    You should see the following result when you run the script.

        The batch executed successfully
        process-state: reload-required

    Note that the `quorum-add.cli` batch script executes the `reload` command, so a reload is not required.

5. Review the changes made to the `standalone-ha.xml` server configuration file by the management CLI commands. The `singleton` subsystem now contains a `quorum` attribute for the `singleton-policy` element that specifies the minimum number.

        <subsystem xmlns="urn:jboss:domain:singleton:1.0">
            <singleton-policies default="default">
                <singleton-policy name="default" cache-container="server" quorum="2">
                    <random-election-policy/>
                </singleton-policy>
            </singleton-policies>
        </subsystem>

6. Repeat these steps for the second server. Note that if the second server is using a port offset, you must specify the controller address on the command line by adding `--controller=localhost:10090`.

          For Linux: ${jboss.home.name}_2/bin/jboss-cli.sh --connect --controller=localhost:10090 --file=quorum-add.cli
          For Windows: ${jboss.home.name}_2\bin\jboss-cli.bat --connect --controller=localhost:10090 --file=quorum-add.cli

7. Be sure both servers are started, deploy one of the examples to both servers. While both servers are running, observe the server logs. The server running the election policy should now log the following message.

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0007: Just reached required quorum of 2 for org.jboss.as.quickstarts.ha.singleton.service.primary-only service. If this cluster loses another member, no node will be chosen to provide this service.

8. Shut down one of the servers by using the `Ctrl` + `C` key combination in the command prompt to verify that no singleton service will be running after the quorum is not reached.

        WARN  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0006: Failed to reach quorum of 2 for org.jboss.as.quickstarts.ha.singleton.service.primary-only service. No singleton master will be elected.
        INFO  [org.wildfly.clustering.server] (thread-20) WFLYCLSV0002: This node will no longer operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-3) Singleton service is stopping on node 'node2'.
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-2) ISPN000094: Received new cluster view for channel server: [node2|4] (1) [node2]
        ...
        WARN  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Failed to query singleton service.

8. A `quorum-remove.cli` script is provided in the root directory of this quickstart that removes the quorum limit from the `singleton` subsystem.

## Troubleshooting Runtime Problems

If the singleton is running on multiple nodes, check for the following issues.

* The most common cause of this problem is starting the servers with the `standalone.xml` or `standalone-full.xml` profile instead of with the `standalone-ha.xml` or `standalone-full-ha.xml` profile. Make sure to start the server with an HA profile using `-c standalone-ha.xml`.

* Another common cause is because the server instances did not discover each other and each server is operating as a singleton cluster. Ensure that `multicast` is enabled or change the `jgroups` subsystem configuration to use a different discovery mechanism. Confirm the following message in the server log to ensure that the discovery was successful.

        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000094: Received new cluster view for channel server: [node1|1] (2) [node1, node2]

## Undeploy the Deployments

If you have not yet done so, you can undeploy all of the deployed artifacts by following these steps.

1. Start the two servers with the HA profile as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Use the following commands to undeploy all of the artifacts.

        mvn wildfly:undeploy
        mvn wildfly:undeploy -Dwildfly.port=10090

## Restoring the Default Singleton Subsystem Configuration

Some of these examples require that you modify the election policies for the `singleton` subsystem by running management CLI scripts. After you have completed testing each configuration, it is important to restore the `singleton` subsystem to its default configuration before you run any other examples.

1. Start both servers with the HA profile as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Restore your default server configurations by running these commands.

    * For Linux:

            ${jboss.home.name}_1/bin/jboss-cli.sh --connect --file=restore-singleton-subsystem.cli
            ${jboss.home.name}_2/bin/jboss-cli.sh --connect --controller=localhost:10090 --file=restore-singleton-subsystem.cli

    * For Windows:

            ${jboss.home.name}_1\bin\jboss-cli.bat --connect --file=restore-singleton-subsystem.cli
            ${jboss.home.name}_2\bin\jboss-cli.bat --connect --controller=localhost:10090 --file=restore-singleton-subsystem.cli

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
