jaxrs-client: JAX-RS Client API example
================================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: JAX-RS  
Summary:The `jaxrs-client` quickstart demonstrates JAX-RS Client API, which interacts with a JAX-RS Web service that runs on WildFly.  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  


What is it?
-----------

The `jaxrs-client` quickstart demonstrates the JAX-RS Client API which interacts with a JAX-RS Web service.

This client "calls" many POST, GET, DELETE operations using different ways: synchronized, asynchronous, delayed and filtered invocations.


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

        mvn package wildfly:deploy
4. This will deploy `target/wildfly-jaxrs-client.war` to the running instance of the server.
 


Run the Tests
-------------------------


This quickstart provides tests that shows the REST Client API features. By default, these tests are configured to be skipped as the tests requires that the application to be deployed first. 


1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn test -Prest-client

Run tests from JBDS
-----------------------

To be able to run the tests from JBDS, first set the active Maven profile in project properties to be either 'rest-client'.

To run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
----------------------------


        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest
        Dec 29, 2014 3:34:44 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest requestResponseFiltersTest
        INFO: ### Testing Request and Response Filters ###
        Dec 29, 2014 3:34:44 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest requestResponseFiltersTest
        INFO: dropping all contacts
        Dec 29, 2014 3:34:45 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest requestResponseFiltersTest
        INFO: Invoking create new contact using a ClientRequestFilter
        Dec 29, 2014 3:34:45 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest requestResponseFiltersTest
        INFO: Invoking list all contacts using a ClientResponseFilter
        Dec 29, 2014 3:34:45 PM org.jboss.as.quickstarts.jaxrsclient.test.LogResponseFilter filter
        INFO: Date: Mon Dec 29 15:34:45 BRST 2014- Status: 200
        Dec 29, 2014 3:34:45 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: ### Testing Delayed invocaton ###
        Dec 29, 2014 3:34:45 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: dropping all contacts
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: Creating a new contact invocation
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: Creating list all contacts invocation
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: invoking the new contact
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest delayedInvocationTest
        INFO: invoking list all contacts ASYNC
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest asyncCrudTest
        INFO: ### CRUD tests  ASYNC ###
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest asyncCrudTest
        INFO: dropping all contacts ASYNC
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest asyncCrudTest
        INFO: creating a new contact ASYNC
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest asyncCrudTest
        INFO: delete a contact by id ASYNC
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest asyncCrudTest
        INFO: fetching all contacts ASYNC
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest invocationCallBackTest
        INFO: ### Testing invocation callback ###
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest invocationCallBackTest
        INFO: dropping all contacts
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest invocationCallBackTest
        INFO: Creating a InvocationCallback
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest invocationCallBackTest
        INFO: Invoking a service using the InvocationCallback
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: ### CRUD tests ###
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: dropping all contacts
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: creating a new contact
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: fetching a contact by id
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: fetching all contacts
        Dec 29, 2014 3:34:46 PM org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest cruedTest
        INFO: delete a contact by id
        Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.51 sec - in org.jboss.as.quickstarts.jaxrsclient.test.ContactsRestClientTest
        
        Results :
        
        Tests run: 5, Failures: 0, Errors: 0, Skipped: 0


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
   

