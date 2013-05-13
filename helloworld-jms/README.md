helloworld-jms: HelloWorld JMS Example
======================
Author: Weston Price
Level: Intermediate
Technologies: JMS
Summary: Demonstrates the use of a standalone (Java SE) JMS client
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is it?
-----------

This quickstart demonstrates the use of external JMS clients with JBoss Enterprise Application Platform 6 or JBoss AS 7.

It contains the following:

1. A message producer that sends messages to a JMS destination deployed to a JBoss Enterprise Application Platform 6 or JBoss AS 7 server.

2. A message consumer that receives message from a JMS destination deployed to a JBoss Enterprise Application Platform 6 or JBoss AS 7 server. 


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#addapplicationuser)


Configure the JBoss Enterprise Application Platform 6 server
---------------------------

If you are using the JBoss AS 7 Quickstart distribution, the server configuration file already contains the JMS `test` queue and you can skip this step. 

However, if you are using the JBoss Enterprise Application Platform 6 distribution, you need to add the JMS `test` queue to the application server configuration file. You can configure JMS by running the  `configure-jms.cli` script provided in the root directory of this quickstart, by using the JBoss CLI interactively, or by manually editing the configuration file.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server.
2. Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

#### Configure JMS by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=configure-jms.cli 
This script adds the `test` queue to the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test
        The batch executed successfully.
        {"outcome" => "success"}


#### Configure JMS Using the JBoss CLI Tool Interactively

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. To start the JBoss CLI tool, open a new command line, navigate to the JBOSS_HOME directory, and type the following:
    
        For Linux: bin/jboss-cli.sh --connect
        For Windows: bin\jboss-cli.bat --connect
3. At the prompt, type the following:

        [standalone@localhost:9999 /] jms-queue add --queue-address=testQueue --entries=queue/test,java:jboss/exported/jms/queue/test

#### Configure JMS by Manually Editing the Server Configuration File

1.  If it is running, stop the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server.
2.  Backup the file: `JBOSS_HOME/standalone/configuration/standalone-full.xml`
3.  Open the file: JBOSS_HOME/standalone/configuration/standalone-full.xml
4.  Add the JMS `test` queue as follows:
    * Find the messaging subsystem:  

            <subsystem xmlns="urn:jboss:domain:messaging:1.1">
    * Scroll to the end of this section and add the following XML after the `</jms-connection-factories>` end tag but before the `</hornetq-server>` element:

                <jms-destinations>
                    <jms-queue name="testQueue">
                        <entry name="queue/test"/>
                        <entry name="java:jboss/exported/jms/queue/test"/>
                    </jms-queue>
                </jms-destinations>
    * Save the changes and close the file.  


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

To run the quickstart from the command line:

1. Make sure you have started the JBoss server. See the instructions in the previous section.

2. Open a command line and navigate to the root of the helloworld-jms quickstart directory:

        cd PATH_TO_QUICKSTARTS/helloworld-jms

3. Type the following command to compile and execute the quickstart:

        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

            mvn clean compile exec:java -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

            mvn clean compile exec:java

 
Investigate the Console Output
-------------------------

If the maven command is successful, with the default configuration you will see output similar to this:

    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire connection factory "jms/RemoteConnectionFactory"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found connection factory "jms/RemoteConnectionFactory" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Attempting to acquire destination "jms/queue/test"
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Found destination "jms/queue/test" in JNDI
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Sending 1 messages with content: Hello, World!
    Mar 14, 2012 1:38:58 PM org.jboss.as.quickstarts.jms.HelloWorldJMSClient main
    INFO: Received message with content Hello, World!

_Note_: After the above INFO message, you may see the following error. You can ignore the error as it is a well known error message and does not indicate the maven command was unsuccessful in any way. 

    Mar 14, 2012 1:38:58 PM org.jboss.naming.remote.protocol.v1.RemoteNamingStoreV1$MessageReceiver handleEnd
    ERROR: Channel end notification received, closing channel Channel ID cd114175 (outbound) of Remoting connection 00392fe8 to localhost/127.0.0.1:4447


Optional Properties
-------------------

The example provides for a certain amount of customization for the `mvn:exec` plugin using the system properties.

* `username`
   
    This username is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#addapplicationuser).
   
    Default: `quickstartUser`
		
* `password`

    This password is used for both the JMS connection and the JNDI look-up.  Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#addapplicationuser)
   
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


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Remove the JMS Configuration
----------------------------

You can remove the JMS configuration by running the  `remove-jms.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the JMS Configuration by Running the JBoss CLI Script

1. Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server by typing the following: 

        For Linux:  JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
        For Windows:  JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        JBOSS_HOME/bin/jboss-cli.sh --connect --file=remove-jms.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        #1 jms-queue remove --queue-address=testQueue
        The batch executed successfully.
        {"outcome" => "success"}


### Remove the JMS Configuration Manually
1. If it is running, stop the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server.
2. Replace the `JBOSS_HOME/standalone/configuration/standalone-full.xml` file with the back-up copy of the file.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

 
