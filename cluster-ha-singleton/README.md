# cluster-ha-singleton: A SingletonService Started by a SingletonStartup

Author: Wolf-Dieter Fink  
Level: Advanced  
Technologies: EJB, HASingleton, JNDI, Clustering, MSC
Summary: The `cluster-ha-singleton` quickstart deploys a Service, wrapped with the SingletonService decorator, and used as a cluster-wide singleton service.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `cluster-ha-singleton` quickstart demonstrates the deployment of a Service that is wrapped with the
SingletonService decorator and used as a cluster-wide singleton service in ${product.name.full}.
The service activates a scheduled timer, which is started only once in the cluster.

The example is composed of a Maven subproject and a parent project. The projects are as follows:

1. `service`: This subproject contains the Service and the EJB code to instantiate, start, and access the service.
2. The root parent `pom.xml` builds the `service` subproject and deploys the archive to the server.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Clone the Server Directory

While you can run this example starting only one instance of the server, if you want to see the singleton behavior, you must start at least two instances of the server. Make a copy of the ${product.name} directory structure to use for the second server.


## Start the Server with a HA profile

_Note: You must start the server using the HA profile or the singleton service will not start correctly._

Start the two ${product.name} servers with the HA profile by typing the following commands. You must pass a socket binding port offset on the command to start the second server.

If you are using Linux:

        Server 1: ${jboss.home.name}_1/bin/standalone.sh --server-config=standalone-ha.xml
        Server 2: ${jboss.home.name}_2/bin/standalone.sh --server-config=standalone-ha.xml  -Djboss.socket.binding.port-offset=100

If you are using Windows

        Server 1: ${jboss.home.name}_1\bin\standalone.bat --server-config=standalone-ha.xml
        Server 2: ${jboss.home.name}_2\bin\standalone.bat --server-config=standalone-ha.xml -Djboss.socket.binding.port-offset=100

_Note: If you want to test with more than two servers, you can start additional servers by specifying a unique port offset for each one._

## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} servers as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This deploys `service/target/${project.artifactId}-service.jar` to the running instance of the first server.
5. Since default socket binding port is `9990` and the second server runs at a port offset of `100`, you must pass port `10090` (9990 + 100) as an argument when you deploy to the second server. Type this command to deploy the archive to the second server.

        mvn wildfly:deploy -Dwildfly.port=10090

    If the second server is on a different host, you must also pass an argument for the host name as follows:

        mvn wildfly:deploy [-Dwildfly.hostname=OTHERHOST] -Dwildfly.port=10090
    _Note: If you test with more than two servers, repeat the command, replacing the unique port offset for each server._
6. This deploys `service/target/${project.artifactId}-service.jar` to the running instance of the additional server.

7. To verify the application deployed to each server instance, check the server logs. The first instance should have the following message:

        INFO  [org.wildfly.clustering.server] (remote-thread--p2-t1) WFLYCLSV0003: localhost elected as the singleton provider of the jboss.quickstart.ha.singleton.timer service

   The first server instance will also have messages like the following:

        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 4) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 5) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>

   The other servers will have the message:

        WFLYSRV0010: Deployed "${project.artifactId}-service.jar" (runtime-name : "${project.artifactId}-service.jar")

   NOTE: You will see the following warnings in both server logs when you deploy the application. You can ignore them.

        WARN  [org.jgroups.protocols.UDP] (MSC service thread 1-6) JGRP000015: the send buffer of socket ManagedDatagramSocketBinding was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
        WARN  [org.jgroups.protocols.UDP] (MSC service thread 1-6) JGRP000015: the receive buffer of socket ManagedDatagramSocketBinding was set to 20MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)
        WARN  [org.jgroups.protocols.UDP] (MSC service thread 1-6) JGRP000015: the send buffer of socket ManagedMulticastSocketBinding was set to 1MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max send buffer in the OS correctly (e.g. net.core.wmem_max on Linux)
        WARN  [org.jgroups.protocols.UDP] (MSC service thread 1-6) JGRP000015: the receive buffer of socket ManagedMulticastSocketBinding was set to 25MB, but the OS only allocated 212.99KB. This might lead to performance problems. Please set your max receive buffer in the OS correctly (e.g. net.core.rmem_max on Linux)

8. The timer started on the server instance will log a message every 10 seconds. If you stop the `${jboss.home.name}_1` server, you see messages in the `${jboss.home.name}_2` server console indicating it is now the singleton provider.

        WFLYCLSV0003: localhost elected as the singleton provider of the jboss.quickstart.ha.singleton.timer service
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.HATimerService] (MSC service thread 1-5) Start HASingleton timer service 'org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.HATimerService'
        INFO  [org.infinispan.remoting.transport.jgroups.JGroupsTransport] (Incoming-2,ee,localhost) ISPN000094: Received new cluster view for channel server: [localhost|2] (1) [localhost]
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 1) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 2) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>

9. In the example, the `${jboss.home.name}_1` instance used as master, if it is available. If it has failed or shutdown, any other service instance will be used.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type the following commands to undeploy the archives:

        mvn wildfly:undeploy
        mvn wildfly:undeploy [-Dwildfly.hostname=OTHERHOST] -Dwildfly.port=10090

_NOTE:_ You may see the following error in the server log when you undeploy the application. You can ignore this error. For performance reasons, the server does not wait for tasks to complete when an application is undeployed,

    ERROR [org.jboss.as.ejb3.invocation] (MSC service thread 1-7) WFLYEJB0034: EJB Invocation failed on component SchedulerBean for method public abstract void org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.Scheduler.stop(): org.jboss.as.ejb3.component.EJBComponentUnavailableException: WFLYEJB0421: Invocation cannot proceed as component is shutting down


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart is more complex than the others. It requires that you configure and run two instances of the ${product.name} server, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

_NOTE_: If you have not yet configured the ${product.name} ${product.version} runtime in JBoss Developer Studio, choose `Window`--> `Preferences` --> `Runtime Environment` and click `Add` to configure the following server instances.

1. Be sure to import the quickstart into JBoss Developer Studio.
2. Follow the instructions above to [Clone the Server Directory](#clone-the-server-directory).
3. Configure the first server instance in JBoss Developer Studio.
   * In the `Server` tab, right-click and choose `New` --> `Server`.
   * Under `Select the server type:`, expand `Red Hat JBoss Middleware` and choose `${jbds.eap.server.name}`.
   * For the `Server name`, enter `EAP7-Server1` and click `Next`.
   * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
   * In the `JBoss Runtime` dialog, enter the following information and then click `Next`.

            Name: EAP7-Server1
            Home Directory: (Browse to the directory for the first server and select it)
            Execution Environment: (Choose your JRE 8 runtime if not correct)
            Configuration base directory: (This should already point to your server configuration directory)
            Configuration file: (Browse and choose the `standalone-ha.xml` file)
   * In the `Add and Remove` dialog, add the `${project.artifactId}-service` to the `Configured` list and click `Finished`.
4. Configure the second server instance in JBoss Developer Studio.
   * In the `Server` tab, right-click and choose `New` --> `Server`.
   * Under `Select the server type:`, expand `Red Hat JBoss Middleware` and choose `${jbds.eap.server.name}`.
   * For the `Server name`, enter `EAP7-Server2` and click `Next`.
   * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
   * In the `JBoss Runtime` dialog, enter the following information and then click `Next`.

            Name: EAP7-Server2
            Home Directory: (Browse to the cloned directory for the second server and select it)
            Execution Environment: (Choose your JRE 8 runtime if not correct)
            Configuration base directory: (This should already point to your cloned server configuration directory)
            Configuration file: (Browse and choose the `standalone-ha.xml` file)
   * In the `Add and Remove` dialog, add the `${project.artifactId}-service` to the `Configured` list and click `Finished`.
   * In the `Server` tab, double-click on `EAP7-Server2` to open the `Overview` page.
   * Click `Open launch configuration` and at the end of the `VM Arguments`, paste `-Djboss.socket.binding.port-offset=100` and click `OK`.
   * Still in the `Overview` page for `EAP7-Server2`, under `Server Ports`, uncheck the `Detect from Local Runtime` next to `Port Offset` and enter `100`. Save the changes using the menu `File --> Save`

5. To deploy the cluster-ha-singleton service to `EAP7-Server1`, right-click on the `${project.artifactId}-service` project, choose `Run As` --> `Run on Server`, choose `EAP7-Server1` and click `Finish`. Note the messages in the `EAP7-Server1` server console indicate it is the singleton provider of the service.

        WFLYCLSV0003: localhost elected as the singleton provider of the jboss.quickstart.ha.singleton.timer service
        WFLYSRV0060: Http management interface listening on http://127.0.0.1:9990/management
        WFLYSRV0051: Admin console listening on http://127.0.0.1:9990
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 1) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 2) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 3) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>

6. To deploy the cluster-ha-singleton service to `EAP7-Server2`, right-click on the `${project.artifactId}-service` project, choose `Run As` --> `Run on Server`, choose `EAP7-Server2` and click `Finish`. Note that `EAP7-Server1` is still the singleton provider of the service. This message is in the `EAP7-Server2` console.

        WFLYSRV0060: Http management interface listening on http://127.0.0.1:10090/management   

7. Stop the `EAP7-Server1` server and note the following message in the `EAP7-Server2` server console indicating it is now the singleton provider.

        WFLYCLSV0003: localhost elected as the singleton provider of the jboss.quickstart.ha.singleton.timer service
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 1) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 2) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>
        INFO  [class org.jboss.as.quickstarts.cluster.hasingleton.service.ejb.SchedulerBean] (EJB default - 3) HASingletonTimer: Info=HASingleton timer @localhost <timestamp>


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
