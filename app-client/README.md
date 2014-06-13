app-client: Application Client to run with the JBoss WildFly appclient container
======================================================
Author: Wolf-Dieter Fink  
Level: Intermediate  
Technologies: EJB, EAR, AppClient  
Summary: Shows how to use the JBoss WildFly application client container to support injection
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>


What is it?
-----------

This quickstart demonstrates how to use the JBoss WildFly client container to start the client 'Main' program and provide Dependency Injections (DI) for client applications. It also shows you how to use Maven to package the application according to the JavaEE specification.


This example consists of the following Maven projects, each with a shared parent:

| **Sub-project** | **Description** |
|:-----------|:-----------|
| `ejb` | An application that can be called by the `client`.
| `ear` | The EAR packaging contains the server and client side.
| `client-simple` | A simple client application for running in the application-client container to show the injection

The root `pom.xml` file builds each of the subprojects in the appropriate order.



System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 6.1 or later.

All you need to build this project is Java 6.0 (Java SDK 1.6) or later, Maven 3.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Add the Application Users
---------------

If the client and server are run on different hosts, you must add the following users to the JBoss WildFly server side application. Be sure to use the names and passwords specified in the table as they are required to run this example.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| admin| ManagementRealm | admin-123 | _leave blank for none_ |
| quickuser| ApplicationRealm | quick-123 | _leave blank for none_ |

To add the users, open a command prompt and type the following commands:

        For Linux:
            JBOSS_HOME/bin/add-user.sh -u admin -p admin-123
            JBOSS_HOME/bin/add-user.sh -a -u quickuser -p quick-123

        For Windows:
            JBOSS_HOME\bin\add-user.sh -u admin -p admin-123
            JBOSS_HOME\bin\add-user.bat -a -u quickuser -p quick-123

If you prefer, you can use the add-user utility interactively. For an example of how to use the add-user utility, see instructions in the root README file located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Start the JBoss WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started and configured the JBoss Server successful as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build the artifacts:

        mvn clean install wildfly:deploy



Access the Remote Client Application at the same machine
---------------------

This example shows how to invoke an EJB from a remote standalone application.
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext.

1. Make sure that the deployments are successful as described above.
2. Navigate to the root directory of this quickstart and type the command to run the application. Be sure to replace `JBOSS_HOME` with the path to your WildFly installation.

        For Linux:   JBOSS_HOME/bin/appclient.sh ear/target/wildfly-application-client.ear#simpleClient.jar Hello from command line
        For Windows: JBOSS_HOME\bin\appclient.sh ear\target\wildfly-application-client.ear#simpleClient.jar Hello from command line

    The client will output the following information provided by the server application:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@myhost


    This output shows that the client is called with arguments and the `ServerApplication` is called at the jboss.node `myhost`.
    The application client connected automatically a server on the same machine.

    Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}


Access the Remote Client Application at different machines
---------------------

This example shows how to invoke an EJB from a remote standalone application.
It also demonstrates how to invoke an EJB from a client using a scoped-context rather than a properties file containing the parameters required by the InitialContext.

1. Install JBoss WildFly on a different machine.
2. Add the application user to JBoss WildFly on the other server as described above.
3. Start the WildFly server on the other machine with the following command line:

        For Linux:   JBOSS_HOME/bin/standalone.sh -b SERVER_MACHINE_IP_ADDRESS -bmanagement SERVER_MACHINE_IP_ADDRESS
        For Windows: JBOSS_HOME\bin\standalone.bat -b SERVER_MACHINE_IP_ADDRESS -bmanagement SERVER_MACHINE_IP_ADDRESS

4. Type this command to deploy the artifacts on your local machine:

        mvn clean install wildfly:deploy -Dwildfly.hostname=SERVER_MACHINE_IP_ADDRESS [-Dwildfly.port=9099] -Dwildfly.username=admin -Dwildfly.password=admin-123

5. Make sure that the deployments are successful as described above.
6. Create a jboss-ejb-client.properties file with the following content

        remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED=false
        remote.connections=default
        remote.connection.default.host=SERVER_MACHINE_IP_ADDRESS
        remote.connection.default.port=8080
        remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS=false
        remote.connection.default.username=quickuser
        remote.connection.default.password=quickuser-123

7. Type this command to run the application:

        For Linux:   JBOSS_HOME/bin/appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear/target/jboss-application-client.ear#simpleClient.jar Hello from command line
        For Windows: JBOSS_HOME\bin\appclient.sh --ejb-client-properties=jboss-ejb-client.properties ear\target\jboss-application-client.ear#simpleClient.jar Hello from command line

    The client will output the following information provided by the applications:

        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51) Main started with arguments
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-51)             [Hello, from, command, line]
        [org.jboss.as.quickstarts.appclient.acc.client.Main] (Thread-##) Hello from StatelessSessionBean@theOtherHOST

    This output shows that the `ServerApplication` is called at the jboss.node `theOtherHOST`.

    Review the server log files to see the bean invocations on the server.

         ClientContext is here = {Client=myhost}

    As shown the connected server(s) can be configured with the properties file. It is also possible to connect multiple servers
    or a cluster with the same jboss-ejb-client.properties as it works for a standalone client.



Undeploy the Archives
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

