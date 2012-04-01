jts: Java Transaction Service - Distributed EJB Transactions Across Multiple Containers 
======================================================================================
Author: Tom Jenkinson

What is it?
-----------

This example demonstrates how to perform distributed transactions in an application. A distributed transaction is a set of operations performed by two or more nodes, participating in an activity coordinated as a single entity of work, and fulfilling the properties of an ACID transaction. ACID meaning:

* Atomic
* Consistent
* Isolated
* Durable.

The example uses Java Transaction Service (JTS) to propagate a transaction context across two Container-Managed Transaction (CMT) EJBs that, although deployed in separate servers, participate in the same transaction. In this example, one server processes the Customer and Account data and the other server processes the Invoice data.

The example expects the EJBs to be deployed onto the same physical machine. This is not a restriction of JTS and the example can easily be converted to run on separate machines by editing the hostname value for the InvoiceManagerEJB in org.jboss.as.quickstarts.cmt.jts.ejb.AccountManagerEJB.

The example builds upon the `cmt` quickstart. Once again, a simple MDB has been provided that prints out the messages that are sent. This is not a transactional MDB and is purely provided for debugging purposes.

You will see that the AccountManagerEJB uses the EJB home for the remote EJB. This is expected to connect to remote EJBs and could be simplified if the EJB is deployed locally.

This example uses a JMS connection factory and defines a queue named `JTSQueue`. 


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Configure the JBoss Servers
-------------------------

For this example, you will need two instances of the application server, with a subtle startup configuration difference. Application server 2 must be started up with a port offset parameter provided to the startup script as "-Djboss.socket.binding.port-offset=100"

The application servers should both be configured as follows:

1. Open the file JBOSS_HOME/standalone/configuration/standalone-full.xml
2. Enable JTS as follows:
    * Find the orb subsystem and change the configuration to:  

            <subsystem xmlns="urn:jboss:domain:jacorb:1.1">
                <orb>
                    <initializers security="on" transactions="on"/>
                </orb>
            </subsystem>
    * Find the transaction subsystem and append the `<jts/>` element:  

            <subsystem xmlns="urn:jboss:domain:transactions:1.1">
                <!-- LEAVE EXISTING CONFIG AND APPEND THE FOLLOWING -->
                <jts/>
            </subsystem>
3. Make a copy of the this JBoss directory structure to use for the second server.


Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Servers
-------------------------

If you are using Linux:

    Server 1: JBOSS_HOME_SERVER_1/bin/standalone.sh -c standalone-full.xml
    Server 2: JBOSS_HOME_SERVER_2/bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100

If you are using windows

    Server 1: JBOSS_HOME_SERVER_1\bin\standalone.bat -c standalone-full.xml
    Server 2: JBOSS_HOME_SERVER_2\bin\standalone.bat -c standalone-full.xml -Djboss.socket.binding.port-offset=100


Build and Deploy the Quickstart
-------------------------

Since this quickstart builds two separate components, you can not use the standard *Build and Deploy* commands used by most of the other quickstarts. You must follow these steps to build, deploy, and run this quickstart.

Make sure you have started the two separate JBoss servers. See the instructions in the previous section.

To deploy the application, you first need to produce the archives to deploy using the following Maven goals. 
Note that `application-component-2` must be built first as it provides an EJB client to `application-component-1`. 
Also note that `application-component-2` must be "installed".

2. Open a command line and navigate to the `jts` quickstart directory
3. Install application-component-2:
    * Navigate to the application-component-2 subdirectory:

            cd QUICKSTART_HOME/jts/application-component-2
    * Install the component
      For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

                mvn install -s PATH_TO_QUICKSTARTS/example-settings.xml

      For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

                mvn install
4. Build application-component-1:
    * Navigate to the application-component-1 subdirectory:
          cd QUICKSTART_HOME/jts/application-component-1
    * Build the component
        For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

                mvn clean package -s PATH_TO_QUICKSTARTS/example-settings.xml

        For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

                mvn package
5. Deploy the artifacts to the JBoss application server. Since this application is written with little failure detection, it is best to deploy `application-component-2` first so that when `application-component-1` is deployed, it can resolve the EJB from the other container.
    * Deploy application-component-2:

            cd QUICKSTART_HOME/jts/application-component-2
            mvn jboss-as:deploy
    * Deploy application-component-1:

            cd QUICKSTART_HOME/jts/application-component-1
            mvn jboss-as:deploy


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-jts-application-component-1/>.

When you enter a name and click to "invoice" that customer, you will see the following in the application server 1 console:
    
    12:09:38,424 INFO  [org.jboss.ejb.client] (http-localhost-127.0.0.1-8080-1) JBoss EJB Client version 1.0.0.Beta11
    12:09:38,513 ERROR [jacorb.orb] (http-localhost-127.0.0.1-8080-1) no adapter activator exists for jboss-as-jts-application-component-2&%InvoiceManagerEJBImpl&%home
    12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-1 (group:HornetQ-client-global-threads-1095034080)) Received Message: Created customer named: Tom

You will also see the following in application-server-2 console:

    12:09:38,697 INFO  [org.jboss.ejb.client] (RequestProcessor-10) JBoss EJB Client version 1.0.0.Beta11
    12:09:39,204 INFO  [class org.jboss.as.quickstarts.cmt.jts.mdb.HelloWorldMDB] (Thread-3 (group:HornetQ-client-global-threads-649946595)) Received Message: Created invoice for customer named: Tom

The web page will then change and  prompt you to check the logs for the MDB messages in the server consoles as noted above. At this point you can be satisfied that the quickstart has operated correctly.
