helloworld-ws: Hello World JAX-WS Web Service
==================================================
Author: Lee Newson
Level: Beginner
Technologies: JAX-WS
Summary: Deployment of a basic JAX-WS Web service bundled in a WAR archive
Target Product: EAP

What is it?
-----------

This example demonstrates the use of *JAX-WS* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7* as a simple Hello World application.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Custom Options
----------------------         

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-helloworld-ws.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/jboss-as-helloworld-ws?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Client Tests using Arquillian
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

		mvn clean test -Parq-jbossas-remote


Investigate the Console Output
----------------------------

The following expected output should appear. The output shows what was said to the Web Service by the client and the responses it received.

    WSDL Deployment URL: http://localhost:8080/jboss-as-helloworld-ws/HelloWorldService?wsdl
    [Client] Requesting the WebService to say Hello.
    [WebService] Hello World!
    [Client] Requesting the WebService to say Hello to John.
    [WebService] Hello John!
    [Client] Requesting the WebService to say Hello to John, Mary and Mark.
    [WebService] Hello John, Mary & Mark!
    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.988 sec


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

        rhc app create -a helloworldws -t APPLICATION_TYPE

_NOTE_: The domain name for this application will be `helloworldws-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldws` and will run the application inside a `jbossas-7` or a `jbosseap-6.0` container. You should see some output similar to the following:

    Creating application: helloworldws
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'helloworldws-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'helloworldws' is available:  Success!
    
    helloworldws published:  http://helloworldws-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@helloworldws-quickstart.rhcloud.com/~/git/helloworldws.git/
    Successfully created application: helloworldws

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldws-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can now migrate the quickstart source. You no longer need the default application so change directory into the new git repository and tell git to remove the source files and pom:

        cd helloworldws
        git rm -r src pom.xml

Copy the source for the `helloworld-ws` quickstart into this new git repository:

        cp -r <quickstarts>/helloworld-ws/src .
        cp <quickstarts>/helloworld-ws/pom.xml .
        
### Configure the OpenShift Server

Openshift does not have Web services setup by default, so we need to modify the server configuration. To do this open `.openshift/config/standalone.xml` (this file may be hidden) in an editor and make the following additions:

1. If the webservices subsystem is not configured as below under the `<profile>` element, copy the following and replace the webservices subsystem to enable and configure Web Services:
        
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

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "helloworld-ws quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the war build by openshift to be copied to the `deployments` directory, and deployed to the "jboss-as-helloworld-ws" context path.

### Access the OpenShift Application

Now you will start to tail the log files of the server. To do this run the following command, remembering to replace the application name and login id.

        rhc app tail -a helloworldws

Once the app is deployed, you can test the application by accessing the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

    http://helloworldws-quickstart.rhcloud.com/jboss-as-helloworld-ws/HelloWorldService?wsdl

If the application has run successfully you should see some output in the browser.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Run the Remote Client Tests against Openshift

This quickstart provides tests that can be run remotely. By default, these tests are configured to be skipped as the tests require the application to be running remotely. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line.._

1. Make sure you have deployed the Application to Openshift as described above.
2. Type the following command to run the test goal with the following profile activated and the URL of the deployed Application:

		mvn clean test -Pjbossas-remote -Dremote.server.url=http://helloworldws-quickstart.rhcloud.com/

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldws

