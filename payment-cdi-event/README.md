payment-cdi-event: Use CDI Events to Process Debit and Credit Operations
========================================================================
Author: Elvadas Nono  
Level: Beginner  
Technologies: CDI, JSF  
Summary: The `payment-cdi-event` quickstart demonstrates how to create credit and debit *CDI Events* in WildFly, using a JSF front-end client.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `payment-cdi-event` quickstart demonstrates how to use *CDI Events* in Red Hat JBoss Enterprise Application Platform.

The JSF front-end client allows you to create both credit and debit operation events.

To test this quickstart, enter an amount, choose either a Credit or Debit operation, and then click on *Pay* to create the event.

A Session scoped (@SessionScoped) payment event handler catches the operation and produces (@Produces) a named list of all operations performed during this session.  The event is logged in the WildFly server log and the event list is displayed in a table at the bottom of the form.
 
The payment-cdi-event quickstart defines the following classes:
 
 *   PaymentBean: 
     *   A session scoped bean that stores the payment form information: 
         *   payment amount
         *   operation type: debit or credit
     *   It contains the following utilities methods:
         *   public String pay(): Process the operation when the user clicks on submit. We have only one JSP page, so the method does not return anything and the flow of control doesn't change.
         *   public void reset(): Clear the payment form data.
 *   PaymentEvent: We have only one Event. It handles both credit and debit operations. Qualifiers help us to make the difference at injection point.
 *   PaymentTypeEnum:  A typesafe enum is used to represent the operation payment type. It contains utility methods to convert between String and Enum.
 *   The qualifiers package contains the Credit and Debit classes. The annotation determines the operation of injecting Event.
 *   The handler package containss Interfaces and implementations of PaymentEvent Observers.
     *   ICreditEventObserver: Interface to listen to CREDIT Event Only (@Observes @Credit).
     *   IDebitEventObserver: Interface to listen to DEBIT Event (@Observes @Debit).
 *   PaymentHandler: 
     *   The concrete implementation of the payment handler, it implements both IcreditEventObserver and IDebitEventObserver.
     *   The payment handler exposes the list of events caught during a session ( @Named  name=payments).
 

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-payment-cdi-event.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-payment-cdi-event/>.


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

