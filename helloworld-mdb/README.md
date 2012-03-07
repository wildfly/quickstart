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

Deploying the Application in OpenShift
----------------------------------

Firstly lets assume you already have an openshift(express) account with a domain created. If you don't please visit https://openshift.redhat.com/app/login create an account and follow the getting started guide which can be found at http://docs.redhat.com/docs/en-US/OpenShift_Express/2.0/html/Getting_Started_Guide/index.html.

Note that we'll use the `jboss-as-quickstart@jboss.org` user for these examples, you'll need to substituite it with your own user name.

Open up a shell and from the directory of your choice run the following command to create our helloworld application.

    rhc-create-app -a helloworldmdb -t jbossas-7 -l jboss-as-quickstart@jboss.org

You should see some output which will show the application being deployed and also the URL at which it can be accessed. If creation is successful, you should see similar output:

    helloworldmdb published:  http://helloworldmdb-quickstart.rhcloud.com/
    git url:  ssh://1e63c17c2dd94a329f21555a33dc617d@helloworldmdb-quickstart.rhcloud.com/~/git/helloworldmdb.git/
    Successfully created application: helloworldmdb

Now in a separate shell navigate to the quickstarts directory and copy the `helloworld-mdb/pom.xml` and `helloworld/src` directory to the `hellworldmdb` directory created by `rhc-create-app`. For example, on Linux or Mac:

    cp helloworld-mdb/pom.xml ../helloworldmdb
    cp -r helloworld-mdb/src ../helloworldmdb
    
Now, we can add the files to the OpenShift GIT repository

    git add src pom.xml

Commit them, and push them up to OpenShift

    git commit -m "deploy"

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
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/helloworldmdb/jbossas-7/standalone/tmp/vfs
    remote: Emptying tmp dir: /var/lib/libra/1e63c17c2dd94a329f21555a33dc617d/helloworldmdb/jbossas-7/standalone/tmp/work
    remote: Running .openshift/action_hooks/deploy
    remote: Starting application...
    remote: Done
    remote: Running .openshift/action_hooks/post_deploy
    To ssh://1e63c17c2dd94a329f21555a33dc617d@helloworldmdb-quickstart.rhcloud.com/~/git/helloworldmdb.git/
       e6f80bd..63504b9  master -> master

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the war build by openshift to be copied to the `deployments` directory, and deployed without a context path.

Once the app is deployed open up a browser and run the application, the URL will be similar as follows but with your own
domain name.

    http://helloworldmdb-quickstart.rhcloud.com/HelloWorldMDBServletClient

If the application has run succesfully you should see some output in the browser.

now you can look at the output of the server by running the following command:

    rhc-ctl-app -a helloworldmdb -c status

This will show the tail of the servers log which should show something like the following.

    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-0 (HornetQ-client-global-threads-1772719)) Received Message: This is message 4
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-1 (HornetQ-client-global-threads-1772719)) Received Message: This is message 1
    2012/03/02 05:52:33,067 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-6 (HornetQ-client-global-threads-1772719)) Received Message: This is message 5
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-3 (HornetQ-client-global-threads-1772719)) Received Message: This is message 3
    2012/03/02 05:52:33,065 INFO  [class org.jboss.as.quickstarts.mdb.HelloWorldMDB] (Thread-2 (HornetQ-client-global-threads-1772719)) Received Message: This is message 2
