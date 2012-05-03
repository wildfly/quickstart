helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================
Author: Serge Pagop, Andy Taylor

What is it?
-----------

This example demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in JBoss AS 7.1.0.

This project creates a queue named `HELLOWORLDMDBQueue` which is bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Full Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh -c standalone-full.xml
        For Windows: JBOSS_HOME\bin\standalone.bat -c standalone-full.xml


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-helloworld-mdb.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-helloworld-mdb/>.


Investigate the Server Console Output
-------------------------

Look at the JBoss Application Server console or Server log and you should see log messages like the following:

    15:42:35,453 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-47 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 1
    15:42:35,455 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-46 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 2
    15:42:35,457 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-50 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 3
    15:42:35,478 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-53 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 5
    15:42:35,481 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-52 (group:HornetQ-client-global-threads-1267410030)) Received Message: This is message 4


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

Build and Deploy the Quickstart - to OpenShift
-------------------------

_NOTE: At the time of this writing, JBoss Enterprise Application Platform 6 is not yet available on OpenShift, so only the JBoss AS 7 version of this quickstart can be deployed to OpenShift_.

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command:

    rhc app create -a hellworldmdb -t jbossas-7

_NOTE_: The domain name for this application will be `helloworldmdb-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application with the name you entered above and will run the application inside a `jbossas-7` container. You should see some output similar to the following:

    Creating application: helloworldmdb
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'helloworldmdb-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'helloworldmdb' is available:  Success!
    
    helloworldmdb published:  http://helloworldmdb-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@helloworldmdb-quickstart.rhcloud.com/~/git/helloworldmdb.git/
    Successfully created application: helloworldmdb

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldmdb-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can now migrate the quickstart source. You no longer need the default application so change directory into the new git repository and tell git to remove the source files and pom:

    cd helloworldmdb
    git rm -r src pom.xml

Copy the source for the this quickstart into this new git repository:

    cp -r QUICKSTART_HOME/helloworld-mdb/src .
    cp QUICKSTART_HOME/helloworld-mdb/pom.xml .
    
Now we need enable HornetQ, JBoss AS' messaging provider.

First, add the the messaging extension. Under `<extensions>`, add:

        <extension module="org.jboss.as.messaging"/>

Now, enable MDBs. In the `ejb3` subsytem, un-comment the `mdb` elements.

Finally, we need to enable and configure HorentQ. Add this subsystem to `.openshift/config/standalone.xml` under the `<profile>` element:

        <subsystem xmlns='urn:jboss:domain:messaging:1.1'>
            <hornetq-server>
                <persistence-enabled>true</persistence-enabled>
                <journal-file-size>102400</journal-file-size>
                <journal-min-files>2</journal-min-files>
                <connectors>
                    <in-vm-connector name='in-vm' server-id='0' />
                </connectors>
                <acceptors>
                    <in-vm-acceptor name='in-vm' server-id='0' />
                </acceptors>
                <address-settings>
                    <address-setting match='#'>
                        <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                        <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                        <redelivery-delay>0</redelivery-delay>
                        <max-size-bytes>20971520</max-size-bytes>
                        <address-full-policy>PAGE</address-full-policy>
                        <message-counter-history-day-limit>10</message-counter-history-day-limit>
                    </address-setting>
                </address-settings>
                <jms-connection-factories>
                    <connection-factory name='InVmConnectionFactory'>
                        <connectors>
                            <connector-ref connector-name='in-vm' />
                        </connectors>
                        <entries>
                            <entry name='java:/ConnectionFactory' />
                        </entries>
                    </connection-factory>
                    <connection-factory name='RemoteConnectionFactory'>
                        <connectors>
                            <connector-ref connector-name='in-vm' />
                        </connectors>
                        <entries>
                            <entry name='RemoteConnectionFactory' />
                        </entries>
                    </connection-factory>
                    <pooled-connection-factory name='hornetq-ra'>
                        <transaction mode='xa' />
                        <connectors>
                            <connector-ref connector-name='in-vm' />
                        </connectors>
                        <entries>
                            <entry name='java:/JmsXA' />
                        </entries>
                    </pooled-connection-factory>
                </jms-connection-factories>
                <jms-destinations />
                <security-enabled>false</security-enabled>
            </hornetq-server>
        </subsystem>


You can now deploy the changes to your OpenShift application using git as follows:

    git add src pom.xml .openshift
    git commit -m "helloworld-mdb quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the war build by openshift to be copied to the `deployments` directory, and deployed without a context path.

When the push command returns you can retest the application by getting the following URLs either via a browser or using tools such as curl or wget:

* <http://helloworldmdb-quickstart.rhcloud.com/> 

If the application has run succesfully you should see some output in the browser.

now you can look at the output of the server by running the following command:

    rhc app status -a helloworldmdb

This will show the tail of the servers log which should show something like the following.

    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-0 (HornetQ-client-global-threads-1772719)) Received Message: This is message 4
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1772719)) Received Message: This is message 1
    2012/03/02 05:52:33,067 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-6 (HornetQ-client-global-threads-1772719)) Received Message: This is message 5
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-3 (HornetQ-client-global-threads-1772719)) Received Message: This is message 3
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-2 (HornetQ-client-global-threads-1772719)) Received Message: This is message 2


You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldmdb

