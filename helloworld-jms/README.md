---
Author: Weston Price
Level: Intermediate
Technologies: JMS
Summary: Demonstrates the use of a standalone (Java SE) JMS client
Target Product: WildFly
Source: https://github.com/wildfly/quickstart/
---

helloworld-jms: HelloWorld JMS Example
======================

What is it?
-----------

This quickstart demonstrates the use of external JMS clients with WildFly 8.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a WildFly 8 server.

2. A message consumer that receives message from a JMS destination deployed to WildFly 8 server.


System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, optional Maven 3.0 or better.

The application this project produces is designed to be run on WildFly 8.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user).


Configure the JBoss WildFly server
---------------------------

_NOTE - Before you begin:_

1. If it is running, stop the WildFly 8 Server.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure JMS by Running the JBoss CLI Script

1. Start the WildFly 8 Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-jms.cli
This script adds the `test` queue to the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.


#### Configure JMS Using the JBoss CLI Tool Interactively

1. Start the WildFly 8 Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:

        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        [standalone@localhost:9990] jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test

#### Configure JMS by Manually Editing the Server Configuration File

1.  If it is running, stop the WildFly 8 Server.
2.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
3.  Open the file: JBOSS_HOME/standalone/configuration/standalone-full.xml
4.  Add the JMS `test` queue as follows:
    * Find the messaging subsystem:

            <subsystem xmlns="urn:jboss:domain:messaging:2.0">
    * Scroll to the end of this section and add the following XML after the `</jms-connection-factories>` end tag but before the `</hornetq-server>` element:

                <jms-destinations>
                    <jms-queue name="testQueue">
                        <entry name="queue/test"/>
                        <entry name="java:jboss/exported/jms/queue/test"/>
                    </jms-queue>
                </jms-destinations>
    * Save the changes and close the file.


Start WildFly 8 with the Full Profile
---------------

1. Open a command line and navigate to the root of the WidFly 8 server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

To run the quickstart from the command line:

1. Make sure you have started the server. See the instructions in the previous section.

2. Open a command line and navigate to the root of the helloworld-jms quickstart directory:

        cd PATH_TO_QUICKSTARTS/helloworld-jms

3. Compile HelloWorldJMSClient.java and run the application adding the path to the jboss-client.jar from your Wildfly 8 install, for example:

java -cp ".:/Users/youruserid/wildfly-8.0.0.Final/bin/client/jboss-client.jar" org.jboss.as.quickstarts.jms.HelloWorldJMSClient 

If you prefer compiling and running code with maven, make sure that pom.xml has the proper version of Wildfly and type the following command to compile and execute the quickstart:

        mvn clean compile exec:java



Investigate the Console Output
-------------------------

If the maven command is successful, with the default configuration you will see output similar to this:

    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!

_Note_: After the above INFO message, you may see the following error. You can ignore the error as it is a well known error message and does not indicate the maven command was unsuccessful in any way.

    Mar 14, 2012 1:38:58 PM org.jboss.naming.remote.protocol.v1.RemoteNamingStoreV1$MessageReceiver handleEnd
    ERROR: Channel end notification received, closing channel Channel ID cd114175 (outbound) of Remoting connection 00392fe8 to localhost/127.0.0.1:8080


Optional Properties
-------------------

The example provides for a certain amount of customization for the `mvn:exec` plugin using the system properties.

* `username`

    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user).

    Default: `quickstartUser`

* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#add-an-application-user)

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

    Default: `"http-remoting://127.0.0.1:8080"`


Remove the JMS Configuration
----------------------------

You can remove the JMS configuration by running the `remove-jms.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Remove the JMS Configuration by Running the JBoss CLI Script

1. Start the WildFly 8 Server by typing the following:

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-jms.cli
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.


### Remove the JMS Configuration Manually
1. If it is running, stop the WildFly 8 Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

mvn dependency:sources
mvn dependency:resolve -Dclassifier=javadoc

Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command for quickstarts running on WildFly 8:

    rhc app create helloworldjms jboss-wildfly-8

The domain name for this application will be `helloworldjms-<YOUR_DOMAIN_NAME>.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldjms` and will run the application inside the `wildfly-8` container. You should see some output similar to the following:

    Application Options
    -------------------
    Domain:     quickstart
    Cartridges: jboss-wildfly-8
    Gear Size:  default
    Scaling:    no
    Creating application 'helloworldjms' ...
    ...
    Your application 'helloworldjms' is now available.
    Run 'rhc show-app helloworldjms' for more details about your app.
 
The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldjms-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

### Configure the OpenShift Server

The messaging subsystem is not enabled by default in the `.openshift/config/standalone.xml`.

We must add this messaging subsystem to be able to use JMS from OpenShift.

Edit `.openshift/config/standalone.xml` and add the `org.jboss.as.messaging` to the extension list:

    <extension module="org.jboss.as.messaging"/>

In the `ejb3` subsystem configuration, you also need to add configuration for the Message-Driven Beans in the `<mdb>` element:

    <subsystem xmlns="urn:jboss:domain:ejb3:2.0">
       <session-bean>
          ...
       </session-bean>
       <mdb>
           <resource-adapter-ref resource-adapter-name="${ejb.resource-adapter-name:hornetq-ra.rar}"/>
           <bean-instance-pool-ref pool-name="mdb-strict-max-pool"/>
       </mdb>
       ...
    </subsystem>

Finally we will configure the messaging subsystem by copying its configuration from WildFly 8 Full configuration profile:

    <subsystem xmlns="urn:jboss:domain:messaging:2.0">
         <hornetq-server>
             <persistence-enabled>true</persistence-enabled>
             <journal-file-size>102400</journal-file-size>
             <journal-min-files>2</journal-min-files>
             <connectors>
                 <http-connector name="http-connector" socket-binding="http-outbound">
                     <param key="http-upgrade-endpoint" value="http-acceptor"/>
                 </http-connector>
                 <http-connector name="http-connector-throughput" socket-binding="http">
                     <param key="http-upgrade-endpoint" value="http-acceptor-throughput"/>
                     <param key="batch-delay" value="50"/>
                 </http-connector>
                 <in-vm-connector name="in-vm" server-id="0"/>
             </connectors>
             <acceptors>
                 <http-acceptor name="http-acceptor" http-listener="default"/>
                 <http-acceptor name="http-acceptor-throughput" http-listener="default">
                     <param key="batch-delay" value="50"/>
                     <param key="direct-deliver" value="false"/>
                 </http-acceptor>
                 <in-vm-acceptor name="in-vm" server-id="0"/>
             </acceptors>
             <security-settings>
                 <security-setting match="#">
                     <permission type="send" roles="guest"/>
                     <permission type="consume" roles="guest"/>
                     <permission type="createNonDurableQueue" roles="guest"/>
                     <permission type="deleteNonDurableQueue" roles="guest"/>
                 </security-setting>
             </security-settings>
             <address-settings>
                 <!--default for catch all-->
                 <address-setting match="#">
                     <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                     <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                     <redelivery-delay>0</redelivery-delay>
                     <max-size-bytes>10485760</max-size-bytes>
                     <address-full-policy>PAGE</address-full-policy>
                     <page-size-bytes>2097152</page-size-bytes>
                     <message-counter-history-day-limit>10</message-counter-history-day-limit>
                 </address-setting>
             </address-settings>
             <jms-connection-factories>
                 <connection-factory name="InVmConnectionFactory">
                     <connectors>
                         <connector-ref connector-name="in-vm"/>
                     </connectors>
                     <entries>
                         <entry name="java:/ConnectionFactory"/>
                     </entries>
                 </connection-factory>
                 <connection-factory name="RemoteConnectionFactory">
                     <connectors>
                         <connector-ref connector-name="http-connector"/>
                     </connectors>
                     <entries>
                         <entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
                     </entries>
                 </connection-factory>
                 <pooled-connection-factory name="hornetq-ra">
                     <transaction mode="xa"/>
                     <connectors>
                         <connector-ref connector-name="in-vm"/>
                     </connectors>
                     <entries>
                         <entry name="java:/JmsXA"/>
                         <!-- Global JNDI entry used to provide a default JMS Connection factory to EE application -->
                         <entry name="java:jboss/DefaultJMSConnectionFactory"/>
                     </entries>
                 </pooled-connection-factory>
             </jms-connection-factories>
         </hornetq-server>
     </subsystem>

We must also add an `outbound-socket-binding` to connect to the WildFly server using port-forwarding. In the `<socket-binding-group>` element named `standard-sockets`, append this element:

    <socket-binding-group name="standard-sockets" ...>
        ...
        <outbound-socket-binding name="http-outbound">
            <remote-destination host="127.0.0.1" port="${jboss.http.port:8080}"/>
        </outbound-socket-binding>
    </socket-binding-group>

Once the `standalone.xml` is edited, we can commit the file to Git and push to OpenShift to restart WildFly 8 with this configuration:

   git add .openshift/config/standalone.xml
   git ci -m "add messaging subsystem to WildFly standalone configuration"
   git push

Once the push command returns, WildFly 8 has been restarted with the messaging configuration.

We must now add the JMS queue and the application user to the WildFly server deployed on OpenShift.

We connect to the deployed WildFly server using the JBoss CLI tool:

    rhc ssh hellowordjms
    jboss-cli.sh -c --controller=$OPENSHIFT_WILDFLY_IP:$OPENSHIFT_WILDFLY_MANAGEMENT_HTTP_PORT

The instructions to create the JMS Queue is the same than above.

To create an Application User, you must use the `add-user.sh` script:

    rhc ssh hellowordjms
    ./wildfly/bin/add-user.sh

The instructions to add the application user are the same than in [Add an Application User](../README.md#add-an-application-user).

### Test the OpenShift Application

To run this quickstart, *port-forwarding* must be used to forward the OpenShift server ports to your local machine using `rhc port-forward`:

    $ rhc port-forward -a helloworldjms
    Checking available ports ... done
    Forwarding ports ...
    
    To connect to a service running on OpenShift, use the Local address
    
    Service Local               OpenShift
    ------- -------------- ---- ------------------
    java    127.0.0.1:8080  =>  127.8.178.129:8080
    java    127.0.0.1:9990  =>  127.8.178.129:9990
    
    Press CTRL-C to terminate port forwarding


Once the JMS Queue and the application user have been added to the WildFly server and port forwarding has been activated, we can go to the `helloworld-jms` quickstart and connect to the WildFly server hosted on OpenShift:

    mvn exec:java

If the maven command is successful, you will see output similar to this:

    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    Feb 03, 2014 3:54:27 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

       rhc app-delete -a helloworldjms

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue.

