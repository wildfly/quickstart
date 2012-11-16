helloworld-mdb: Helloword Using an MDB (Message-Driven Bean)
============================================================
Author: Serge Pagop, Andy Taylor, Jeff Mesnil  
Level: Intermediate  
Technologies: JMS, EJB, MDB  
Summary: Demonstrates the use of JMS 1.1 and EJB 3.1 Message-Driven Bean  
Target Product: EAP

What is it?
-----------

This example demonstrates the use of *JMS 1.1* and *EJB 3.1 Message-Driven Bean* in JBoss Enterprise Application Platform 6 or JBoss AS 7.1.0.

This project creates two JMS resources:

* A queue named `HELLOWORLDMDBQueue` bound in JNDI as `java:/queue/HELLOWORLDMDBQueue`
* A topic named `HELLOWORLDMDBTopic` bound in JNDI as `java:/topic/HELLOWORLDMDBTopic`


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

4. This will deploy `target/jboss-as-helloworld-mdb.war` to the running instance of the server. Look at the JBoss Application Server console or Server log and you should see log messages corresponding to the deployment of the message-driven beans and the JMS destinations:

        14:11:01,020 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.queue.HELLOWORLDMDBQueue
        14:11:01,029 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/queue/HELLOWORLDMDBQueue
        14:11:01,030 INFO org.hornetq.core.server.impl.HornetQServerImpl trying to deploy queue jms.topic.HELLOWORLDMDBTopic
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQueueMDB' with 'hornetq-ra' resource adapter
        14:11:01,060 INFO org.jboss.as.ejb3 JBAS014142: Started message driven bean 'HelloWorldQTopicMDB' with 'hornetq-ra' resource adapter
        14:11:01,070 INFO org.jboss.as.messaging JBAS011601: Bound messaging object to jndi name java:/topic/HELLOWORLDMDBTopic

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-helloworld-mdb/> and will send some messages to the queue.

To send messages to the topic, use the following URL: <http://localhost:8080/jboss-as-helloworld-mdb/HelloWorldMDBServletClient?topic>

Investigate the Server Console Output
-------------------------

Look at the JBoss Application Server console or Server log and you should see log messages like the following:

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

        mvn jboss-as:undeploy


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

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command, replacing APPLICATION_TYPE with `jbosseap-6.0` for quickstarts running on JBoss Enterprise Application Platform 6, or `jbossas-7` for quickstarts running on JBoss AS 7:

    rhc app create -a hellworldmdb -t APPLICATION_TYPE

The domain name for this application will be `helloworldmdb-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldmdb` and will run the application inside the `jbosseap-6.0`  or `jbossas-7` container. You should see some output similar to the following:

    Creating application: helloworldmdb
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'helloworldmdb-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'helloworldmdb' is available:  Success!
    
    helloworldmdb published:  http://helloworldmdb-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@helloworldmdb-quickstart.rhcloud.com/~/git/helloworldmdb.git/
    Successfully created application: helloworldmdb

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldmdb-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.
        

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

    cd helloworldmdb
    git rm -r src pom.xml

Copy the source for the `helloworld-mdb` quickstart into this new git repository:

    cp -r QUICKSTART_HOME/helloworld-mdb/src .
    cp QUICKSTART_HOME/helloworld-mdb/pom.xml .
    
### Configure the OpenShift Server

HornetQ is enabled by default in `.openshift/config/standalone.xml`. There is nothing to do to be able to send and receive messages from OpenShift.

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

    rhc app status -a helloworldmdb

This will show the tail of the servers log which should show something like the following.

    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-0 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 4
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 1
    2012/03/02 05:52:33,067 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-6 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 5
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-3 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 3
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-2 (HornetQ-client-global-threads-1772719)) Received Message from queue: This is message 2


You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldmdb
        
_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must destroy an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To destroy an application, type the following, substituting the application name you want to destroy: `rhc app destroy -a APPLICATION_NAME_TO_DESTROY`
