kitchensink: Assortment of technologies including Arquillian
========================
Author: Pete Muir


What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java. 

There is a tutorial for this quickstart in the [Getting Started Developing Applications Guide](https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide "Getting Started Developing Applications Guide").

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

         For Linux:   JBOSS_HOME/bin/standalone.sh
         For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.html/#buildanddeploy) for complete instructions and additional options._


#### Build and Deploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

            mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-kitchensink.war` to the running instance of the server.

#### Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to undeploy the archive:

            mvn jboss-as:undeploy
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink/>.


Run the Arquillian tests
----------------------------

This quickstart provides Arquillian tests. See [Run the Arquillian Tests](../README.html/#arquilliantests) for information on how to set up and run the tests. When asked to start the JBoss Server, this quickstart requires that you start it with the _web_ profile.


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.html/#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc
