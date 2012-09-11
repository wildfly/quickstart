wsat-simple: WS-AT (WS-AtomicTransaction) - Simple
==================================================
Author: Paul Robinson
Level: Intermediate
Technologies: WS-AT, JAX-WS
Summary: Deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive
Target Product: EAP

What is it?
-----------

This example demonstrates the deployment of a WS-AT (WS-AtomicTransaction) enabled JAX-WS Web service bundled in a WAR archive for deployment to  *JBoss Enterprise Application Platform 6* or *JBoss AS 7*..

The Web service is offered by a Restaurant for making bookings. The Service allows bookings to be made within an Atomic Transaction.

This example demonstrates the basics of implementing a WS-AT enabled Web service. It is beyond the scope of this quick start to demonstrate more advanced features. In particular:

1. The Service does not implement the required hooks to support recovery in the presence of failures.
2. It also does not utilize a transactional back end resource.
3. Only one Web service participates in the protocol. As WS-AT is a 2PC coordination protocol, it is best suited to multi-participant scenarios.

For a more complete example, please see the XTS demonstrator application that ships with the JBossTS project: http://www.jboss.org/jbosstm.

It is also assumed that you have an understanding of WS-AtomicTransaction. For more details, read the XTS documentation that ships with the JBossTS project, which can be downloaded here: http://www.jboss.org/jbosstm/downloads/JBOSSTS_4_16_0_Final

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


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Custom Options
----------------------

First, edit the log level to reduce the amount of log output. This should make it easier to read the logs produced by this example. To do this add the
following logger block to the ./docs/examples/configs/standalone-xts.xml of your JBoss distribution. You should add it just bellow one of the other logger blocks.

        <logger category="org.apache.cxf.service.factory.ReflectionServiceFactoryBean">
            <level name="WARN"/>
        </logger>         

Next you need to start JBoss Enterprise Application Platform 6 or JBoss AS 7 (7.1.0.CR1 or above), with the XTS sub system enabled. This is enabled through the optional server configuration *standalone-xts.xml*. To do this, run the following commands from the top-level directory of JBossAS:

        For Linux:     ./bin/standalone.sh --server-config=../../docs/examples/configs/standalone-xts.xml
        For Windows:   \bin\standalone.bat --server-config=..\..\docs\examples\configs\standalone-xts.xml


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

The following expected output should appear. Note there will be some other log messages interlaced between these. The output explains what actually went on when these tests ran.

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


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Express Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.
### Create the OpenShift Application

Note that we use the `jboss-as-quickstart@jboss.org` user for these examples. You need to substitute it with your own user name.

Open a shell command prompt and change to a directory of your choice. Enter the following command, replacing APPLICATION_TYPE with `jbosseap-6.0` for quickstarts running on JBoss Enterprise Application Platform 6, or `jbossas-7` for quickstarts running on JBoss AS 7:

    rhc app create -a wsatsimple -t APPLICATION_TYPE

_NOTE_: The domain name for this application will be `wsatsimple-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `wsatsimple` and will run the application inside the `jbosseap-6.0`  or `jbossas-7` container. You should see some output similar to the following:

    Creating application: wsatsimple
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added the RSA host key for IP address '23.20.102.147' to the list of known hosts.
    Confirming application 'wsatsimple' is available:  Success!
    
    wsatsimple published:  http://wsatsimple-quickstart.rhcloud.com/
    git url:  ssh://76f095330e3f49af97a52e513a9c966b@wsatsimple-quickstart.rhcloud.com/~/git/wsatsimple.git/
    Successfully created application: wsatsimple

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

        cd wsatsimple
        git rm -r src pom.xml

Copy the source for the wsat-simple quickstart into this new git repo:

        cp -r <quickstarts>/wsat-simple/src .
        cp <quickstarts>/wsat-simple/pom.xml .

### Configure the OpenShift Server

Openshift does not have Web services or WS-AT enabled by default, so we need to modify the server configuration. To do this open `.openshift/config/standalone.xml` (this file may be hidden) in an editor and make the following additions:

1. If the following extensions do not exist, add them under the `<extensions>` element: 

        <extension module="org.jboss.as.webservices"/>
        <extension module="org.jboss.as.xts"/>

2. If the jmx subsystem is not configured under the `<profile>` element, copy the following under the `<profile>` element to enable and configure JMX:

        <subsystem xmlns="urn:jboss:domain:jmx:1.1">
            <show-model value="true"/>
            <remoting-connector/>
        </subsystem>
3. If the webservices subsystem is not configured under the `<profile>` element, copy the following under the `<profile>` element to enable and configure Web Services:
        
        <subsystem xmlns="urn:jboss:domain:webservices:1.1">
            <modify-wsdl-address>true</modify-wsdl-address>
            <wsdl-host>${env.OPENSHIFT_APP_DNS}</wsdl-host>
            <wsdl-port>80</wsdl-port>
            <endpoint-config name="Standard-Endpoint-Config"/>
            <endpoint-config name="Recording-Endpoint-Config">
                <pre-handler-chain name="recording-handlers" protocol-bindings="##SOAP11_HTTP ##SOAP11_HTTP_MTOM ##SOAP12_HTTP ##SOAP12_HTTP_MTOM">
                    <handler name="RecordingHandler" class="org.jboss.ws.common.invocation.RecordingServerHandler"/>
                </pre-handler-chain>
            </endpoint-config>
        </subsystem>
        <subsystem xmlns="urn:jboss:domain:xts:1.0">
            <xts-environment url="http://${OPENSHIFT_INTERNAL_IP}:8080/ws-c11/ActivationService"/>
        </subsystem>
4. To reduce the amount of logging and make it easier to read the logs produced by this quickstart, edit the log level by adding the following block just below the other blocks:

        <logger category="org.apache.cxf.service.factory.ReflectionServiceFactoryBean">
            <level name="WARN"/>
        </logger>

The `.openshift/config/standalone.xml` is now ready, so save it and exit your editor.

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml .openshift/config/standalone.xml
        git commit -m "wsat-simple quickstart on OpenShift"
        git push

OpenShift will build the application using Maven, and deploy it to JBoss AS 7. If successful, you should see output similar to:

    remote: [INFO] ------------------------------------------------------------------------
    remote: [INFO] BUILD SUCCESS
    remote: [INFO] ------------------------------------------------------------------------
    remote: [INFO] Total time: 19.991s
    remote: [INFO] Finished at: Wed Mar 07 12:48:15 EST 2012
    remote: [INFO] Final Memory: 8M/168M
    remote: [INFO] ------------------------------------------------------------------------
    remote: Running .openshift/action_hooks/build
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/wsatsimple/jbossas-7/standalone/tmp/vfs
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/wsatsimple/jbossas-7/standalone/tmp/work
    remote: Running .openshift/action_hooks/deploy
    remote: Starting application...
    remote: Done
    remote: Running .openshift/action_hooks/post_deploy
    To ssh://1e63c17c2dd94a329f21555a33dc617d@wsatsimple-quickstart.rhcloud.com/~/git/wsatsimple.git/
       e6f80bd..63504b9  master -> master

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments` directory and deployed without a context path.

### Test the OpenShift Application

Now you will start to tail the log files of the server. To do this run the following command, remembering to replace the application name and login id.

        rhc app tail -a wsatsimple

Once the app is deployed, you can test the application by accessing the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

    http://wsatsimple-quickstart.rhcloud.com/WSATSimpleServletClient

If the application has run successfully you should see some output in the browser. You should also see some output on the server log, similar to the output from the "Test commit" test above.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a wsatsimple

To view the list of your current OpenShift applications, type:

        rhc domain

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must destroy an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To destroy an existing application, type the following, substituting the application name you want to destroy: `rhc app destroy -a APPLICATION_NAME_TO_DESTROY`
