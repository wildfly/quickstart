# ha-singleton-deployment: Deploying Cluster-wide Singleton Services Using Singleton Deployments

Author: Radoslav Husar  
Level: Advanced  
Technologies: EJB, Singleton Deployments, Clustering  
Summary: The `ha-singleton-deployment` quickstart demonstrates the recommended way to deploy any service packaged in an application archive as a cluster-wide singleton.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The `ha-singleton-deployment` quickstart demonstrates the deployment of a service packaged in an application as a cluster-wide singleton using singleton deployments.
In this example, the service is a timer that is initialized by a `@Startup @Singleton` bean.
The example is built and packaged as a single EJB archive.

For more information about singleton deployments, see _HA Singleton Deployments_ in the [Development Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for JBoss EAP.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

Everything needed to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure the environment is configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Setting Up the Test Environment

To demonstrate the singleton behavior, at least two application server instances must be started.
Begin by making a copy of the entire ${product.name} directory to be used as second cluster member.
Note that the example can be run on a single node as well, but without observation of the singleton properties.

Start the two ${product.name} servers with the same HA profile using the following commands.
Note that a socket binding port offset and a unique node name must be passed to the second server if the servers are binding to the same host.

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
3. Use the following command to clean up previously built artifacts, and build and deploy the EJB archive:

      mvn clean install wildfly:deploy

4. Ensure the `target/${project.artifactId}.jar` archive is deployed to `node1` (the one without port offset) by observing the log.

        INFO [org.jboss.as.server.deployment] (MSC service thread 1-1) WFLYSRV0027: Starting deployment of "ha-singleton-deployment.jar" (runtime-name: "ha-singleton-deployment.jar")
        ...
        INFO [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node1 elected as the singleton provider of the jboss.deployment.unit."ha-singleton-deployment.jar".FIRST_MODULE_USE service
        INFO [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the jboss.deployment.unit."ha-singleton-deployment.jar".FIRST_MODULE_USE service
        INFO [org.jboss.as.server] (management-handler-thread - 4) WFLYSRV0010: Deployed "ha-singleton-deployment.jar" (runtime-name : "ha-singleton-deployment.jar")
        ...
        WARNING [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (ServerService Thread Pool -- 68) SingletonTimer is initializing.
        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 1) SingletonTimer: Hello World!

     Note that the following warnings might appear in the server output after the applications are deployed. These can be safely ignored in a development environment.

        WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 20MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
        WARN  [org.jboss.as.clustering.jgroups.protocol.UDP] (ServerService Thread Pool -- 68) JGRP000015: the receive buffer of socket MulticastSocket was set to 25MB, but the OS only allocated 6.71MB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)

5. Use the following command to deploy the already built archive to the second server. Note that since the default socket binding port is `9990` and the second server has ports offsetted by `100`, the sum, `10090` must be passed as an argument to the deploy maven goal.

        mvn wildfly:deploy -Dwildfly.port=10090

6. Ensure the `service/target/${project.artifactId}.jar` archive is deployed to `node2` by observing the log. Note that even though the logs indicate "Deployed", the deployment does not actually deploy completely and the timer is not operating on this node.

        INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) WFLYSRV0027: Starting deployment of "ha-singleton-deployment.jar" (runtime-name: "ha-singleton-deployment.jar")
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000078: Starting JGroups channel server
        ...
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000094: Received new cluster view for channel server: [node1|1] (2) [node1, node2]
        ...
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000079: Channel server local address is node2, physical addresses are [127.0.0.1:55300]
        INFO  [org.infinispan.factories.GlobalComponentRegistry] (MSC service thread 1-6) ISPN000128: Infinispan version: Infinispan 'Chakra' 8.2.6.Final
        INFO  [org.jboss.as.clustering.infinispan] (ServerService Thread Pool -- 68) WFLYCLINF0002: Started default cache from server container
        INFO  [org.jboss.as.server] (management-handler-thread - 2) WFLYSRV0010: Deployed "ha-singleton-deployment.jar" (runtime-name : "ha-singleton-deployment.jar")

7. Verify the timer is running only on one instance by observing the logs. The node running the timer will output the following every 5 seconds:

        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 1) SingletonTimer: Hello World!

     While the instance not running, the timer will display the following as the last log line:

        INFO  [org.jboss.as.server] (management-handler-thread - 2) WFLYSRV0010: Deployed "ha-singleton-deployment.jar" (runtime-name : "ha-singleton-deployment.jar")

8. Verify failover of the singleton deployment. Shutdown the server operating as the singleton master, for instance by using the `Ctrl` + `C` key combination in the command prompt. Observe the following messages on the node being shutdown:

        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 3) SingletonTimer: Hello World!
        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 4) SingletonTimer: Hello World!
        INFO  [org.jboss.as.server] (Thread-2) WFLYSRV0220: Server shutdown has been requested via an OS signal
        WARNING [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (ServerService Thread Pool -- 31) SingletonTimer is stopping: the server is either being shutdown or another node has become elected to be the singleton master.
        ...
        INFO  [org.jboss.as] (MSC service thread 1-6) WFLYSRV0050: WildFly Core 3.0.0.Beta13 "Kenny" stopped in 88ms

     Now observe the log messages on the second server. The node will now be elected as the singleton master, deployment will complete, and the timer will start operating:

        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0003: node2 elected as the singleton provider of the jboss.deployment.unit."ha-singleton-deployment.jar".FIRST_MODULE_USE service
        INFO  [org.wildfly.clustering.server] (DistributedSingletonService - 1) WFLYCLSV0001: This node will now operate as the singleton provider of the jboss.deployment.unit."ha-singleton-deployment.jar".FIRST_MODULE_USE service
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (thread-4) ISPN000094: Received new cluster view for channel server: [node2|2] (1) [node2]
        ...
        WARNING [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (ServerService Thread Pool -- 68) SingletonTimer is initializing.
        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 1) SingletonTimer: Hello World!
        INFO  [class org.jboss.as.quickstarts.ha.singleton.SingletonTimer] (EJB default - 2) SingletonTimer: Hello World!


## Troubleshooting

Should the singleton be running on multiple nodes, the most common causes are accidentally starting with the `standalone.xml` or `standalone-full.xml` profile instead of with the `standalone-ha.xml` or `standalone-full-ha.xml` profile.
Make sure to start the server with an HA profile using `-c standalone-ha.xml`.

Another common cause is that the server instances did not discover each other and each server is operating as a singleton cluster. Ensure that multicast is enabled or change the `jgroups` subsystem configuration to use a different discovery mechanism.
Observe the following log line to ensure that the discovery was successful:

    INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (MSC service thread 1-3) ISPN000094: Received new cluster view for channel server: [node1|1] (2) [node1, node2]


## Making Existing Deployments Singleton

In this quickstart, the deployment is made singleton by a configuration file bundled in the archive.
Inspect the content in `src/main/resources/META-INF/singleton-deployment.xml`.
Any existing deployment can be made singleton by using *deployment overlays* mechanism.
To demonstrate how to use deployment overlays, follow these steps:

1. Move the `src/main/resources/META-INF/singleton-deployment.xml` file into root directory of this quickstart.
2. Rebuild the project. Ensure that the servers are started, and redeploy the application, which will no longer be configured by singleton deployment by the archive:

        mvn clean install
        mvn wildfly:deploy
        mvn wildfly:deploy -Dwildfly.port=10090

3. Start the management CLI and set up a deployment overlay on both servers:

        ${jboss.home.name}-1/bin/jboss-cli.sh --connect
        deployment-overlay add --name=singleton-deployment --deployments=ha-singleton-deployment.jar --content=META-INF/singleton-deployment.xml=singleton-deployment.xml
        deployment-overlay redeploy-affected --name=singleton-deployment

     Repeat this process for the second server using the port offset:

        ${jboss.home.name}-1/bin/jboss-cli.sh --connect --controller=localhost:10090
        deployment-overlay add --name=singleton-deployment --deployments=ha-singleton-deployment.jar --content=META-INF/singleton-deployment.xml=singleton-deployment.xml
        deployment-overlay redeploy-affected --name=singleton-deployment

4. Review the deployment overlay changes in the `standalone-ha.xml` server profile:

        <deployment-overlays>
            <deployment-overlay name="singleton-deployment">
                <content path="META-INF/singleton-deployment.xml" content="60a35e2bb6a1886f0a4abe499c7af16833d2a533"/>
                <deployment name="ha-singleton-deployment.jar"/>
            </deployment-overlay>
        </deployment-overlays>

5. Observe the server output. The deployments are now set up as singleton deployments.

6. To remove the deployment overlay run the following CLI command:

        deployment-overlay remove --name=singleton-deployment
        deployment-overlay redeploy-affected --name=singleton-deployment


For convenience, the management CLI scripts to add the deployment overlay, `singleton-deployment-overlay-add.cli`, and to remove the deployment overlay, `singleton-deployment-overlay-remove.cli`, are located in the root directory of this quickstart.


## Undeploy the Archives

1. Ensure all ${product.name} servers are started.
2. Navigate to the root directory of this quickstart in the command prompt.
3. Use the following commands to undeploy the artifacts:

        mvn wildfly:undeploy
        mvn wildfly:undeploy -Dwildfly.port=10090
