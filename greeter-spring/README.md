greeter-spring: Greeter Example using Spring 3.2
======================================================
Author: Marius Bogoevici
Level: Beginner
Technologies: Spring MVC, JSP, and JPA 2.0
Summary: Demonstrates the use of JPA 2.0 and JSP in WildFly 8.
Target Product: EAP
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>

What is this?
-------------

The application this project produces is designed to be run on WildFly 8.

When you deploy this example, two users are automatically created for you: emuster and jdoe. This data is located in the `src/main/resources/init-db.sql` file.

The `greeter-Spring` differs from the standard `greeter` quickstart in that it uses Spring MVC for Mapping GET and POST request:

* `<mvc:annotation-driven\>` configured in `src/main/webapp/WEB-INF/spring-mvc-context.xml` tells Spring to look for `@RequestMapping` in our controllers.
* Spring then routes the HTTP requests to the correct methods in `CreateController.java` and `GreetController`

Spring's XML configurations are used to get hold of the database and entity manager (via jndi) to perform transactional operations:

* `<tx:jta-transaction-manager/>` and `<tx:annotation-driven/>` are configured in `/src/main/webapp/WEB-INF/spring-business-context.xml`
* Methods in UserDaoImpl are marked as `@Transactional`, which Spring, using aspect oriented programming, surrounds with boilerplate code to make the methods transactional

To test this example:

1. Enter a name in the username field and click on Greet!.
2. If you enter a username that is not in the database, you get a message No such user exists!.
3. If you enter a valid username, you get a message "Hello, " followed by the user's first and last name.
4. To create a new user, click the Add a new user link. Enter the username, first name, and last name and then click Add User. The user is added and a message displays the new user id number.
5. Click on the Greet a user! link to return to the Greet! page.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on WildFly 8.

Configure Maven
---------------

If you have not yet done so, you must Configure Maven before testing the quickstarts.

Start WildFly 8 with the Web Profile
---------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

Build and Deploy the Quickstart
----------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:
        mvn clean package wildfly:deploy

4. This will deploy target/greeter-spring.war to the running instance of the server.

If you don't have maven configured you can manually copy target/greeter-spring.war to JBOSS_HOME/standalone/deployments.

Access the application
----------------------

The application will be running at the following URL: <http://localhost:8080/jboss-as-greeter-spring>

Undeploy the Archive
---------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Or you can manually remove the application by removing greeter-spring.war from JBOSS_HOME/standalone/deployments

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse)

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
