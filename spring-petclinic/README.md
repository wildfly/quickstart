# spring-petclinic: PetClinic Example using Spring 4.x  

Author: Ken Krebs, Juergen Hoeller, Rob Harrop, Costin Leau, Sam Brannen, Scott Andrews  
Level: Advanced  
Technologies: JPA, Junit, JMX, Spring MVC Annotations, AOP, Spring Data, JSP, webjars, Dandellion  
Summary: The `spring-petclinic` quickstart shows how to run the Spring PetClinic Application in ${product.name} using the ${product.name} BOMs.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?  

The `spring-petclinic` quickstart shows how to run the [Spring PetClinic](<http://github.com/spring-projects/spring-petclinic>) Application
in ${product.name.full} with the use of Red Hat ${product.name} BOMs (_for the best compatibility_). One of the major
changes is the use of the `webapp/WEB-INF/jboss-deployment-structure.xml` file. This file specifies which modules
to include or exclude when building the application. In this case, we exclude Hibernate libraries since the application
uses Spring Data JPA. Additionally, this is only required when using the spring-data-jpa profile, see `resources/spring/business-config.xml`.

For detailed explanation of the changes made to adapt the Quickstart to ${product.name.full} see: [CHANGES.md](CHANGES.md)

PetClinic features alternative DAO implementations and application configurations for JDBC, JPA, and Spring Data JPA, with
HSQLDB and MySQL as target databases. The default PetClinic configuration is JPA on HSQLDB.  

* The `src/main/resources/spring/business-config.xml` pulls in `src/main/resources/spring/data-access.properties` to set
the JDBC-related settings for the JPA EntityManager definition.
    * A simple comment change in `data-access.properties` switches between the data access strategies.
* In `webapp/WEB_INF/web.xml` the `<param-name>spring.profiles.active</param-name>` using `<param-value>jpa</param-value>`
(_as the default_) refers to the bean to be used in `src/main/resources/spring/business-config.xml`.
    * Setting the `<param-value>` to `jdbc`, `jpa`, or `spring-data-jpa` is all that is needed to change the DAO implementation.

All versions of PetClinic also demonstrate JMX support via the use of `<context:mbean-export/>` in `resources/spring/tools-config.xml`
for exporting MBeans. The `CallMonitoringAspect.java` is exposed using Spring's `@ManagedResource` and `@ManagedOperation`
annotations and with `@Around` annotation we add monitoring around all `org.springframework.stereotype.Repository *` functions.
You can start up the JDK's JConsole to manage the exported bean.

The use of `@Cacheable` is also demonstrated in `ClinicServiceImpl.java` by caching the results of the method `findVets`.
The cacheManager in configured in `tools-config.xml` and `ehcache.xml` specifies the `vets` cache properties.

The default transaction manager for JDBC is DataSourceTransactionManager and for JPA and Spring Data JPA, JpaTransactionManager.
Those local strategies allow for working with any locally defined DataSource. These are defined in the `business-config.xml`

_Note that the sample configurations for JDBC, JPA, and Spring Data JPA configure a DataSource from the Apachce Tomcat JDBC Pool project for connection pooling. See `datasource-config.xml`._

## System Requirements  

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server  

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `spring-petclinic/target/${project.artifactId}.war` to the running instance of the server.

If you do not have maven configured you can manually copy `spring-petclinic/target/${project.artifactId}.war` to ${jboss.home.name}/standalone/deployments.

For MySQL, you need to use the corresponding schema and SQL scripts in the `db/mysql` subdirectory.

In you intend to use a local DataSource, the JDBC settings can be adapted in `src/main/resources/spring/datasource-config.xml`.
To use a JTA DataSource, you need to set up corresponding DataSources in your Java EE container.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

_Note:_ You see the following warning in the server log when you access the application. This example does not provide a `dandelion.properties` file because it does not require any changes to the dandelion default configuration. You can ignore this warning.

    WARN  [com.github.dandelion.core.config.StandardConfigurationLoader] (default task-1) No file "dandelion.properties" was found in "dandelion/dandelion.properties" (classpath). The default configuration will be used.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Arquillian Functional Tests

This quickstart provides Arquillian functional tests as well. They are located in the `functional-tests/` subdirectory under the root directory of this quickstart. Functional tests verify that your application behaves correctly from the user's point of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

NOTE: The arquillian-based functional tests deploy the application, so be sure you have undeployed it before you begin. To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. If the application is still deployed from the previous section, undeploy it now.

        mvn wildfly:undeploy
3. Build the quickstart WAR using the following command:

        mvn clean package

4. Navigate to the functional-tests/ directory in this quickstart.
5. If you have a running instance of the ${product.name} server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-remote

6. If you prefer to run the functional tests using managed instance of the ${product.name} server, meaning the tests will start the server for you, type the following command:

        mvn clean verify -Parq-managed

7. The `spring-petclinic` quickstart contains three configurations: JDBC, JPA, and Spring Data JPA. You should see the tests run 3 times, one for each configuration.

8. Review the server log. You will see an exception for each test configuration run similar to the following in the server log.  This is intentional to demonstrate how exceptions are handled within application. This the same exception you can test by clicking on the `Error` menu item in the upper right corner in the deployed application. The application shows a nice error page in the browser instead of the exception.

        WARN  [warn] (default task-15) Handler execution resulted in exception: java.lang.RuntimeException: Expected: controller used to showcase what happens when an exception is thrown
	        at org.springframework.samples.petclinic.web.CrashController.triggerException(CrashController.java:35)
	        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	        at java.lang.reflect.Method.invoke(Method.java:497)
	        at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:221)
          (remainder of StackTrace removed for readability)


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

Note: Eclipse/JBDS may generate a persistence.xml file in the src/main/resources/META-INF/ directory. In order to avoid errors, delete this file.

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
