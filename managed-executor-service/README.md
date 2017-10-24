# managed-executor-service: Managed Executor Service example

Author: Rafael Benevides  
Level: Beginner  
Technologies: EE Concurrency Utilities, JAX-RS, JAX-RS Client API  
Summary: The `managed-executor-service` quickstart demonstrates how Java EE applications can submit tasks for asynchronous execution.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The Managed Executor Service (javax.enterprise.concurrent.ManagedExecutorService) allows Java EE applications to submit tasks for asynchronous execution. It is an extension of Java SE's Executor Service (java.util.concurrent.ExecutorService) adapted to the Java EE platform requirements.

Managed Executor Service instances are managed by the application server, thus Java EE applications are forbidden to invoke any lifecycle related method.

A JAX-RS resource provides access to some operations that are executed asynchronously.

This quickstart does not contain any user interface. The tests must be run to verify everything is working correctly.

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in ${product.name} and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Configuration Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for ${product.name.full}._

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

1. Open a command line and navigate to the root of the  ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Investigate the Console Output

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientIT
    feb. 22, 2017 4:22:22 PM org.xnio.Xnio <clinit>
    INFO: XNIO version 3.3.4.Final
    feb. 22, 2017 4:22:22 PM org.xnio.nio.NioXnio <clinit>
    INFO: XNIO NIO Implementation Version 3.3.4.Final
    feb. 22, 2017 4:22:22 PM org.jboss.remoting3.EndpointImpl <clinit>
    INFO: JBoss Remoting version 4.0.18.Final
    feb. 22, 2017 4:22:23 PM org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientIT testRestResources
    INFO: creating a new product
    feb. 22, 2017 4:22:23 PM org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientIT testRestResources
    INFO: Product created. Executing a long running task
    feb. 22, 2017 4:22:26 PM org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientIT testRestResources
    INFO: Deleting all products
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.619 sec - in org.jboss.as.quickstarts.managedexecutorservice.test.ProductsRestClientIT

    Results :

    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

## Investigate the Server Console Output

Look at the ${product.name} console or Server log and you should see log messages like the following:

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


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    WFLYJCA0091: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

To run the tests in Red Hat JBoss Developer Studio:

You must first set the active Maven profile in project properties to be either `arq-managed` for running on managed server or `arq-remote` for running on remote server. Then, to run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
