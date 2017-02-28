deltaspike-projectstage: Demonstrate usage of DeltaSpike project stage and shows usage of a conditional @Exclude
======================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: JSF, CDI, Deltaspike  
Summary: Demonstrate usage of DeltaSpike project stage and shows usage of a conditional @Exclude  
Prerequisites: 
Target Product: ${product.name}  
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>  

What is it?
-----------

Project stages provide a way to customize the implementation based on the type of deployment environment. For example, you may want to generate sample data for system testing, but not for production. You can create a bean that generates sample data and activate it only for project stage *SystemTest*

*Besides custom project stages* it's possible to use the following pre-defined project stages:

- UnitTest
- Development
- SystemTest
- IntegrationTest
- Staging
- Production

Furthermore, with `@Exclude`, it is possible to annotate beans which should be ignored by CDI, even if they are in a CDI enabled archive.

This project has a interface called `MyBean` that has 4 different implementations:

- ExcludedExceptOnDevelopment - Uses the annotation `@Exclude(exceptIfProjectStage=Development.class)` to exclude the implementation if the project stage is anything other than Development..
- ExcludedOnDevelopment - Uses the annotation `@Exclude(ifProjectStage=Development.class)` to exclude the implementation for project stage Development.
- MyExcludedBean  - Uses the annotation `@Exclude` to exclude the implementation for all project stages.
- NoExcludedBean - The implementation is always available because this bean does not use any annotation.

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on WildFly 10.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start WildFly 10
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
4. This will deploy `target/wildfly-as-deltaspike-projectstage.war` to the running instance of the server.

Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/wildfly-deltaspike-projectstage>

You be presented with a simple page that shows the current project stage: *Staging*. You will se also the *List of available CDI instances for MyBean* table with two available implementations.

Edit the file `src/main/resources/META-INF/apache-deltaspike.properties` and change the `org.apache.deltaspike.ProjectStage` property to `Development`. Deploy the application again

        mvn clean package wildfly:deploy

Access the application again at the same URL:  <http://localhost:8080/wildfly-deltaspike-projectstage>

Look at *List of available CDI instances for MyBean* table and realize that the available implementations has changed.
        
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

