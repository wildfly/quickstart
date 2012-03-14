helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================
Author: Serge Pagop

What is it?
-----------

This example demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in JBoss AS 7.1.0.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or JBoss Enterprise Application Platform 6. 
 
NOTE:
This Project will use the already default configured connection factory named "InVmConnectionFactory" with the jndi "java:/ConnectionFactory" and a queue named "testQueue" with the jndi "queue/test".
The artifacts will come from the JBoss Community Maven repository, a superset of the Maven central repository.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

For JBoss AS 7 or JBoss Enterprise Application Platform 6:

    On Linux run: $JBOSS_HOME/bin/standalone.sh -c standalone-full.xml

    On Windows run: $JBOSS_HOME/bin/standalone.bat -c standalone-full.xml

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-helloworld-mdb.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-helloworld-mdb/HelloWorldMDBServletClient>.

Go to the JBoss Application Server console or Server log and the result can look like this:

    15:42:35,453 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-47 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 1
    15:42:35,455 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-46 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 2
    15:42:35,457 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-50 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 3
    15:42:35,478 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-53 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 5
    15:42:35,481 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-52 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 4


To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.
