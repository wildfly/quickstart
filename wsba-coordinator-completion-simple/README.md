# wsba-coordinator-completion-simple: Example of a WS-BA Enabled JAX-WS Web Service

Author: Paul Robinson  
Level: Intermediate  
Technologies: WS-BA, JAX-WS  
Summary:  The `wsba-coordinator-completion-simple` quickstart deploys a WS-BA (WS Business Activity) enabled JAX-WS Web service WAR (CoordinatorCompletion protocol).  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `wsba-coordinator-completion-simple` quickstart demonstrates the deployment of a WS-BA (WS Business Activity) enabled JAX-WS Web service bundled in a WAR archive (Participant Completion protocol) for deployment to ${product.name.full}.

The Web service exposes a simple `set` collection as a service. The Service allows items to be added to the set within a Business Activity.

This example demonstrates the basics of implementing a WS-BA enabled Web service. It is beyond the scope of this quick start to demonstrate more advanced features. In particular:

* The Service does not implement the required hooks to support recovery in the presence of failures.
* It also does not utilize a transactional back-end resource.
* Only one web service participates in the protocol. As WS-BA is a coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the Narayana project: http://narayana.io.

It is also assumed that you have an understanding of WS-BusinessActivity. For more details, read the XTS documentation that ships with the Narayana project: <http://narayana.io/docs/product>

The application consists of a single JAX-WS web service that is deployed within a WAR archive. It is tested with a JBoss Arquillian enabled JUnit test.

When running the `org.jboss.as.quickstarts.wsba.coordinatorcompletion.simple.ClientTest#testSuccess()` method, the following steps occur:

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


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server with the Custom Options

Next you need to start ${product.name} with the XTS subsystem enabled. This is enabled through the optional server configuration *standalone-xts.xml*. To do this, run the following commands from the top-level directory of ${product.name}:

    For Linux:     ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml | egrep "started|stdout"
    For Windows:   \bin\standalone.bat --server-config=..\..\docs\examples\configs\standalone-xts.xml | egrep "started|stdout"


Note, the pipe to egrep (| egrep "started|stdout") is useful to just show when the server has started and the output from these tests. For normal operation, this pipe can be removed.


## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

4. You should see the following result.

        Results :

        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

_Note: You see the following warning when you run the Arquillian tests in remote mode._

    WARNING: Configuration contain properties not supported by the backing object org.jboss.as.arquillian.container.remote.RemoteContainerConfiguration
    Unused property entries: {serverConfig=../../docs/examples/configs/standalone-xts.xml}
    Supported property names: [managementAddress, password, managementPort, managementProtocol, username]

_This is because, in remote mode, you are responsible for starting the server with the XTS subsystem enabled. When you run the Arquillian tests in managed mode, the container uses the `serverConfig` property defined in the `arquillian.xml` file to start the server with the XTS subsystem enabled._

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Investigate the Server Log

The following messages should appear in the server log. Note there may be other log messages interlaced between these. The messages trace the steps taken by the tests.

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


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart is more complex than the others. It requires that you configure the ${product.name} server to use the *standalone-xts.xml* configuration file, which is located in an external configuration directory.

1. Be sure to import the quickstart into JBoss Developer Studio.
2. If you have not already done so, you must configure a new ${product.name} server to use the XTS configuration.
     * In the `Server` tab, right-click and choose `New` --> `Server`.
     * Under `Select the server type:`, expand `Red Hat JBoss Middleware` and choose `${jbds.eap.server.name}`.
     * For the `Server name`, enter `${product.name} XTS Configuration` and click `Next`.
     * In the `Create a new Server Adapter` dialog, choose `Create a new runtime (next page)` and click `Next`.
     * In the `JBoss Runtime` dialog, enter the following information and then click `Finish`.

            Name: ${product.name} XTS Runtime
            Home Directory: (Browse to the server directory and select it)
            Execution Environment: (Choose your runtime JRE if not correct)
            Configuration base directory: (This should already point to your server configuration directory)
            Configuration file: ../../docs/examples/configs/standalone-xts.xml

3. Start the new `${product.name} XTS Configuration` server.
4. Right-click on the `${project.artifactId}` project, choose `Run As` --> `Maven build`, enter `clean verify -Parq-remote` for the `Goals:`, and click `Run` to run the Arquillian tests. The test results appear in the console.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
