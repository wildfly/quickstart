hibernate4: How to Use Hibernate 4 in an Application
====================================================
Author: Madhumita Sadhukhan
Level: Intermediate
Technologies: Hibernate 4
Summary: This quickstart performs the same functions as the _hibernate3_ quickstart, but uses Hibernate 4 for database access. Compare this quickstart to the _hibernate3_ quickstart to see the changes needed to run with Hibernate 4..
Target Product: EAP

What is it?
-----------

This quickstart is based upon the kitchensink example, but demonstrates how to use Hibernate ORM 4 over JPA in JBoss Enterprise Application Platform 6 or JBoss AS 7.

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 , Hibernate-Core and Hibernate Bean Validation.  It includes a persistence unit associated with Hibernate session and some sample persistence and transaction code to help you with database access in enterprise Java. 

You can compare this quickstart to the `hibernate3` quickstart to see the code differences between Hibernate 3 and Hibernate 4.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add the Correct Dependencies
---------------------------

JBoss Enterprise Application Platform 6 and JBoss AS 7 both provide Hibernate 3, Hibernate 4, and JPA support. 

If you use Hibernate 4 packaged within JBoss Enterprise Application Platform 6 or JBoss AS 7, you will need to first import the JPA API.

This quickstart demonstrates usage of Hibernate Session and Hibernate Validators.

If you look at the pom.xml file in the root of the hibernate4 quickstart directory, you will see that the dependencies for the Hibernate modules have been added with the scope as `provided`.
For example:

      <dependency>
         <groupId>org.hibernate</groupId>
         <artifactId>hibernate-validator</artifactId>
         <version>4.2.0.Final</version>
         <scope>provided</scope>
         <exclusions>
            <exclusion>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-api</artifactId>
            </exclusion>
         </exclusions>
      </dependency>

Please note that if you are working with Hibernate 3, the process is different. You will need to bundle the jars since JBoss Enterprise Application Platform 6 and JBoss AS 7 do not ship with Hibernate 3. Refer to the `hibernate3` quickstart for details on how to bundle the JARs.


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

4. This will deploy `target/jboss-as-hibernate4.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-hibernate4/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
