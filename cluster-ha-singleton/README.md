cluster-ha-singleton: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB
=============================================================================================================
Author: Wolf-Dieter Fink
Level: Advanced
Technologies: EJB, HASingleton, JNDI
Summary: A SingletonService deployed in a JAR started by SingletonStartup and accessed by an EJB
Target Product: EAP

What is it?
-----------

This example demonstrates the deployment of a Service that is wrapped with the SingletonService decorater
to be a cluster wide singleton service.

The example is composed of 2 maven projects with a shared parent. The projects are as follows:

1. `service`: This project contains the Service and the EJB code to instantiate, start and access the service
2. `client` : This project contains a remote ejb client to show the behaviour

The root `pom.xml` builds each of the subprojects in the above order and deploys the archive to the server.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

You can copy or link to the Maven configuration information in the README file in the root folder of the quickstarts. For example:

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with a HA profile
-------------------------

If you run a non HA profile the singleton service will not start correctly. To run the example one instance must be started, to see the singleton behaviour at minimum two instances
should be started.

    Start server one : standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeOne
    Start server two : standalone.sh --server-config=standalone-ha.xml -Djboss.node.name=nodeTwo -Djboss.socket.binding.port-offset=100


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `service/target/jboss-as-cluster-ha-singleton-service.jar` to the running instance of the server.
5. Type this command to deploy the archive to the second server (or more) and replace hostname and port depend on your settings:

        mvn jboss-as:deploy -Ddeploy.hostname=localhost -Ddeploy.port=10099

6. This will deploy `service/target/jboss-as-cluster-ha-singleton-service.jar` to the running instance of the additional server.
 
Check whether the application is deployed on each instance.
All instances will have a message:
INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010342: nodeOne/cluster elected as the singleton provider of the jboss.quickstart.ejb.ha.singleton service
Only nodeOne (or even one instance) will have a message:
INFO  [org.jboss.as.clustering.singleton] (SingletonService lifecycle - 1) JBAS010340: This node will now operate as the singleton provider of the jboss.quickstart.ejb.ha.singleton service

The timer on the started node will log a message every 10sec.

Check the timer
---------------------

1. Open a command line and navigate to the root directory of this quickstart.
2. Type this command to start the client

        cd client
        mvn exec:exec

3. check the output
		The request to the EJB is running four times and every time it should answer
		  # The timer service is active on node with name = NodeOne
		If you look into the servers logfiles you will see that each node will process requests
4. Stop the server nodeOne
		If you look into the server nodeTwo logfile you will see the message
		"JBAS010342: nodeTwo/cluster elected as the singleton provider of ..."
		that shows that the singleton service was started here
		The timer will be started here and log a message every 10sec.
5. repeat step 2
    The request is running four times and the message is 
		  # The timer service is active on node with name = NodeTwo
		If you look into the server#2 logfile you will see that it process all four requests.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
        mvn jboss-as:undeploy -Ddeploy.hostname=localhost -Ddeploy.port=10099


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

------------------------------------
