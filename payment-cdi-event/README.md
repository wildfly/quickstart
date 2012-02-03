payment-cdi-event: Use CDI Events to Process Debit and Credit Operations
========================================================================
Author: Elvadas Nono

This quickstart demonstrates how to use *CDI 1.0 Events* in JBoss AS 7 or JBoss Enterprise Application Platform 6.
The JSF front-end client allows you to create both credit and debit operation events.

To test this quickstart, enter an amount, choose either a Credit or Debit operation,
and then click on *Pay* to create the event.

A Session scoped (@SessionScoped) payment event handler catches the operation
 and produces (@Produces) a named list of all operations performed during this session. 
The event is logged in the JBoss console and the event list is displayed in
 a table at the bottom of the form.
 
The payment-cdi-event quickstart defines the following classes
 
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
 
 
You can test the output of this quickstart at the URL http://localhost:8080/jboss-as-payment-cdi-event

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.

With the prerequisites out of the way, you're ready to build and deploy.


Deploying the Application
-------------------------

First you need to start JBoss AS 7. To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using Windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn clean package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-payment-cdi-event.war`.

The application will be running at the following URL <http://localhost:8080/jboss-as-payment-cdi-event/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
