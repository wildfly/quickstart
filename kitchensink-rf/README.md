kitchensink-rf: Example Using Multiple Java EE 6 Technologies with RichFaces
========================
Author: Pete Muir, Brian Leathem
Level: Intermediate
Technologies: CDI, JSF, JPA, EJB, JPA, JAX-RS, BV
Summary: An example that incorporates multiple technologies
Target Product: WFK

What is it?
-----------

This is your project! It is a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0 with RichFaces 4, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes a persistence unit and some sample persistence and transaction code to introduce you to database access in enterprise Java.

This application builds on top of the standard JSF approach, by incorporating the RichFaces project to provide a set of components, allowing for a rich user experience.  RichFaces builds on top of the JSF standard, and is a fully portable solution compatible with all JSF implementations.

The kitchensink quickstart is built using Vanilla JSF for its front end.  With this kitchensink-rf quickstart, we build on top of the JSF user interface, augmenting it with RichFaces JSF components and capabilities.  Some key points to make note of while running the application:

*   Ajax push: This application makes use of ajax push.  When a member is created in one browser, the member list is updated in **all** open browsers.

    Try this yourself, by opening two different browsers, create a new member in one browser, and watch for the list to be updated in both browsers.

*   Ajax: All page updates are made with an ajax call, increasing the page responsiveness, and leading to a more native **feeling** application.

*   Client-side validation: By simply nesting a <rich:validator /> tag in the input elements, we wire them with RichFaces client-side validation capabilities.  The inputs are validated locally using javascript, without requiring a round-trip to the server.

*   Popups: Click the view link next to a member in the member list to view a popup with the member details.  To close the popup, click the "X" in the top right-hand corner, or click anywhere on the background mask.

*   Mobile support: view the application form a webkit powered browser on a mobile device to try out the mobile version of the application.  Alternatively, view the mobile version on your desktop by navigating to the url: <http://localhost:8080/jboss-as-kitchensink-rf/mobile/>

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better, and the RichFaces library.

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

4. This will deploy `target/jboss-as-kitchensink-rf.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-kitchensink-rf/>.


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