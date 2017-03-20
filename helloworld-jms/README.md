# helloworld-jms: Helloworld JMS Example

Author: Weston Price  
Level: Intermediate  
Technologies: JMS  
Summary: The `helloworld-jms` quickstart demonstrates the use of external JMS clients with ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `helloworld-jms` quickstart demonstrates the use of external JMS clients with ${product.name.full}.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a ${product.name} server.

2. A message consumer that receives message from a JMS destination deployed to a ${product.name} server.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Add an Application User

This quickstart uses secured management interfaces and requires that you create the following application user to access the running application.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |

To add the application user, open a command prompt and type the following command:

        For Linux:   ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
        For Windows: ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


## Configure the Server

You configure the JMS `test` queue by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-jms.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone-full.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml
        For Windows:  ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml
3. Review the `configure-jms.cli` file in the root of this quickstart directory. This script adds the `test` queue to the `messaging` subsystem in the server configuration file.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-jms.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-jms.cli
5. You should see the following result when you run the script:

        The batch executed successfully
6. Stop the ${product.name} server.


## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone-full.xml` file and review the changes.

The following `testQueue` jms-queue was configured in the default server configuration of the  `messaging-activemq` subsystem.

      <jms-queue name="testQueue" entries="queue/test java:jboss/exported/jms/queue/test"/>


## Start the Server with the Full Profile

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml
        For Windows: ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml


## Build and Execute the Quickstart

To run the quickstart from the command line:

1. Make sure you have started the ${product.name} server. See the instructions in the previous section.

2. Open a command prompt and navigate to the root of the helloworld-jms quickstart directory:

        cd PATH_TO_QUICKSTARTS/helloworld-jms

3. Type the following command to compile and execute the quickstart:

        mvn clean compile exec:java

_NOTE: If you execute this command multiple times, you may see the following warning and exception, followed by a stacktrace. This is caused by a bug in Artemis that has been fixed, but not yet released. For details, see <https://issues.apache.org/jira/browse/ARTEMIS-158>. You can ignore this warning._

    WARN: AMQ212007: connector.create or connectorFactory.createConnector should never throw an exception, implementation is badly behaved, but we will deal with it anyway.
    java.lang.IllegalArgumentException: port out of range:-1


## Investigate the Console Output

If the Maven command is successful, with the default configuration you will see output similar to this:

    timestamp org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
    timestamp org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    timestamp org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    timestamp org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    timestamp AM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    timestamp org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!


## Optional Properties

The example provides for a certain amount of customization for the `mvn:exec` plug-in using the system properties.

* `username`

    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).

    Default: `quickstartUser`

* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user)

    Default: `quickstartPwd1!`

* `connection.factory`

    The name of the JMS ConnectionFactory you want to use.

    Default: `jms/RemoteConnectionFactory`

* `destination`

    The name of the JMS Destination you want to use.

    Default: `jms/queue/test`

* `message.count`

    The number of JMS messages you want to produce and consume.

    Default: `1`

* `message.content`

    The content of the JMS TextMessage.

    Default: `"Hello, World!"`

* `java.naming.provider.url`

	  This property allows configuration of the JNDI directory used to lookup the JMS destination. This is useful when the client resides on another host.

    Default: `"localhost"`


## Remove the JMS Configuration

You can remove the JMS configuration by running the  `remove-jms.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Remove the JMS Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml
        For Windows:  ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-jms.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=remove-jms.cli
3. This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully

### Remove the JMS Configuration Manually
1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Be sure to [Add an Application User](#add-an-application-user) as described above.
2. Configure and start the ${product.name} server in Red Hat JBoss Developer Studio:
    * Define a server runtime environment that uses the `standalone-full.xml` configuration file.
    * Start the server defined in the previous step.
3. Outside of JBoss Developer Studio, configure the JMS `test` queue by running the JBoss CLI commands as described above under [Configure the Server](#configure-the-server).
4. In JBoss Developer Studio, right-click on the `${project.artifactId}` project and choose `Run As` --> `Java Application`.  In the `Select Java Application` window, choose `HellowWorldJMSClient - org.jboss.as.quickstarts.jms` and click `OK`. The client output displays in the `Console` window.
The output messages appear in the `Console` window.
5. Be sure to [Remove the JMS Configuration](#remove-the-jms-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
