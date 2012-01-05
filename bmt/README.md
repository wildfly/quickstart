Stepping Outside the Container (with JPA and JTA)
=================================================

What is it?
-----------

On occasion the application developer requires finer grained control over the lifecycle of 
JTA transactions and JPA Entity Managers than the defaults provided by the JEE container.
This example shows how the developer can override these defaults and take control of aspects
of the lifecycle of JPA and transactions.

[But note that a JEE container is designed with robustness in mind so you should
carefully analyse the scaleabiltiy, concurrency and performance needs of you application
before taking advantage of these techniques in your own applications.]

The example demonstrates how to manually manage transaction demarcation whilst accessing
JPA entities in JBoss AS 7:

* part 1 shows the developer responsibilities when injecting an Entity Manager into a managed
(stateless) bean.
* part 2 shows the developer responsibilities when using JPA and transactions with an unmanaged
component

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.
 
Deploying the application
-------------------------

First you need to start JBoss AS 7 (or EAP 6). To do this, run

		$JBOSS_HOME/bin/standalone.sh

or if you are using windows

		$JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

		mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

		mvn jboss-as:deploy

This will deploy `target/jboss-as-bmt.war` to the running instance of the AS.

The application will be running at the following URL <http://localhost:8080/jboss-as-bmt/BMT>.

To undeploy from JBoss AS, run this command:

		mvn jboss-as:undeploy

If you need to redeploy the war then ensure that you explicitly undeploy it first so that databases
are cleaned up correctly.

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Using the application
---------------------

JBoss AS 7 comes with an H2 datasource by default. The example shows how to transactionally
insert key value pairs into to this H2 database and demonstrates the requirements on the
developer with respect to the JPA Entity Manager.

To access the application type the following into a browser:

		http://localhost:8080/jboss-as-bmt/BMT

You will be presented with a simple form for adding key value pairs and a checkbox to indicate
whether the updates should be executed using an unmanaged component (effectively this will run the transaction
and JPA updates in the servlet - ie not session beans). If the box is checked then the updates will be
executed within a session bean method.

To list all pairs leave the key input box empty. To add or update the value of a key fill in
the key and value input boxes. Press the submit button to see the results.

Downloading the sources and Javadocs
------------------------------------

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

		mvn dependency:sources
		mvn dependency:resolve -Dclassifier=javadoc
