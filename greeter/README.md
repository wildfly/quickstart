greeter: Greeter Example
========================
Author: Pete Muir
Level: Beginner
Technologies: CDI, JSF, JPA, EJB, JTA
Summary: Demonstrates the use of CDI 1.0, JPA 2.0, JTA 1.1, EJB 3.1 and JSF 2.0
Target Product: EAP

What is it?
-----------

This example demonstrates the use of *CDI 1.0*, *JPA 2.0*, *JTA 1.1*, *EJB 3.1* and *JSF 2.0* in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

When you deploy this example, two users are automatically created for you:  `emuster` and `jdoe`. This data is located in the `src/main/resources/import.sql file`.

To test this example:

1. Enter a name in the `username` field and click on `Greet!`.
2. If you enter a username that is not in the database, you get a message `No such user exists!`.
3. If you enter a valid username, you get a message "Hello, " followed by the user's first and last name.
4. To create a new user, click the `Add a new user` link. Enter the username, first name, and last name and then click `Add User`. The user is added and a message displays the new user id number.
5. Click on the `Greet a user!` link to return to the `Greet!` page.



There is a tutorial for this quickstart in the [Getting Started Developing Applications Guide](http://www.jboss.org/jdf/stage/quickstarts/jboss-as-quickstart/guide/GreeterQuickstart/).

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

4. This will deploy `target/jboss-as-greeter.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-greeter>. 


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

