---
Author: Serge Pagop, Andy Taylor, Jeff Mesnil
Level: Intermediate
Technologies: JMS, EJB, MDB
Summary: Demonstrates the use of JMS and EJB Message-Driven Bean
Target Product: WildFly
Source: https://github.com/wildfly/quickstart/
---

helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================

What is it?
-----------

This example demonstrates the use of *JMS 2.0* and *EJB 3.2 Message-Driven Bean* in WildFly 8.

This project creates two JMS resources:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/topic/HELLOWORLDMDBTopic`

These destinations are automatically created when the project is deployed on WildFly thanks to the [src/main/webapp/WEB-INF/hornetq-jms.xml](hornetq-jms.xml file).

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on WildFly 8.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#configure-maven-) before testing the quickstarts.


Start WildFly 8 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-helloworld-mdb.war` to the running instance of the server. Look at the WildFly console or Server log and you should see log messages corresponding to the deployment of the message-driven beans and the JMS destinations:

        14:11:01,020 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.queue.HELLOWORLDMDBQueue
        14:11:01,029 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/queue/HELLOWORLDMDBQueue
        14:11:01,030 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.topic.HELLOWORLDMDBTopic
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQueueMDB' with 'hornetq-ra' resource adapter
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQTopicMDB' with 'hornetq-ra' resource adapter
        14:11:01,070 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/topic/HELLOWORLDMDBTopic

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-helloworld-mdb/> and will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/wildfly-helloworld-mdb/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the WildFly console or Server log and you should see log messages like the following:

    17:51:52,122 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-1 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 1
    17:51:52,123 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-11 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 2
    17:51:52,124 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-12 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 5
    17:51:52,135 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-13 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 4
    17:51:52,136 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldQueueMDB] (Thread-14 (HornetQ-client-global-threads-26912020)) Received Message from queue: This is message 3


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


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

    rhc app create helloworldmdb https://cartreflect-claytondev.rhcloud.com/reflect?github=openshift-cartridges/openshift-wildfly-cartridge

The domain name for this application will be `helloworldmdb-<YOUR_DOMAIN_NAME>.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldmdb` and will run the application inside the `wildfly-8` container. You should see some output similar to the following:

    The cartridge 'https://cartreflect-claytondev.rhcloud.com/reflect?github=openshift-cartridges/openshift-wildfly-cartridge' will
    be downloaded and installed
    
    Application Options
    -------------------
    Domain:     quickstart
    Cartridges: https://cartreflect-claytondev.rhcloud.com/reflect?github=openshift-cartridges/openshift-wildfly-cartridge
    Gear Size:  default
    Scaling:    no
    
    Creating application 'helloworldmdb' ...
    
    ...
    
    Your application 'helloworldmdb' is now available.
    
    Run 'rhc show-app helloworldmdb' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldmdb-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

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

Finally we will configure the messaging subsystem by copying its configuration from WildFly 8.0.0.Final Full configuration profile:

    <subsystem xmlns="urn:jboss:domain:messaging:2.0">
        <hornetq-server>
            <persistence-enabled>true</persistence-enabled>
            <journal-file-size>102400</journal-file-size>
            <journal-min-files>2</journal-min-files>
            <connectors>
                <http-connector name="http-connector" socket-binding="http">
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

Once the `standalone.xml` is edited, we can commit the file to Git and push to OpenShift to restart WildFly 8 with this configuration:

    git add .openshift/config/standalone.xml
    git ci -m "add messaging subsystem to WildFly standalone configuration"
    git push

Once the push command returns, WildFly 8 has been restarted with the messaging configuration.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

    git rm -r src pom.xml

Copy the source for the `helloworld-mdb` quickstart into this new git repository:

    cp -r QUICKSTART_HOME/helloworld-mdb/src .
    cp QUICKSTART_HOME/helloworld-mdb/pom.xml .

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

    git add src pom.xml .openshift
    git commit -m "helloworld-mdb quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments` directory and deployed without a context path.

### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL either via a browser or using tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

* <http://helloworldmdb-quickstart.rhcloud.com/> to send messages to the queue
* <http://helloworldmdb-quickstart.rhcloud.com/HelloWorldMDBServletClient?topic> to send messages to the topic

If the application has run succesfully you should see some output in the browser.

Now you can look at the output of the server by running the following command:

    rhc tail -a helloworldmdb

This will show the tail of the servers log which should show something like the following.

    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-0 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 4
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 1
    2012/03/02 05:52:33,067 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-6 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 5
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-3 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 3
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-2 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 2


You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a helloworldmdb
        
_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue. 
