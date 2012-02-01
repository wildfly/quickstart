hibernate4: How to Use Hibernate 4 in an Application
====================================================
Author: Madhumita Sadhukhan 

What is it?
-----------

This is a quickstart built upon kitchensink additionally demonstrating how to use Hibernate4 as ORM over JPA in AS7(or EAP6)! 
It's a sample, deployable Maven 3 project to help you
get your foot in the door developing with Java EE 6 and Hibernate 4 on JBoss AS 7 or EAP 6. This 
project is setup to allow you to create a compliant Java EE 6 application 
using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 , Hibernate-Core and Hibernate Bean Validation. 
It includes a persistence unit associated with Hibernate session and some sample persistence and transaction code 
to help you get your feet wet with database access in enterprise Java. 

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6. 
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.
 
With the prerequisites out of the way, you're ready to build and deploy.


Adding correct Dependencies
---------------------------

JBoss AS7 (or EAP 6) provides both JPA as well as Hibernate4(as ORM) and also supports Hibernate3 etc.
If you choose to use Hibernate4 packaged within JBoss AS7(or EAP6) you will need to first import the JPA API.
This quickstart demonstrates usage of Hibernate Session and Hibernate Validators.
You will also need to add dependencies to the required Hibernate modules for using these features in pom.xml with scope as provided.
for eg .

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

You may refer "Adding a new external dependency" located at http://community.jboss.org/wiki/HackingOnAS7 for further help on adding dependencies.

Please note that if you are working with Hibernate 3 the process is different as you will need to bundle the jars since JBoss AS7(or EAP6)
does not ship with Hibernate 3.
Please refer to the quickstart demonstrating Hibernate3 for details on how to bundle the jars in such cases.


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

This will deploy `target/jboss-as-hibernate4.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-hibernate4/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.
 
Importing the project into an IDE
=================================

If you created the project using the Maven archetype wizard in your IDE
(Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should
already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the 
JBoss AS 7 Getting Started Guide for Developers.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
