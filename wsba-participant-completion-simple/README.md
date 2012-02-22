wsba-participant-completion-simple: Deployment of a WS-BA (WS Business Activity) - Participant Completion
=========================================================================================================
Author: Paul Robinson

What is it?
-----------

This example demonstrates the deployment of a WS-BA (WS Business Activity) enabled JAX-WS Web service bundled in a war
archive for deployment to *JBoss AS 7*.

The Web service exposes a simple 'set' collection as a service. The Service allows items to be added to the set within a
Business Activity.

The example demonstrates the basics of implementing a WS-BA enabled Web service. It is beyond the scope of this
quick start to demonstrate more advanced features. In particular

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-BA is a coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the JBossTS project: http://www.jboss.org/jbosstm.

It is also assumed tht you have an understanding of WS-BusinessActivity. For more details, read the XTS documentation
that ships with the JBossTS project, which can be downloaded here: http://www.jboss.org/jbosstm/downloads/JBOSSTS_4_16_0_Final

The application consists of a single JAX-WS web service that is deployed within a war archive. It is tested with a JBoss
Arquillian enabled JUnit test.

When running the org.jboss.as.quickstarts.wsba.participantcompletion.simple.ClientTest#testSuccess() method, the
following steps occur:

1. A new Business Activity is created by the client.
2. An operation on a WS-BA enabled Web service is invoked by the client.
3. The JaxWSHeaderContextProcessor in the WS Client handler chain inserts the BA context into the outgoing SOAP message
4. When the service receives the SOAP request, it's JaxWSHeaderContextProcessor in it's handler chain inspects the BA context and associates the request with this BA.
5. The Web service operation is invoked...
6. A participant is enlisted in this BA. This allows the Web Service logic to respond to protocol events, such as compensate and close.
7. The service invokes the business logic. In this case, a String value is added to the set.
8. The backend resource is prepared. This ensures that the Backend resource can undo or make permanent the change when told to do so by the coordinator.
9. Providing the above steps where successful, the service notifies the coordinator that it has completed. The service has now made it's changes visible and is not holding any locks. Allowing the service to notify completion is an optimisation that prevents the holding of locks, whilst waiting for other participants to complete. This notification is required as the Service participates in the ParticipantCompletion protocol.
10. The client can then decide to complete or cancel the BA. If the client decides to complete, all participants will be told to close. If the participant decides to cancel, all participants will be told to compensate.

There are other tests that show:

* What happens when an application exception is thrown by the service.
* How the client can cancel a BA.


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

Test success:

    11:41:02,386 INFO  [stdout] (management-handler-threads - 6) Starting 'testSuccess'. This test invokes a WS within a BA. The BA is later closed, which causes the WS call to complete successfully.
    11:41:02,386 INFO  [stdout] (management-handler-threads - 6) [CLIENT] Creating a new Business Activity
    11:41:02,386 INFO  [stdout] (management-handler-threads - 6) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA wil be included in this activity)
    11:41:02,927 INFO  [stdout] (management-handler-threads - 6) [CLIENT] invoking addValueToSet(1) on WS
    11:41:03,233 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] invoked addValueToSet('1')
    11:41:03,233 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a participant into the BA
    11:41:03,336 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    11:41:03,336 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work
    11:41:03,337 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare successful, notifying coordinator of completion
    11:41:03,660 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) SetParticipantBA.confirmCompleted('true')
    11:41:03,660 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Participant.confirmCompleted('true') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)
    11:41:03,660 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Commit the backend resource (e.g. commit any changes to databases so that they are visible to others)
    11:41:03,779 INFO  [stdout] (management-handler-threads - 6) [CLIENT] Closing Business Activity (This will cause the BA to complete successfully)
    11:41:04,330 INFO  [stdout] (TaskWorker-2) [SERVICE] Participant.close (The participant knows that this BA is now finished and can throw away any temporary state)

Test cancel:

    11:41:04,721 INFO  [stdout] (management-handler-threads - 5) Starting 'testCancel'. This test invokes a WS within a BA. The BA is later cancelled, which causes these WS call to be compensated.
    11:41:04,721 INFO  [stdout] (management-handler-threads - 5) [CLIENT] Creating a new Business Activity
    11:41:04,721 INFO  [stdout] (management-handler-threads - 5) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA will be included in this activity)
    11:41:04,781 INFO  [stdout] (management-handler-threads - 5) [CLIENT] invoking addValueToSet(1) on WS
    11:41:05,133 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] invoked addValueToSet('1')
    11:41:05,134 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a participant into the BA
    11:41:05,241 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    11:41:05,242 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work
    11:41:05,242 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare successful, notifying coordinator of completion
    11:41:05,305 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) SetParticipantBA.confirmCompleted('true')
    11:41:05,305 INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Participant.confirmCompleted('true') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)


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
