kitchensink-ear: Example Using Multiple Java EE 6 Technologies Deployed as an EAR
==============================================================================================
Author: Pete Muir

What is it?
-----------

This is your project! It's a sample, deployable Maven 3 project to help you get your foot in the door developing with Java EE 6 on JBoss AS 7 or JBoss Enterprise Application Platform 6. 

This project is setup to allow you to create a compliant Java EE 6 application using JSF 2.0, CDI 1.0, EJB 3.1, JPA 2.0 and Bean Validation 1.0. It includes a persistence unit and some sample persistence and transaction code to help you get your feet wet with database access in enterprise Java. 

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven 
-------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Start the JBoss Server
-------------------------

Start the JBoss Enterprise Application Platform 6 or JBoss AS 7 Server with the web profile.

1. Open a command line and navigate to the root of the JBoss directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the application
-------------------------

1. Make sure your server is running.
2. Open a command line and navigate to the root of the kitchensink-ear quickstart directory.
3. Type the following in the command line: 
    For JBoss Enterprise Application Platform 6, Maven user settings NOT configured: 

        mvn clean package jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml

    For JBoss AS 7 or JBoss Enterprise Application Platform 6, Maven user settings configured: 

        mvn clean package jboss-as:deploy

4. This will build and deploy `ear/target/jboss-as-kitchensink-ear.ear`.
5. To undeploy the application, run this command:

        mvn jboss-as:undeploy

You can also use Eclipse to start the JBoss Enterprise Application Platform 6 or JBoss AS 7 server and deploy the project. See the <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> for more information.

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-kitchensink-ear>.

1. Enter a name, email address, and Phone nubmer in the input field and click the _Register_ button.
2. If the data entered is valid, the new member will be registered and added to the _Members_ display list.
3. If the data is not valid, you must fix the validation errors and try again.
4. When the registration is successful, you will see a log message in the server console:

        Registering _the_name_you_entered_

Run the Arquillian tests
---------------------

This quickstart provides Arquillian tests. See [Run the Arquillian Tests](../README.html/#arquilliantests) for more information on how to set up and run the tests. When asked to start the JBoss Server, this quickstart requires that you start it with the _web_ profile.

### Investigate the Console Output

You should see the following console output when you run the tests:

    Results :
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

### Check the server console

You should see messages similar to the following:

    INFO  [org.jboss.as.server] (management-handler-thread - 9) JBAS018559: Deployed "test.war"
    INFO  [org.jboss.as.quickstarts.kitchensink_ear.controller.MemberRegistration] (http--127.0.0.1-8080-2) Registering Jane Doe
    INFO  [org.jboss.as.quickstarts.kitchensink_ear.test.MemberRegistrationTest] (http--127.0.0.1-8080-2) Jane Doe was persisted with id 1
    INFO  [org.jboss.weld.deployer] (MSC service thread 1-6) JBAS016009: Stopping weld service for deployment test.war
    INFO  [org.jboss.as.jpa] (MSC service thread 1-1) JBAS011403: Stopping Persistence Unit Service 'test.war#primary'
    INFO  [org.hibernate.tool.hbm2ddl.SchemaExport] (MSC service thread 1-1) HHH000227: Running hbm2ddl schema export
    INFO  [org.hibernate.tool.hbm2ddl.SchemaExport] (MSC service thread 1-1) HHH000230: Schema export complete
    INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-5) JBAS010409: Unbound data source [jboss/datasources/KitchensinkEarQuickstartTestDS]
    INFO  [org.jboss.as.server.deployment] (MSC service thread 1-6) JBAS015877: Stopped deployment test.war in 19ms
    INFO  [org.jboss.as.server] (management-handler-thread - 10) JBAS018558: Undeployed "test.war"


Import the project into an IDE
---------------------

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), there is nothing to do. You should already have an IDE project.

Detailed instructions for using Eclipse with JBoss AS 7 are provided in the JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the command line using `archetype:generate`, you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
