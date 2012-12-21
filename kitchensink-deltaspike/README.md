kitchensink-deltaspike: Assortment of technologies including Arquillian and DeltaSpike CDI extensions
========================
Author: Pete Muir, Bernard Tison
Level: Intermediate
Technologies: CDI, JSF, JPA, JPA, JAX-RS, BV, DeltaSpike
Summary: A version of kitchensink that uses DeltaSpike @Transactional
Target Product: EAP

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, JPA 2.0 and Bean Validation 1.0. 

This quickstart uses the `kitchensink` quickstart as its starting point. Rather than using a Stateless EJB, it leverages the DeltaSpike @Transactional annotation to give transactional behavior to a CDI bean.
The entity manager is managed by the application rather than the container. 

The DeltaSpike project (http://incubator.apache.org/deltaspike) consists of a number of portable CDI extensions that provide useful features for Java application developers.

Changes compared to the original `kitchensink` quickstart
---------------------------------------------------------

* The `org.jboss.as.quickstarts.kitchensink.service.MemberRegistration` class is annotated with the DeltaSpike `@Transactional` annotation rather than `@javax.ejb.Stateless`.
* The `org.jboss.as.quickstarts.kitchensink.util.Resources` class has been modified to handle an application managed entity manager.
* The transaction type in the persistence unit configuration file (`src/main/resources/META-INF/persistence.xml`) has been changed to `RESOURCE_LOCAL`. 
* The DeltaSpike `TransactionalInterceptor` has been added to the beans.xml CDI configuration file (`src/main/webapp/WEB-INF/beans.xml`).
* The DeltaSpike dependencies have been added to the project POM.
* The ShrinkWrap `shrinkwrap-resolver-bom` dependency has been added to the project POM, to be able to build the archive for the Arquillian test.  

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-kitchensink-deltaspike.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink-deltaspike/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-jbossas-remote 


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
