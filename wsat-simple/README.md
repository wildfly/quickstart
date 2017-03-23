# wsat-simple: WS-AT (WS-AtomicTransaction) - Simple

Author: Paul Robinson  
Level: Intermediate  
Technologies: WS-AT, JAX-WS  
Summary: The `wsat-simple` quickstart demonstrates a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service, bundled as a WAR, and deployed to ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `wsat-simple` quickstart demonstrates the deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive for deployment to ${product.name.full}.

The Web service is offered by a Restaurant for making bookings. The Service allows bookings to be made within an Atomic Transaction.

This example demonstrates the basics of implementing a WS-AT enabled Web service. It is beyond the scope of this quick start to demonstrate more advanced features. In particular:

* The Service does not implement the required hooks to support recovery in the presence of failures.
* It also does not utilize a transactional back-end resource.
* Only one web service participates in the protocol. As WS-AT is a 2PC coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the Narayana project: http://narayana.io/.

It is also assumed that you have an understanding of WS-AtomicTransaction. For more details, read the XTS documentation that ships with the Narayana project: http://narayana.io/docs/product.

The application consists of a single JAX-WS web service that is deployed within a WAR archive. It is tested with a JBoss Arquillian enabled JUnit test.

When running the `org.jboss.as.quickstarts.wsat.simple.ClientTest#testCommit()` method, the following steps occur:

1. A new Atomic Transaction (AT) is created by the client.
2. An operation on a WS-AT enabled Web service is invoked by the client.
3. The JaxWSHeaderContextProcessor in the WS Client handler chain inserts the WS-AT context into the outgoing SOAP message
4. When the service receives the SOAP request, the JaxWSHeaderContextProcessor in its handler chain inspects the WS-AT context and associates the request with this AT.
5. The Web service operation is invoked...
6. A participant is enlisted in this AT. This allows the Web Service logic to respond to protocol events, such as Commit and Rollback.
7. The service invokes the business logic. In this case, a booking is made with the restaurant.
8. The backend resource is prepared. This ensures that the Backend resource can undo or make permanent the change when told to do so by the coordinator.
10. The client can then decide to commit or rollback the AT. If the client decides to commit, the coordinator will begin the 2PC protocol. If the participant decides to rollback, all participants will be told to rollback.

There is another test that shows what happens if the client decides to rollback the AT.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server with the Custom Options

First, edit the log level to reduce the amount of log output. This should make it easier to read the logs produced by this example. To do this add the
following logger block to the ./docs/examples/configs/standalone-xts.xml of your JBoss distribution. You should add it just bellow one of the other logger blocks.

    <logger category="org.apache.cxf.service.factory.ReflectionServiceFactoryBean">
        <level name="WARN"/>
    </logger>

Next you need to start ${product.name} with the XTS subsystem enabled. This is enabled through the optional server configuration *standalone-xts.xml*. To do this, run the following commands from the top-level directory of ${product.name}:

    For Linux:     ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml
    For Windows:   \bin\standalone.bat --server-config=..\..\docs\examples\configs\standalone-xts.xml


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

The following messages should appear in the server log. The messages trace the steps taken by the tests. Note there may be other informational log messages interlaced between these.

Test rollback:

    10:54:29,607 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) Starting 'testRollback'. This test invokes a WS within an AT. The AT is later rolled back, which causes the back-end resource(s) to be rolled back.
    10:54:29,607 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] Creating a new WS-AT User Transaction
    10:54:29,608 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
    10:54:29,932 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] invoking makeBooking() on WS
    10:54:30,000 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Restaurant service invoked to make a booking
    10:54:30,000 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Enlisting a Durable2PC participant into the AT
    10:54:30,121 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] Invoking the back-end business logic
    10:54:30,122 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-25) [SERVICE] makeBooking called on backend resource.
    10:54:30,126 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-4) [CLIENT] rolling back Atomic Transaction (This will cause the AT and thus the enlisted back-end resources to rollback)
    10:54:30,349 INFO  [stdout] (TaskWorker-2) [SERVICE] one or more participants voted 'aborted' or a failure occurred, so coordinator tells the participant to rollback
    10:54:30,350 INFO  [stdout] (TaskWorker-2) [SERVICE] rollback called on backend resource.

Test commit:

    10:54:30,662 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) Starting 'testCommit'. This test invokes a WS within an AT. The AT is later committed, which causes the back-end resource(s) to be committed.
    10:54:30,663 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] Creating a new WS-AT User Transaction
    10:54:30,663 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] Beginning Atomic Transaction (All calls to Web services that support WS-AT wil be included in this transaction)
    10:54:30,797 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] invoking makeBooking() on WS
    10:54:30,848 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Restaurant service invoked to make a booking
    10:54:30,849 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Enlisting a Durable2PC participant into the AT
    10:54:30,936 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] Invoking the back-end business logic
    10:54:30,937 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-66) [SERVICE] makeBooking called on backend resource.
    10:54:30,942 INFO  [stdout] (http-localhost.localdomain/127.0.0.1:8080-54) [CLIENT] committing Atomic Transaction (This will cause the AT to complete successfully)
    10:54:31,046 INFO  [stdout] (TaskWorker-2) [SERVICE] Prepare called on participant, about to prepare the back-end resource
    10:54:31,046 INFO  [stdout] (TaskWorker-2) [SERVICE] prepare called on backend resource.
    10:54:31,047 INFO  [stdout] (TaskWorker-2) [SERVICE] back-end resource prepared, participant votes prepared
    10:54:31,067 WARN  [com.arjuna.wst] (TaskWorker-2) ARJUNA043219: Could not save recovery state for non-serializable durable WS-AT participant restaurantServiceAT:ba222c73-00c3-4ecc-921c-80fd5dfdc11a
    10:54:31,209 INFO  [stdout] (TaskWorker-2) [SERVICE] all participants voted 'prepared', so coordinator tells the participant to commit
    10:54:31,210 INFO  [stdout] (TaskWorker-2) [SERVICE] commit called on backend resource.

_Note: You can ignore the warning message `ARJUNA043219: Could not save recovery state for non-serializable durable WS-AT participant restaurantServiceAT` that is printed in the server console. This quickstart does not implement the required recovery hooks in the interest of making it easy to follow. In a real world production application, you should provide the required recovery code. For more information, see_ <http://narayana.io/docs/product>.


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


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
