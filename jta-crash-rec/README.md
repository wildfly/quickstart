jta-crash-rec: Example of JTA Crash Recovery
=============================
Author: Mike Musgrove

What is it?
-----------

This quickstart demonstrates how to code distributed or XA (eXtended Architecture) transactions so that the ACID properties are preserved across participating resources after a server crash. An XA transaction is one in which multiple resources, such as MDBs and databases, participate within the same transaction. It ensures all operations are performed as a single entity of work. ACID is a set of 4 properties that guarantee the resources are processed in the following manner:

* Atomic - if any part of the transaction fails, all resources remain unchanged. 
* Consistent - the state will be consistent across resources after a commit
* Isolated - the execution of the transaction for each resource is isolated from each others
* Durable - the data will persist after the transaction is committed

This quickstart shows how to atomically update multiple resources within one transaction. It updates a relational database table using JPA and sends a message using JMS. This type of paired updates to two different resources are called XA transactions and are defined by the Java EE JTA specification JSR-907. JTA transactions are not distributed across multiple application servers. 

The relational database table in this example contains two columns that represent a "key" / "value" pair. The application presents an HTML form containing two input text boxes and allows you to create, update, delete or list these pairs. When you add or update a "key" / "value" pair, the quickstart starts a transaction, updates the database table, produces a JMS message containing the update, and then commits the transaction. If all goes well, eventually the consumer gets the message and generates a database update, setting the "value" corresponding to the "key" to something that indicates it was changed by the message consumer.

In this example, you halt the JBoss server in the middle of an XA transaction after the database modification has been committed, but before the JMS producer is committed. You can verify that the transaction was started, then restart the JBoss server to complete the transaction. You then verify that everything is in a consistent state.

JBoss Enterprise Application Platform 6 and JBoss AS 7 ship with H2, an in-memory database written in Java. In this example, we use H2 for the database. Although H2 XA support is not recommended for production systems, the example does illustrate the general steps you need to perform for any datasource vendor. This example provides its own H2 XA datasource configuration. It is defined in the `jta-crash-rec-ds.xml` file in the WEB-INF folder of the WAR archive. 


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Download and Configure Byteman
------------------------

This quickstart uses Byteman to help demonstrate crash recovery. Instructions to install and configure Byteman can be found here: [Install and Configure Byteman](../README.md#byteman). 



<a name="clear-transaction-objectstore"></a>

Clear the Transaction ObjectStore
-------------------------

Make sure there is no transaction objectstore data left after testing this or any of the other quickstarts. If you are using the default file based transaction logging store:

1. Open a command line and type the following:

        ls $JBOSS_HOME/standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/
2. If this directory exists and contains any files, delete them before starting the server:

        rm -rf $JBOSS_HOME/standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/*
3. On Windows, use the file manager to accomplish the same result.



<a id="startserver"></a>

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

4. This will deploy `target/jboss-as-jta-crash-rec.war` to the running instance of the server.



Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-jta-crash-rec/XA>. 


Test the application
-------------------------

1. When you access the application, you will find a web page containing two html input boxes for adding "key" / "value" pairs to a database. Instructions for using the application are shown at the top of the application web page.

2. When you add a new "key" / "value" pair, the change is committed to the database and a JMS message sent. The message consumer then updates the newly inserted row by appending the text *"updated via JMS"* to the value. Since the consumer updates the row asynchronously, you may need to cick _Refresh Table_ to see the text added to the "key" / "value" pair you previously entered.

3. When an _XA transaction_ is committed, the application server completes the transaction in two phases.
    * In phase 1 each of the resources, in this example the database and the JMS message producer, are asked to prepare to commit any changes made during the transaction. 
    * If all resources vote to commit then the application server starts phase 2 in which it tells each resource to commit those changes. 
    * The added complexity is to cope with failures, especially failures that occur during phase 2. Some failure modes require cooperation between the application server and the resources in order to guarantee that any pending changes are recovered. 

4. To demonstrate XA recovery, you need to enable the Byteman tool to terminate the application server while _phase 2_ is running as follows:
    * Stop the JBoss server.
    * [Clear any transaction objectstore data](#clear-transaction-objectstore) remaining from previous tests.
    * Follow the instructions to [halt the application using Byteman](../README.md#byteman-halt). The following text will be appended to the server configuration file:

            For Linux: JAVA_OPTS="-javaagent:/PATH_TO_BYTEMAN_DOWNLOAD/lib/byteman.jar=script:/PATH_TO_QUICKSTARTS/jta-crash-rec/src/main/scripts/xa.btm ${JAVA_OPTS}"
            For Windows: JAVA_OPTS=%JAVA_OPTS% -javaagent:C:PATH_TO_BYTEMAN_DOWNLOAD\lib\byteman.jar=script:C:\PATH_TO_QUICKSTARTS\jta-crash-rec\src\main\scripts\xa.btm %JAVA_OPTS%
    * [Start the JBoss server](#startserver) as instructed above.

5. Once you complete step 4, you are ready to create a _recovery record_. Go to the application URL <http://localhost:8080/jboss-as-jta-crash-rec/XA> and insert another row into the database. At this point, Byteman halts the application server. 

6. If you want to verify the database insert was committed but that message delivery is still pending, you can use an SQL client such as the H2 database console tool. Issue a query to show that the value is present but does not contain the message added by the consumer (*" updated via JMS"*). Here is how you can do it using H2:
    * Start the H2 console by typing:

            java -jar $JBOSS_HOME/modules/com/h2database/h2/main/h2*.jar
    * Log in:
       
            Database URL: jdbc:h2:file:~/jta-crash-rec-quickstart
            User name:    sa
            Password:     sa
    * The console is available at the url <http://localhost:8082>. If you receive an error such as `Exception opening port "8082"` it is most likely because some other application has that port open. You will need to find which application is using the port and close it.
    * Once you are logged in enter the following query to see that the pair you entered is present but does not contain *"updated via JMS"*.

            select * from kvpair
    * Log out of the H2 console. H2 is limited to one connection and the application will need it from this point forward.
    * If you are using the default file based transaction logging store, there will be a record in the file system corresponding to the pending transaction. 

        * Open a command line and navigate to the `$JBOSS_HOME` directory
        * List the contents of the following directory:

                ls $JBOSS_HOME/standalone/data/tx-object-store/ShadowNoFileLockStore/defaultStore/StateManager/BasicAction/TwoPhaseCoordinator/AtomicAction/
        * An example of a logging record file name is: 
 
                0_ffff7f000001_-7f1cf331_4f0b0ad4_15
        * After recovery, log records are normally deleted automatically. However, logs may remain in the case where the Transaction Manager (TM) commit request was received and acted upon by a resource, but the TM crashed before it had time to clean up the logs of that resource.    
7. To observe XA recovery
    * [Disable the Byteman script](../README.md#byteman-disable) by restoring the backup server configuration file.
    * [Start the JBoss server](#startserver) as instructed above.
    * Load the web interface to the application 
    * By the time the JBoss server is ready, the transaction should have recovered.
    * A message is printed on the JBoss server console when the consumer has completed the update. Look for a line that reads 'JTA Crash Record Quickstart: key value pair updated via JMS'.
    * Check that the row you inserted in step 4 now contains the text *"updated via JMS"*, showing that the JMS message was recovered successfully. Use the application URL to perform this check.
    * You will most likely see the following message on the console. 

            ARJUNA016038: No XAResource to recover ... eis_name=...JTACrashRecQuickstartDS during recovery

        This is normal. What actually happened is that the first resource (JTACrashRecQuickstartDS) committed before the JBoss server was halted in step 5. The transaction logs are only updated/deleted after the outcome of the transaction is determined. If the transaction manager did update the log as each participant (database and JMS queue) completed then throughput would suffer. Notice you do not get a similar message for the JMS resource since that is the resource that recovered and the log record was updated to reflect this change. You need to manually remove the record for the first participant if you know which one is which or, if you are using the community version of the JBoss server, then you can also inspect the transaction logs using a JMX browser. For the demo it is simplest to delete the records from the file system, however, *be wary of doing this on a production system*.


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


