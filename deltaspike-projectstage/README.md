deltaspike-projectstage: Demonstrate usage of DeltaSpike project stage and shows usage of a conditional @Exclude
======================================================
Author: Rafael Benevides
Level: Beginner
Technologies: JSF, CDI, Deltaspike
Summary: Demonstrate usage of DeltaSpike project stage and shows usage of a conditional @Exclude
Prerequisites: 
Target Product: WFK

What is it?
-----------

Project stages allow to use implementations depending on the current environment. E.g. you can implement a bean which creates sample-data for system tests which gets activated only in case of project-stage *SystemTest*

*Besides custom project-stages* it's possible to use the following pre-defined project-stages:

- UnitTest
- Development
- SystemTest
- IntegrationTest
- Staging
- Production

Furthermore, with `@Exclude` it's possible to annotate beans which should be ignored by CDI even if they are in a CDI enabled archive.

This project has a interface called `MyBean` that has 4 different implementations:

- ExcludedExceptOnDevelopment - That uses the following annotation `@Exclude(exceptIfProjectStage=Development.class)` excluding the implementation if the project-stage is different from development.
- ExcludedOnDevelopment - That uses the following annotation `@Exclude(ifProjectStage=Development.class)` excluding the implementation in case of project-stage development.
- MyExcludedBean  - That uses the following annotation `@Exclude` excluding the implementation in any case.
- NoExcludedBean - That doesn't use any annotation, so this implementation is always available.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
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
4. This will deploy `target/jboss-as-deltaspike-projectstage.war` to the running instance of the server.

Access the application
---------------------

Access the running application in a browser at the following URL:  <http://localhost:8080/jboss-as-deltaspike-projectstage>

You be presented with a simple page that shows the current project stage: *Staging*. You will se also the *List of available CDI instances for MyBean* table with two available implementations.

Edit the file `src/main/resources/META-INF/apache-deltaspike.properties` and change the `org.apache.deltaspike.ProjectStage` property to `Development`. Deploy the application again

        mvn clean package jboss-as:deploy

Access the application again at the same URL:  <http://localhost:8080/jboss-as-deltaspike-projectstage>

Look at *List of available CDI instances for MyBean* table and realize that the available implementations has changed.
        
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

