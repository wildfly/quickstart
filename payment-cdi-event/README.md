CDI Event
=======================

This quickstart demonstrates how to use *CDI 1.0 Events* in JBoss AS 7 or EAP 6.
 The JSF front-end client allows you to create both credit and debit operation events.

To test this quickstart, enter an amount, choose either a Credit or Debit operation,
and then click on *Pay* to create the event.
A Session scoped (@SessionScoped) payment event handler catches the operation
 and produces (@Produces) a named list of all operations performed during this session.
The event is logged in the JBoss console and the event list is displayed in
 a table at the bottom of the form.
 
 The payment sample defines the folowing classes
 
 +beans
 --PaymentBean: A session scope bean which stores information of the payment form:
 					   amount, operation type: debit/credit and two utilities methods:
 					   public String pay() -----> proceed the payment operation when user click on submit
 					   Here we have only one jsp page so, the method return nothing and the control flow doesn't change.
 					
 					   public void reset: clear the payment form data.
 					   
 +Event
 --PaymentEvent: We have only one Event with two derivates ( Credit or debit) Qualifiers help us to make the difference at injection point
 					   
 
 +qualifiers
 --Credit and Debit Annotation to make difference when injecting Event
 
 +handler package: Contains interface and implementation of paymentEvent Observers
 ICreditEventObserver: interface to listen to CreditEvent Only (@Observes @Credit)
 IDebitEventObserver: observes only Debit Event (@Observes @Debit)
 
 PaymentHandler: The concrete implementation of payment handler, implements both IcreditEventObserver and IDebitEventObserver
 				 The paymenet handler exposes the list of event he catched during a session
 				 ( @Named  name=payments)
 
 
PaymentTypeEnum:  enum to represent Payment type with various method to convert from String (think about input checkbox value coming from the user experience) to Enum
 
 
You can test the output at the URL http://localhost:8080/jboss-as-payment-cdi-event

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

or if you are using windows

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
