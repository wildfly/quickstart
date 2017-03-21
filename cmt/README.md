# cmt: Container Managed Transactions (CMT)

Author: Tom Jenkinson  
Level: Intermediate  
Technologies: EJB, CMT, JMS  
Summary: The `cmt` quickstart demonstrates Container-Managed Transactions (CMT), showing how to use transactions managed by the container.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `cmt` quickstart demonstrates using transactions managed by the container in ${product.name.full}. It is a fairly typical scenario of updating a database and sending a JMS message in the same transaction. A simple MDB is provided that prints out the message sent but this is not a transactional MDB and is purely provided for debugging purposes.

Aspects touched upon in the code:

1. XA transaction control using the container managed transaction annotations
2. XA access to the standard default datasource using the JPA API
3. XA access to a JMS queue

After users complete this quickstart, they are invited to run through the following quickstarts:

1. [jts](../jts/README.md) - The JTS quickstart builds upon this quickstart by distributing the CustomerManager and InvoiceManager
2. [jts-distributed-crash-rec](../jts-distributed-crash-rec/README.md) - The crash recovery quickstart builds upon the [jts](../jts/README.md) quickstart by demonstrating the fault tolerance of ${product.name}.

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_


### What are Container Managed Transactions?

Prior to EJB, getting the right incantation to ensure sound transactional operation of the business logic was a highly specialised skill. Although this still holds true to a great extent, EJB has provided a series of improvements to allow simplified transaction demarcation notation that is therefore easier to read and test.

With CMT, the EJB container sets the boundaries of a transaction. This differs from BMT (bean managed transactions) where the developer is responsible for initiating and completing a transaction via the methods begin, commit, rollback on a `javax.transaction.UserTransaction`.

### What Makes This an Example of Container Managed Transactions?

Take a look at `org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJB`. You can see that this stateless session bean has been marked up with the @javax.ejb.TransactionAttribute annotation.

The available options for this annotation are as follows:

* Required - As demonstrated in the quickstart. If a transaction does not already exist, this will initiate a transaction and complete it for you, otherwise the business logic will be integrated into the existing transaction
* RequiresNew - If there is already a transaction running, it will be suspended, the work performed within a new transaction which is completed at exit of the method and then the original transaction resumed.
* Mandatory - If there is no transaction running, calling a business method with this annotation will result in an error
* NotSupported - If there is a transaction running, it will be suspended and no transaction will be initiated for this business method
* Supports - This will run the method within a transaction if a transaction exists, alternatively, if there is no transaction running, the method will not be executed within the scope of a transaction
* Never - If the client has a transaction running and does not suspend it but calls a method annotated with Never then an EJB exception will be raised.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Start the Server with the Full Profile

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the full profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh -c standalone-full.xml
        For Windows: ${jboss.home.name}\bin\standalone.bat -c standalone-full.xml


## Build and Deploy the Quickstart

1. Open a command prompt and navigate to the root directory of this quickstart.
2. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

3. This will deploy `target/${project.artifactId}.war` to the running instance of the server.

## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

You will be presented with a simple form for adding customers to a database.

After a user is successfully added to the database, a message is produced containing the details of the user. An example MDB will dequeue this message and print the following contents:

    Received Message: Created invoice for customer named:  Jack

When the same customer name is given, a duplicate warning is given and no JMS-message is send to cause the above message.

The customer name should match: letter & '-', otherwise an error is given. This is to show that a 'LogMessage' entity is still stored in the database thanks to the ```@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)```
that the method logCreateCustomer in the EJB LogMessageManagerEJB is decorated with.


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    HHH000431: Unable to determine H2 database version, certain features may not work

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

_NOTE:_ Within JBoss Developer Studio, be sure to define a server runtime environment that uses the `standalone-full.xml` configuration file.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
