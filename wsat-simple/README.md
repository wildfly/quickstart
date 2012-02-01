wsat-simple: WS-AT (WS-AtomicTransaction) - Simple
==================================================
Author: Paul Robinson

What is it?
-----------

This example demonstrates the deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a war
archive for deployment to *JBoss AS 7*.

The Web service is offered by a Restaurant for making bookings. The Service allows bookings to be made within an
Atomic Transaction.

The example demonstrates the basics of implementing a WS-AT enabled Web service. It is beyond the scope of this
quick start to demonstrate more advanced features. In particular:

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-AT is a 2PC coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the JBossTS project: http://www.jboss.org/jbosstm.

It is also assumed tht you have an understanding of WS-AtomicTransaction. For more details, read the XTS documentation
that ships with the JBossTS project, which can be downloaded here: http://www.jboss.org/jbosstm/downloads/JBOSSTS_4_16_0_Final

The application consists of a single JAX-WS web service that is deployed within a war archive. It is tested with a JBoss
Arquillian enabled JUnit test.

When running the org.jboss.as.quickstarts.wsat.simple.ClientTest#testCommit() method, the
following steps occur:

1. A new Atomic Transaction (AT) is created by the client.
2. An operation on a WS-AT enabled Web service is invoked by the client.
3. The JaxWSHeaderContextProcessor in the WS Client handler chain inserts the WS-AT context into the outgoing SOAP message
4. When the service receives the SOAP request, it's JaxWSHeaderContextProcessor in it's handler chain inspects the WS-AT context and associates the request with this AT.
5. The Web service operation is invoked...
6. A participant is enlisted in this AT. This allows the Web Service logic to respond to protocol events, such as Commit and Rollback.
7. The service invokes the business logic. In this case, a booking is made with the restaurant.
8. The backend resource is prepared. This ensures that the Backend resource can undo or make permanent the change when told to do so by the coordinator.
10. The client can then decide to commit or rollback the AT. If the client decides to commit, the coordinator will begin the 2PC protocol. If the participant decides to rollback, all participants will be told to rollback.

There is another test that shows what happens if the client decides to rollback the AT.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First you need to start JBoss AS 7 (7.1.0.CR1 or above, or EAP 6), with the XTS sub system enabled, this is enabled through an optional server configuration (standalone-xts.xml). To do this, run the following commands, from within the top-level directory of JBossAS:

    ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml | egrep "started|stdout"

Note, the pipe to egrep (| egrep "started|stdout") is useful to just show when the server has started and the output from these tests. For normal operation, this pipe can be removed.

or if you are using windows

    ./bin/standalone.bat --server-config=../../docs/examples/configs/standalone-xts.xml

To test the application run:

    mvn clean test -Parq-jbossas-remote

The following expected output should appear. The output explains what actually went on when these tests ran.

Test commit:

    14:06:28,208 INFO  [stdout] (management-handler-threads - 14) Starting 'testCommit'. This test invokes a WS within an AT. The AT is later committed, which causes the back-end resource(s) to be committed.
    14:06:28,209 INFO  [stdout] (management-handler-threads - 14) [CLIENT] Creating a new WS-AT User Transaction
    14:06:28,209 INFO  [stdout] (management-handler-threads - 14) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
    14:06:28,532 INFO  [stdout] (management-handler-threads - 14) [CLIENT] invoking makeBooking() on WS
    14:06:29,168 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Restaurant service invoked to make a booking
    14:06:29,168 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a Durable2PC participant into the AT
    14:06:29,410 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    14:06:29,410 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] makeBooking called on backend resource.
    14:06:29,411 INFO  [stdout] (management-handler-threads - 14) [CLIENT] committing Atomic Transaction (This will cause the AT to complete successfully)
    14:06:29,974 INFO  [stdout] (TaskWorker-3) [SERVICE] Prepare called on participant, about to prepare the back-end resource
    14:06:29,974 INFO  [stdout] (TaskWorker-3) [SERVICE] prepare called on backend resource.
    14:06:29,974 INFO  [stdout] (TaskWorker-3) [SERVICE] back-end resource prepared, participant votes prepared
    14:06:30,560 INFO  [stdout] (TaskWorker-3) [SERVICE] all participants voted 'prepared', so coordinator tells the participant to commit
    14:06:30,560 INFO  [stdout] (TaskWorker-3) [SERVICE] commit called on backend resource.

Test rollback:

    14:06:31,163 INFO  [stdout] (management-handler-threads - 13) Starting 'testRollback'. This test invokes a WS within an AT. The AT is later rolled back, which causes the back-end resource(s) to be rolled back.
    14:06:31,163 INFO  [stdout] (management-handler-threads - 13) [CLIENT] Creating a new WS-AT User Transaction
    14:06:31,164 INFO  [stdout] (management-handler-threads - 13) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
    14:06:31,461 INFO  [stdout] (management-handler-threads - 13) [CLIENT] invoking makeBooking() on WS
    14:06:32,094 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Restaurant service invoked to make a booking
    14:06:32,094 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a Durable2PC participant into the AT
    14:06:32,297 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    14:06:32,322 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] makeBooking called on backend resource.
    14:06:32,324 INFO  [stdout] (management-handler-threads - 13) [CLIENT] rolling back Atomic Transaction (This will cause the AT and thus the enlisted back-end resources to rollback)
    14:06:32,818 INFO  [stdout] (TaskWorker-1) [SERVICE] one or more participants voted 'aborted' or a failure occurred, so coordinator tells the participant to rollback
    14:06:32,818 INFO  [stdout] (TaskWorker-1) [SERVICE] rollback called on backend resource.

You can also start JBoss AS 7 and run the tests within Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the
JBoss AS 7 Getting Started Guide for Developers.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
