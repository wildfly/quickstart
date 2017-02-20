managedexecutorservice: ManagedExecutorService example
================================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: EE Concurrency Utilities, JAX-RS, JAX-RS Client API 
Summary:The `managedexecutorservice` quickstart demonstrates how Java EE applications can submit tasks for asynchronous execution.  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  


What is it?
-----------

The Managed Executor Service (javax.enterprise.concurrent.ManagedExecutorService) allows Java EE applications to submit tasks for asynchronous execution. It is an extension of Java SE's Executor Service (java.util.concurrent.ExecutorService) adapted to the Java EE platform requirements.

Managed Executor Service instances are managed by the application server, thus Java EE applications are forbidden to invoke any lifecycle related method.

A JAX-RS resource provides access to some operations that are executed asynchronously. 

  
System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the  WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
4. This will deploy `target/wildfly-managedexecutorservice.war` to the running instance of the server.
 


Run the Tests
-------------------------


This quickstart provides tests that shows how the asynchronous tasks are executed. By default, these tests are configured to be skipped as the tests requires that the application to be deployed first. 


1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn test -Prest-test

Run tests from JBDS
-----------------------

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'rest-client'.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
------------------------------


    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientTest
    [timestamp] org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientTest testRestResources
    INFO: creating a new product
    [timestamp] org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientTest testRestResources
    INFO: Product created. Executing a Long running task
    [timestamp] org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientTest testRestResources
    INFO: Deleting all products
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.202 sec - in org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientTest
    
    Results :
    
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
    
Investigate the Server Console Output
-------------------------------------
Look at the WildFly console or Server log and you should see log messages like the following:

    13:34:07,940 INFO  [ProductResourceRESTService] (default task-51) Will create a new Product on other Thread
    13:34:07,940 INFO  [ProductResourceRESTService] (default task-51) Returning response
    13:34:07,941 INFO  [PersitTask] (EE-ManagedExecutorService-default-Thread-5) Begin transaction
    13:34:07,941 INFO  [PersitTask] (EE-ManagedExecutorService-default-Thread-5) Persisting a new product
    13:34:07,946 INFO  [PersitTask] (EE-ManagedExecutorService-default-Thread-5) Commit transaction
    13:34:08,002 INFO  [ProductResourceRESTService] (default task-52) Submitting a new long running task to be executed
    13:34:08,003 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:08,009 INFO  [LongRunningTask] (EE-ManagedExecutorService-default-Thread-5) Starting a long running task
    13:34:08,010 INFO  [LongRunningTask] (EE-ManagedExecutorService-default-Thread-5) Analysing A Product
    13:34:08,306 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:08,608 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:08,912 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:09,215 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:09,519 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:09,823 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:10,128 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:10,431 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:10,735 INFO  [ProductResourceRESTService] (default task-52) Waiting for the result to be available...
    13:34:11,040 INFO  [ProductResourceRESTService] (default task-52) Result is available. Returning result...56
    13:34:11,082 INFO  [ProductResourceRESTService] (default task-53) Will delete all Products on other Thread
    13:34:11,082 INFO  [ProductResourceRESTService] (default task-53) Returning response
    13:34:11,082 INFO  [DeleteTask] (EE-ManagedExecutorService-default-Thread-5) Begin transaction
    13:34:11,083 INFO  [DeleteTask] (EE-ManagedExecutorService-default-Thread-5) Deleting all products
    13:34:11,092 INFO  [DeleteTask] (EE-ManagedExecutorService-default-Thread-5) Commit transaction. Products deleted: 1

Note that the PersistTask and DeleteTask were executed after ProductResourceRESTService sends a Response. The only exception is for LongRunningTask where ProductResourceRESTService waits for its response.
    


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------


You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
   

