wsba-coordinator-completion-simple: Example of a WS-BA (WS Business Activity) Enabled JAX-WS Web Service
========================================================================================================
Author: Paul Robinson
Level: Intermediate
Technologies: WS-BA, JAX-WS
Summary:  Deployment of a WS-BA (WS-BusinessActivity) enabled JAX-WS Web service bundled in a WAR archive (Participant Completion protocol)
Target Product: EAP

What is it?
-----------

This example demonstrates the deployment of a WS-BA (WS Business Activity) enabled JAX-WS Web service bundled in a WAR archive for deployment to *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

The Web service exposes a simple 'set' collection as a service. The Service allows items to be added to the set within a Business Activity.

This example demonstrates the basics of implementing a WS-BA enabled Web service. It is beyond the scope of this quick start to demonstrate more advanced features. In particular:

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-BA is a coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the JBossTS project: http://www.jboss.org/jbosstm.

It is also assumed that you have an understanding of WS-BusinessActivity. For more details, read the XTS documentation that ships with the JBossTS project, which can be downloaded here: <http://www.jboss.org/jbosstm/downloads/JBOSSTS_4_16_0_Final>

The application consists of a single JAX-WS web service that is deployed within a WAR archive. It is tested with a JBoss Arquillian enabled JUnit test.

When running the org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple.ClientTest#testSuccess() method, the following steps occur:

1. A new Business Activity is created by the client.
2. Multiple operations on a WS-BA enabled Web service is invoked by the client.
3. The `JaxWSHeaderContextProcessor` in the WS Client handler chain inserts the BA context into the outgoing SOAP messages.
4. When the service receives a SOAP request, the `JaxWSHeaderContextProcessor` in its handler chain inspects the BA context and associates the request with this BA.
5. The Web service operation is invoked.
6. For the first request, in this BA, A participant is enlisted in this BA. This allows the Web Service logic to respond to protocol events, such as compensate and close.
7. The service invokes the business logic. In this case, a String value is added to the set.
9. The client can then make additional calls to the `SetService`. As the `SetService` participates as a `CoordinatorCompletion` protocol, it will continue to accept calls to `addValueToSet` until it is told to complete by the coordinator.
10. The client can then decide to complete or cancel the BA. 
    * If the client decides to complete, all participants will be told to complete. Providing all participants successfully complete, the coordinator will then tell all participants to close, otherwise the completed participants will be told to compensate.  
    * If the participant decides to cancel, all participants will be told to compensate.

There is another test that shows how the client can cancel a BA.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Custom Options
----------------------

Next you need to start JBoss Enterprise Application Platform 6 or JBoss AS 7 (7.1.0.CR1 or above), with the XTS sub system enabled. This is enabled through the optional server configuration *standalone-xts.xml*. To do this, run the following commands from the top-level directory of JBossAS:

        For Linux:     ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml | egrep "started|stdout"
        For Windows:   \bin\standalone.bat --server-config=..\..\docs\examples\configs\standalone-xts.xml | egrep "started|stdout"


Note, the pipe to egrep (| egrep "started|stdout") is useful to just show when the server has started and the output from these tests. For normal operation, this pipe can be removed.


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Investigate the Console Output
----------------------------

The following expected output should appear. The output explains what actually went on when these tests ran.

Test success:

    16:24:19,191 INFO  [stdout] (management-handler-threads - 10) Starting 'testSuccess'. This test invokes a WS twice within a BA. The BA is later closes, which causes these WS calls to complete successfully.
    16:24:19,191 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Creating a new Business Activity
    16:24:19,192 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA wil be included in this activity)
    16:24:19,216 INFO  [stdout] (management-handler-threads - 10) [CLIENT] invoking addValueToSet(1) on WS
    16:24:19,241 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] invoked addValueToSet('1')
    16:24:19,241 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Enlisting a participant into the BA
    16:24:19,281 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Invoking the back-end business logic
    16:24:19,283 INFO  [stdout] (management-handler-threads - 10) [CLIENT] invoking addValueToSet(2) on WS
    16:24:19,308 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] invoked addValueToSet('2')
    16:24:19,308 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Re-using the existing participant, already registered for this BA
    16:24:19,308 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Invoking the back-end business logic
    16:24:19,313 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Closing Business Activity (This will cause the BA to complete successfully)
    16:24:19,419 INFO  [stdout] (TaskWorker-3) [SERVICE] Participant.complete (This tells the participant that the BA completed, but may be compensated later)
    16:24:19,498 INFO  [stdout] (TaskWorker-3) [SERVICE] Participant.confirmCompleted('true') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)
    16:24:19,498 INFO  [stdout] (TaskWorker-3) [SERVICE] Commit the backend resource (e.g. commit any changes to databases so that they are visible to others)
    16:24:19,543 INFO  [stdout] (TaskWorker-1) [SERVICE] Participant.close (The participant knows that this BA is now finished and can throw away any temporary state)

Test cancel:

    16:24:19,616 INFO  [stdout] (management-handler-threads - 10) Starting 'testCancel'. This test invokes a WS twice within a BA. The BA is later cancelled, which causes these WS calls to be compensated.
    16:24:19,616 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Creating a new Business Activity
    16:24:19,616 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA will be included in this activity)
    16:24:19,631 INFO  [stdout] (management-handler-threads - 10) [CLIENT] invoking addValueToSet(1) on WS
    16:24:19,653 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] invoked addValueToSet('1')
    16:24:19,653 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Enlisting a participant into the BA
    16:24:19,685 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Invoking the back-end business logic
    16:24:19,688 INFO  [stdout] (management-handler-threads - 10) [CLIENT] invoking addValueToSet(2) on WS
    16:24:19,713 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] invoked addValueToSet('2')
    16:24:19,713 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Re-using the existing participant, already registered for this BA
    16:24:19,713 INFO  [stdout] (http-localhost-127.0.0.1-8080-2) [SERVICE] Invoking the back-end business logic
    16:24:19,756 INFO  [stdout] (management-handler-threads - 10) [CLIENT] Cancelling Business Activity (This will cause the work to be compensated)
    16:24:19,815 INFO  [stdout] (TaskWorker-3) [SERVICE] Participant.cancel (The participant should compensate any work done within this BA)
    16:24:19,815 INFO  [stdout] (TaskWorker-3) [SERVICE] SetParticipantBA: Carrying out compensation action
    16:24:19,815 INFO  [stdout] (TaskWorker-3) [SERVICE] Compensate the backend resource by removing '1' from the set (e.g. undo any changes to databases that were previously made visible to others)
    16:24:19,816 INFO  [stdout] (TaskWorker-3) [SERVICE] Compensate the backend resource by removing '2' from the set (e.g. undo any changes to databases that were previously made visible to others)


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
