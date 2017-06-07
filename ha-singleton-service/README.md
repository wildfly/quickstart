# ha-singleton-service: Deploying Cluster-wide Singleton MSC Services

Author: Radoslav Husar  
Level: Advanced  
Technologies: MSC, Singleton Service, Clustering  
Summary: The `ha-singleton-service` quickstart demonstrates deploying a cluster-wide singleton MSC service.
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The `ha-singleton-services` quickstart demonstrates two patterns of deploying a cluster-wide singleton MSC service.
The first pattern in the `primary-only` directory demonstrates a singleton service and a querying service which regularly queries the value the singleton service provides.
It is transparent whether the singleton service is running on the same node or the value is obtained remotely.
The second pattern in the `with-backups` directory demonstrates a singleton service which is installed with a backup service.
The backup service is running on all nodes which are not elected to be running the singleton service. 

The examples are built and packaged as JAR archives.

For more information about singleton deployments, see _HA Singleton Deployments_ in the [Development Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for JBoss EAP.

## System Requirements

The deployments this project produces is designed to be run on ${product.name.full} ${product.version} or later.

Everything needed to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure the environment is configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Setting Up the Test Environment

To demonstrate the singleton behavior, at least two application server instances must be started.
Begin by making a copy of the entire ${product.name} directory to be used as second cluster member.
Note that the example can be run on a single node as well, but without observation of the singleton properties.

Start the two ${product.name} servers with the same HA profile using the following commands.
Note that a socket binding port offset and a unique node name must be passed to the second server if the servers are binding to the same host.
These logical node names are used in the log to identify which node is elected. 

For Linux:

    Server 1: ${jboss.home.name}-1/bin/standalone.sh -c standalone-ha.xml -Djboss.node.name=node1
    Server 2: ${jboss.home.name}-2/bin/standalone.sh -c standalone-ha.xml -Djboss.node.name=node2 -Djboss.socket.binding.port-offset=100

For Windows:

    Server 1: ${jboss.home.name}-1\bin\standalone.bat -c standalone-ha.xml -Djboss.node.name=node1
    Server 2: ${jboss.home.name}-2\bin\standalone.bat -c standalone-ha.xml -Djboss.node.name=node2 -Djboss.socket.binding.port-offset=100

The demonstration is not limited to two servers. Additional servers can be started by specifying a unique port offset for each one.


## Running the Quickstart

1. Start the ${product.name} servers as described in the above section.
2. Navigate to the root directory of this quickstart in the command prompt.
3. Since there are two patterns to demonstrate, navigate to the `primary-only` directory (maven module).
4. Use the following command to clean up previously built artifacts, and build and deploy the JAR archive:

      mvn clean install wildfly:deploy

5. Ensure the `target/ha-singleton-service-primary-only.jar` archive is deployed to `node1` (the one without port offset) by observing the log.

        INFO  [org.jboss.as.server.deployment] (MSC service thread 1-7) WFLYSRV0027: Starting deployment of "ha-singleton-service-primary-only.jar" (runtime-name: "ha-singleton-service-primary-only.jar")
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.ServiceActivator] (MSC service thread 1-5) Singleton and querying services activated.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-3) Querying service is starting.
        ...
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-7) Singleton service is starting on node 'node1'.
        ...
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.

     Note that the following warnings might appear in the server output after the applications are deployed. These can be safely ignored in a development environment.

        WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 20MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
        WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 25MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)

6. Use the following command to deploy the already built archive to the second server. Note that since the default socket binding port is `9990` and the second server has ports offsetted by `100`, the sum, `10090` must be passed as an argument to the deploy maven goal.

        mvn wildfly:deploy -Dwildfly.port=10090

7. Ensure the `target/ha-singleton-service-primary-only.jar` archive is deployed to `node2` by observing the log.

        INFO  [org.jboss.as.repository] (management-handler-thread - 4) WFLYDR0001: Content added at location /Users/rhusar/wildfly/build/target/y/standalone/data/content/18/6efcc6c07b471f641cfcc97f9120505726e6bd/content
        INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) WFLYSRV0027: Starting deployment of "ha-singleton-service-primary-only.jar" (runtime-name: "ha-singleton-service-primary-only.jar")
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.ServiceActivator] (MSC service thread 1-6) Singleton and querying services activated.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-5) Querying service is starting.
        ...
        INFO  [org.jboss.as.server] (management-handler-thread - 4) WFLYSRV0010: Deployed "ha-singleton-service-primary-only.jar" (runtime-name : "ha-singleton-service-primary-only.jar")
        ...
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.
        
      Inspect the log of the first node. Since the cluster membership has changed, the election policy will determine the node node which will run the singleton.
      
        INFO  [org.infinispan.CLUSTER] (remote-thread--p7-t1) ISPN000336: Finished cluster-wide rebalance for cache default, topology id = 5
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node1 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service

8. Verify the querying service is running on all nodes and that all are querying the same singleton service instance (same node name is printed in the log). Both nodes will output the following every 5 seconds:

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.

9. Verify failover of the singleton service. Shutdown the server operating as the singleton master, for instance by using the `Ctrl` + `C` key combination in the command prompt. Observe the following messages on the node being shutdown:

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Singleton service is running on node 'node1'.
        INFO  [org.jboss.as.server] (Thread-2) WFLYSRV0220: Server shutdown has been requested via an OS signal
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-3) Singleton service is stopping on node 'node1'.
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (MSC service thread 1-6) Querying service is stopping.
        ...
        INFO  [org.jboss.as] (MSC service thread 1-6) WFLYSRV0050: WildFly Core 3.0.0.Beta25 "Kenny" stopped in 66ms

     Now observe the log messages on the second server. The node will now be elected as the singleton master.

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node2 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-8) Singleton service is starting on node 'node2'.


## Singleton Service with Backup Service

The other pattern is located in the `with-backups` directory/module.
The singleton service is configured with a backup service.
The backup service is run on all nodes which were not elected to be running the singleton service, i.e. primary node.
Repeat the above steps noting that the node running the singleton service will log:

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.backups.SingletonService] (pool-18-thread-1) Primary singleton service is running on node 'node1'.

while all other nodes will log:

        INFO  [org.jboss.as.quickstarts.ha.singleton.service.backups.SingletonService] (pool-20-thread-1) Backup singleton service is running on node 'node2'.


## Election Policies

1. Inspect the source code of the `ServiceActivator` and its `activate` method. In the quickstart's source code the default election policy is used to build the singleton services which can be modified in the server configuration. 

2. Ensure the servers are started and configure the default election policy to be based on logical names. 
   Start the command line interface (CLI) and add a simple election policy with ordered list of logical names.
   Note that the operation leaves the server in the `reload-required` state, thus a `reload` operation need to follow to apply the changes into runtime.
  
        $ ./bin/jboss-cli.sh -c  
        [standalone@localhost:9990 /] /subsystem=singleton/singleton-policy=default/election-policy=simple:add(name-preferences=[node3,node2,node1])
        {
            "outcome" => "success",
            "response-headers" => {
                "operation-requires-reload" => true,
                "process-state" => "reload-required"
            }
        }
        [standalone@localhost:9990 /] :reload
        {
            "outcome" => "success",
            "result" => undefined
        }
        
   Inspect the server profile for the configuration change done by the CLI command:
   
        <subsystem xmlns="urn:jboss:domain:singleton:1.0">
            <singleton-policies default="default">
                <singleton-policy name="default" cache-container="server">
                    <simple-election-policy>
                        <name-preferences>node3 node2 node1</name-preferences>
                    </simple-election-policy>
                </singleton-policy>
            </singleton-policies>
        </subsystem>
        
    Repeat the same steps for the second server. Note that if using port offset, starting the CLI will require specifying the controller address by appending `--controller=127.0.0.1:10090`.

3. Verify that the election policy now in effect. The server running the election policy should now log the following:
 
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node2 elected as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        
   while all nodes log the following:
       
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-7-thread-1) Singleton service is running on node 'node2'.
        
4. Experiment with other election policies. In the step 2 use `random` election policy which elects a random cluster member when the cluster membership changes.

        [standalone@127.0.0.1:9990 /] /subsystem=singleton/singleton-policy=default/election-policy=simple:remove(){allow-resource-service-restart=true}
        {"outcome" => "success"}
        [standalone@127.0.0.1:9990 /] /subsystem=singleton/singleton-policy=default/election-policy=random:add()
        {"outcome" => "success"}
        [standalone@localhost:9990 /] :reload
        {
            "outcome" => "success",
            "result" => undefined
        }

For convenience, the management CLI script to configure name-preference, `name-preference-election-policy-add.cli`, and to replace with random, `random-election-policy-add.cli`, are located in the root directory of this quickstart.

## Quorum

Quorum specifies the minimum number of cluster members that must be present for election to even begin.
This mechanism is used to mitigate split brain problem by sacrificing availability of the singleton service.
If there are less members than the quorum, no election will be performed and the singleton service will not be running on any node.

1. Ensure the servers are started and configure quorum. Run the following operation in the CLI:

        [standalone@127.0.0.1:9990 /] /subsystem=singleton/singleton-policy=default/:write-attribute(name=quorum,value=2)
        {
            "outcome" => "success",
            "response-headers" => {
                "operation-requires-reload" => true,
                "process-state" => "reload-required"
            }
        }
        [standalone@127.0.0.1:9990 /] :reload
        {
            "outcome" => "success",
            "result" => undefined
        }
        
2. While both servers are running, observe the logs. The server will warn that the number of cluster members is equal to the quorum: 

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0007: Just reached required quorum of 2 for org.jboss.as.quickstarts.ha.singleton.service.primary-only service. If this cluster loses another member, no node will be chosen to provide this service.

3. Stop one of the servers, for instance by using the `Ctrl` + `C` key combination in the command prompt, to verify that no singleton service will be running after the quorum is not reached.

        WARN  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0006: Failed to reach quorum of 2 for org.jboss.as.quickstarts.ha.singleton.service.primary-only service. No singleton master will be elected.
        INFO  [org.wildfly.clustering.server] (thread-20) WFLYCLSV0002: This node will no longer operate as the singleton provider of the org.jboss.as.quickstarts.ha.singleton.service.primary-only service
        INFO  [org.jboss.as.quickstarts.ha.singleton.service.primary.SingletonService] (MSC service thread 1-3) Singleton service is stopping on node 'node2'.
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-2) ISPN000094: Received new cluster view for channel server: [node2|4] (1) [node2]
        ...
        WARN  [org.jboss.as.quickstarts.ha.singleton.service.primary.QueryingService] (pool-4-thread-1) Failed to query singleton service.

For convenience, the management CLI script to configure quorum, `quorum-add.cli`, and to remove, `quorum-remove.cli`, are located in the root directory of this quickstart.

## Troubleshooting

Should the singleton be running on multiple nodes, the most common causes are accidentally starting with the `standalone.xml` or `standalone-full.xml` profile instead of with the `standalone-ha.xml` or `standalone-full-ha.xml` profile.
Make sure to start the server with an HA profile using `-c standalone-ha.xml`.

Another common cause is that the server instances did not discover each other and each server is operating as a singleton cluster. Ensure that multicast is enabled or change the `jgroups` subsystem configuration to use a different discovery mechanism.
Observe the following log line to ensure that the discovery was successful:

    INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000094: Received new cluster view for channel server: [node1|1] (2) [node1, node2]

## Debugging

To debug the source code of any library in the project, run the following maven goal to download the sources into the local repository.

    mvn dependency:sources
    
## Undeploy the Deployments

1. Ensure all ${product.name} servers are started.
2. Navigate to the root directory of this quickstart in the command prompt.
3. Use the following commands to undeploy the artifacts:

        mvn wildfly:undeploy
        mvn wildfly:undeploy -Dwildfly.port=10090
