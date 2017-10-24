# wsba-participant-completion-simple: Deployment of a WS-BA enabled JAX-WS Web Service

Author: Paul Robinson  
Level: Intermediate  
Technologies: WS-BA, JAX-WS  
Summary: The `wsba-participant-completion-simple` quickstart deploys a WS-BA (WS Business Activity) enabled JAX-WS Web service WAR (ParticipantCompletion Protocol).  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `wsba-participant-completion-simple` quickstart demonstrates the deployment of a WS-BA (WS Business Activity) enabled JAX-WS Web service bundled in a WAR archive (ParticipantCompletion Protocol) for deployment to ${product.name.full}.

The Web service exposes a simple 'set' collection as a service. The Service allows items to be added to the set within a Business Activity.

The example demonstrates the basics of implementing a WS-BA enabled Web service. It is beyond the scope of this quickstart to demonstrate more advanced features. In particular

* The Service does not implement the required hooks to support recovery in the presence of failures.
* It also does not utilize a transactional back-end resource.
* Only one web service participates in the protocol. As WS-BA is a coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the Narayana project: http://narayana.io.

It is also assumed tht you have an understanding of WS-BusinessActivity. For more details, read the XTS documentation
that ships with the Narayana project: http://narayana.io/docs/product

The application consists of a single JAX-WS web service that is deployed within a WAR archive. It is tested with a JBoss
Arquillian enabled JUnit test.

When running the org.jboss.as.quickstarts.wsba.participantcompletion.simple.ClientTest#testSuccess() method, the
following steps occur:

1. A new Business Activity is created by the client.
2. An operation on a WS-BA enabled Web service is invoked by the client.
3. The `JaxWSHeaderContextProcessor` in the WS Client handler chain inserts the BA context into the outgoing SOAP message.
4. When the service receives the SOAP request, the `JaxWSHeaderContextProcessor` in its handler chain inspects the BA context and associates the request with this BA.
5. The Web service operation is invoked.
6. A participant is enlisted in this BA. This allows the Web Service logic to respond to protocol events, such as compensate and close.
7. The service invokes the business logic. In this case, a String value is added to the set.
8. The backend resource is prepared. This ensures that the Backend resource can undo or make permanent the change when told to do so by the coordinator.
9. Providing the above steps where successful, the service notifies the coordinator that it has completed. The service has now made its changes visible and is not holding any locks. Allowing the service to notify completion is an optimisation that prevents the holding of locks, whilst waiting for other participants to complete. This notification is required as the Service participates in the `ParticipantCompletion` protocol.
10. The client can then decide to complete or cancel the BA. If the client decides to complete, all participants will be told to close. If the participant decides to cancel, all participants will be told to compensate.

There are additional tests that show:

* What happens when an application exception is thrown by the service.
* How the client can cancel a BA.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

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

    INFO  [stdout] (management-handler-threads - 6) Starting 'testSuccess'. This test invokes a WS within a BA. The BA is later closed, which causes the WS call to complete successfully.
    INFO  [stdout] (management-handler-threads - 6) [CLIENT] Creating a new Business Activity
    INFO  [stdout] (management-handler-threads - 6) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA wil be included in this activity)
    INFO  [stdout] (management-handler-threads - 6) [CLIENT] invoking addValueToSet(1) on WS
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] invoked addValueToSet('1')
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a participant into the BA
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare successful, notifying coordinator of completion
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Participant.confirmCompleted('true') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Commit the backend resource (e.g. commit any changes to databases so that they are visible to others)
    INFO  [stdout] (management-handler-threads - 6) [CLIENT] Closing Business Activity (This will cause the BA to complete successfully)
    INFO  [stdout] (TaskWorker-2) [SERVICE] Participant.close (The participant knows that this BA is now finished and can throw away any temporary state)

Test cancel:

    INFO  [stdout] (management-handler-threads - 5) Starting 'testCancel'. This test invokes a WS within a BA. The BA is later cancelled, which causes these WS call to be compensated.
    INFO  [stdout] (management-handler-threads - 5) [CLIENT] Creating a new Business Activity
    INFO  [stdout] (management-handler-threads - 5) [CLIENT] Beginning Business Activity (All calls to Web services that support WS-BA will be included in this activity)
    INFO  [stdout] (management-handler-threads - 5) [CLIENT] invoking addValueToSet(1) on WS
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] invoked addValueToSet('1')
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Enlisting a participant into the BA
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Invoking the back-end business logic
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Prepare successful, notifying coordinator of completion
    INFO  [stdout] (http-localhost-127.0.0.1-8080-1) [SERVICE] Participant.confirmCompleted('true') (This tells the participant that compensation information has been logged and that it is safe to commit any changes.)



## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart is more complex than the others. It requires that you configure the ${product.name} server to use the *standalone-xts.xml* configuration file, which is located in an external configuration directory.

1. Import the quickstart into JBoss Developer Studio.
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
