cmt (Container Managed Transactions): Example Using Transactions That Are Managed by the Container 
==================================================================================================
Author: Tom Jenkinson

## What is it?

This example demonstrates using transactions managed by the container. It is a fairly typical 
scenario of updating a database and sending a JMS message in the same transaction. A simple 
MDB is provided that prints out the message sent but this is not a transactional MDB and is 
purely provided for debugging purposes.

Aspects touched upon in the code:

1. XA transaction control using the container managed transaction annotations
2. XA access to a H2 database using the JPA API
3. XA access to a JMS queue

### What are container managed transactions?

Prior to EJB, getting the right incantation to ensure sound transactional operation of a users
business logic was a highly specialised skill. Although this still holds true to a great 
extent, EJB has provided a series of improvements to to allow simplified transaction demarcation 
notation that is therefore easier to read and test. With CMT, the EJB container sets the 
boundaries of a transaction, this differs from BMT (bean managed transactions) where the developer
is responsible for initiating and completing a transaction via the methods begin, commit, rollback
on a <code>javax.transaction.UserTransaction</code>.

#### What makes this an example of container managed transactions?

Take a look at <code>org.jboss.as.quickstarts.cmt.ejb.CustomerManagerEJBImpl</code>. You can see that
this stateless session bean has been marked up with an @javax.ejb.TransactionAttribute annotation.

The available options for this annotation are as follows:

* Required - As demonstrated in the example. If a transaction does not already exist, 
this will initiate a transaction and	complete it for you, otherwise the business 
logic will be integrated into the existing transaction
* RequiresNew - If there is already a transaction running, it will be suspended, 
the work performed within a new transaction which is completed at exit of the method 
and then the original transaction resumed. 
* Mandatory - If there is no transaction running, calling a business method with 
is annotated	with this will result in an error
* NotSupported - If there is a transaction running, it will be suspended and no 
transaction will be initiated for this business method
* Supports - This will run the method within a transaction if a transaction exists, 
alternatively, if there is no transaction running the method will not be executed 
within the scope of a transaction 
* Never - If the client has a transaction running and does not suspend it but calls 
a method annotated with Never then an EJB exception will be raised.

## System requirements

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or JBoss Enterprise Application Platform 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss Enterprise Application Platform 6.


## Deploying the application

First you need to start JBoss AS 7 (or JBoss Enterprise Application Platform 6)
with a JMS connection factory in it. The instructions for this vary slightly depending upon whether you are using the
community release (JBoss AS 7) or the platform release (JBoss Enterprise
Application Platform 6).

For JBoss AS 7:

    $JBOSS_HOME/bin/standalone.sh -c standalone-full.xml

or if you are using JBoss AS 7 on windows

    $JBOSS_HOME/bin/standalone.bat -c standalone-full.xml

For JBoss Enterprise Application Platform 6:

    $JBOSS_HOME/bin/standalone.sh

or if you are using JBoss Enterprise Application Platform 6 on windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-cmt.war` to the running instance of the AS.

The application will be running at the following URL <http://localhost:8080/jboss-as-cmt/>.

After a user is successfully added to the database, a message is produced container the 
details of the user. An example MDB will dequeue this message and print the contents as such:
	
    Received Message: Created customer named: Tom with ID: 1

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

If you need to redeploy the war then ensure that you explicitly undeploy it first so that databases
are cleaned up correctly.

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.

## Using the application

JBoss AS 7 comes with an H2 datasource by default. The example shows how to transactionally
insert key value pairs into to this H2 database and demonstrates the requirements on the
developer with respect to the JPA Entity Manager.

To access the application type the following into a browser:

    http://localhost:8080/jboss-as-cmt/

You will be presented with a simple form for adding customers to a database.

## Downloading the sources and Javadocs

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
