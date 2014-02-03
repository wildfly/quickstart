---
Author: Serge Pagop, Andy Taylor, Jeff Mesnil
Level: Intermediate
Technologies: JMS, EJB, MDB
Summary: Demonstrates the use of JMS and EJB Message-Driven Bean
Target Product: WildFly
Source: https://github.com/wildfly/quickstart/
---

helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================

What is it?
-----------

This example demonstrates the use of *JMS 2.0* and *EJB 3.2 Message-Driven Bean* in WildFly 8.

This project creates two JMS resources:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/topic/HELLOWORLDMDBTopic`


System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.0 or better.

The application this project produces is designed to be run on WildFly 8.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


Start WildFly 8 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-helloworld-mdb.war` to the running instance of the server. Look at the JBoss Application Server console or Server log and you should see log messages corresponding to the deployment of the message-driven beans and the JMS destinations:

        14:11:01,020 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.queue.HELLOWORLDMDBQueue
        14:11:01,029 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/queue/HELLOWORLDMDBQueue
        14:11:01,030 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.topic.HELLOWORLDMDBTopic
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQueueMDB' with 'hornetq-ra' resource adapter
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQTopicMDB' with 'hornetq-ra' resource adapter
        14:11:01,070 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/topic/HELLOWORLDMDBTopic

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-helloworld-mdb/> and will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/jboss-as-helloworld-mdb/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the JBoss Application Server console or Server log and you should see log messages like the following:

    17:51:52,122 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-1 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 1
    17:51:52,123 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-11 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 2
    17:51:52,124 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-12 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 5
    17:51:52,135 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-13 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 4
    17:51:52,136 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-14 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 3


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
