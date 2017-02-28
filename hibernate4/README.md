hibernate4: How to Use Hibernate 4 in an Application
====================================================
Author: Madhumita Sadhukhan  
Level: Intermediate  
Technologies: Hibernate 4  
Summary: This quickstart performs the same functions as the _hibernate5_ quickstart, but uses Hibernate 4 for database access. Compare this quickstart to the _hibernate5_ quickstart to see the changes needed to run with Hibernate 5.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

What is it?
-----------

This quickstart is based upon the kitchensink example, but demonstrates how to use Hibernate ORM 4 over JPA in JBoss WildFly.

This project is setup to allow you to create a compliant Java EE 7 application using JSF 2.2, CDI 1.1, EJB 3.2, JPA 2.1 , Hibernate-Core and Hibernate Bean Validation.  It includes a persistence unit associated with Hibernate session and some sample persistence and transaction code to help you with database access in enterprise Java.

You can compare this quickstart to the `hibernate5` quickstart to see the code differences between Hibernate 4 and Hibernate 5.


System requirements
-------------------

All you need to build this project is Java 8 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add the Correct Dependencies
---------------------------

JBoss WildFly 10 provides Hibernate 5 by default. However, it is possible to use Hibernate 4 bundled within your application. 

This quickstart demonstrates usage of Hibernate Session and Hibernate Validators.

If you look at the pom.xml file in the root of the hibernate4 quickstart directory, you will see that the dependencies for the Hibernate modules have been added with the `compile` scope (which is the default and thus omitted).
For example:

      <dependency>
         <groupId>org.hibernate</groupId>
         <artifactId>hibernate-validator</artifactId>
         <version>${version.hibernate4}</version>
         <exclusions>
            <exclusion>
               <groupId>org.slf4j</groupId>
               <artifactId>slf4j-api</artifactId>
            </exclusion>
         </exclusions>
      </dependency>

The compile scope makes sure that the Hibernate dependencies also end-up in the final WAR file.


Start JBoss WildFly with the Web Profile
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

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-hibernate4.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-hibernate4/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
