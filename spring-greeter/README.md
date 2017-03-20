# spring-greeter: Greeter Example using Spring 4.x

Author: Marius Bogoevici  
Level: Beginner  
Technologies: Spring MVC, JSP, JPA  
Summary: The `spring-greeter` quickstart is based on the `greeter` quickstart, but differs in that it uses Spring MVC for Mapping GET and POST requests.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

The `spring-greeter` quickstart is based on the `greeter` quickstart, but differs in that it uses Spring MVC for Mapping GET and POST requests:

* `<mvc:annotation-driven\>` configured in `src/main/webapp/WEB-INF/spring-mvc-context.xml` tells Spring to look for
`@RequestMapping` in our controllers.
* Spring then routes the HTTP requests to the correct methods in `CreateController.java` and `GreetController`

Spring's XML configurations are used to get hold of the database and entity manager (via jndi) to perform transactional operations:

* `<tx:jta-transaction-manager/>` and `<tx:annotation-driven/>` are configured in `/src/main/webapp/WEB-INF/spring-business-context.xml`
* Methods in UserDaoImpl are marked as `@Transactional`, which Spring, using aspect oriented programming, surrounds with
boilerplate code to make the methods transactional

When you deploy this example, two users are automatically created for you: emuster and jdoe. This data is located in the
`src/main/resources/init-db.sql` file.


To test this example:

1. Enter a name in the username field and click on Greet!.
2. If you enter a username that is not in the database, you get a message No such user exists!.
3. If you enter a valid username, you get a message "Hello, " followed by the user's first and last name.
4. To create a new user, click the Add a new user link. Enter the username, first name, and last name and then click Add User.
The user is added and a message displays the new user id number.
5. Click on the Greet a user! link to return to the Greet! page.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat

## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:
        mvn clean package wildfly:deploy

4. This will deploy target/spring-greeter.war to the running instance of the server.

If you do not have maven configured you can manually copy target/spring-greeter.war to ${jboss.home.name}/standalone/deployments.

## Access the application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Or you can manually remove the application by removing spring-greeter.war from ${jboss.home.name}/standalone/deployments

## Run the Arquillian Functional Tests

This quickstart provides Arquillian functional tests as well. They are located in the functional-tests/ subdirectory under
the root directory of this quickstart. Functional tests verify that your application behaves correctly from the user's point
of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the ${product.name} server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-remote

5. If you prefer to run the functional tests using managed instance of the ${product.name} server, meaning the tests will start the
server for you, type the following command:

        mvn clean verify -Parq-managed


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following
commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
