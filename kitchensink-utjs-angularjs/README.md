kitchensink-utjs-angular: Example using Undertow.js with Angular.js on the front end
====================================================================================
Author: Stuart Douglas
Level: Intermediate
Technologies: Undertow.js, Angular.js
Summary: Based on kitchensink, but uses a Angular for the front end and Undertow.js for the back end
Target Project: WildFly
Source: <${github.repo.url}>

What is it?
-----------

This quickstart is a deployable Maven 3 project to help you get your foot in the door developing with Undertow.js and Java EE7 on JBoss WildFly.

This project is setup to allow you to create a compliant Java EE 7 application using Undertow.js CDI 1.1, JPA 2.1 and Bean Validation 1.1. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in Undertow.js.

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly 10+.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-eap-quickstarts#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-kitchensink-utjs-angularjs.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-kitchensink-utjs-angularjs/>.


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
