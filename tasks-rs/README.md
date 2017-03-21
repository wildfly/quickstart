# tasks-rs: JAX-RS, JPA quickstart

Author: Mike Musgrove  
Level: Intermediate  
Technologies: JPA, JAX-RS  
Summary: The `tasks-rs` quickstart demonstrates how to implement a JAX-RS service that uses JPA persistence.  
Prerequisites: tasks  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `tasks-rs` quickstart demonstrates how to implement a JAX-RS service that uses JPA persistence deployed to ${product.name.full}.

* The client uses HTTP to interact with the service. It builds on the *tasks* quickstart, which provides simple task management with secure login.

* The service interface is implemented using JAX-RS. The SecurityContext JAX-RS annotation is used to inject the security details into each REST method.

The application manages `User` and `Task` JPA entities. A user represents an authenticated principal and is associated with zero or more tasks. Service methods validate that there is an authenticated principal and the first time a principal is seen, a JPA User entity is created to correspond to the principal. JAX-RS annotated methods are provided for associating tasks with this user and for listing and removing tasks.

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in ${product.name} and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Configuration Guide](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) for ${product.name.full}._


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Add an Application User

This quickstart uses secured management interfaces and requires that you create the following application user to access the running application.

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |

To add the application user, open a command prompt and type the following command:

      For Linux:   ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
      For Windows: ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application Resources

Application resources for this quickstart are prefixed with the URL <http://localhost:8080/${project.artifactId}/> and can be accessed by an HTTP client.

* A web browser can be used for methods that accept *GET*.
* Otherwise, you must use cURL or some other command line tool that supports HTTP *POST* and *DELETE* methods.

Below you will find instructions to create, display, and delete tasks.

### Create a Task

To associate a task called `task1` with the user `quickstartUser`, you must authenticate as user `quickstartUser` and send an HTTP *POST* request to the url <http://localhost:8080/${project.artifactId}/tasks/title/task1>.

To issue the *POST* command using cURL, type the following command:

    curl -i -u 'quickstartUser:quickstartPwd1!' -H "Content-Length: 0" -X POST http://localhost:8080/${project.artifactId}/tasks/title/task1

You will see the following response:

    HTTP/1.1 201 Created
    Expires: 0
    Cache-Control: no-cache, no-store, must-revalidate
    X-Powered-By: Undertow/1
    Server: JBoss-EAP/7
    Pragma: no-cache
    Location: http://localhost:8080/${project.artifactId}/tasks/id/1
    Date: Thu, 20 Aug 2015 17:30:24 GMT

This is what happens when the command is issued:

* The `-i` flag tells cURL to print the returned headers.
* The `-u` flag provides the authentication information for the request.
* The `-H` flag adds a header to the outgoing request.
* The `-X` flag tells cURL which HTTP method to use. The HTTP *POST* is used to create resources.
* The `Location` header of the response contains the URI of the resource representing the newly created task.

The final argument to cURL determines the title of the task. Note that this approach is perhaps not very restful but it simplifies this quickstart. A better approach would be to *POST* to `http://localhost:8080/${project.artifactId}/tasks/title` passing the task title in the body of the request.


### Display the XML Representation of a Task

To display the XML representation of the newly created resource, issue a *GET* request on the task URI returned in the `Location` header during the create.

1. To issue a *GET* using a browser, open a browser and access the URI. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/${project.artifactId}/tasks/id/1>
2. To issue a *GET* using cURL, type the following command:

        curl -H "Accept: application/xml" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/${project.artifactId}/tasks/id/1

    The `-H` flag tells the server that the client wishes to accept XML content.

Using either of the above *GET* methods, you should see the following XML:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
       <task id="1" ownerName="quickstartUser">
          <title>task1</title>
       </task>


### Display the XML Representation of all Tasks for a User

To obtain a list of all tasks for user `quickstartUser` in XML format, authenticate as user `quickstartUser` and send an HTTP `GET` request to the resource `tasks` URL.

1. To issue a *GET* using a browser, open a browser and access the following URL. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/${project.artifactId}/tasks/title>

2. To list all tasks associated with the user `quickstartUser` using cURL, type:

        curl -H "Accept: application/xml" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/${project.artifactId}/tasks/title

Using either of the above *GET* methods, you should see the following XML:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection>
        <task id="1" ownerName="quickstartUser">
        <title>task1</title>
        </task>
    </collection>

### Delete a Task

To delete a task, again authenticate as principal `quickstartUser` and send an HTTP *DELETE* request to the URI that represents the task.

To delete the task with id `1`:

    curl -i -u 'quickstartUser:quickstartPwd1!' -X DELETE http://localhost:8080/${project.artifactId}/tasks/id/1

You will see this response:

    HTTP/1.1 204 No Content
    Expires: 0
    Cache-Control: no-cache, no-store, must-revalidate
    X-Powered-By: Undertow/1
    Server: JBoss-EAP/7
    Pragma: no-cache
    Date: Thu, 20 Aug 2015 17:32:39 GMT

Now list all tasks associated with user `quickstartUser`:

    curl -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/${project.artifactId}/tasks/title

You will see a response with an empty collection:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection/>


## Modify this Quickstart to Support JSON Representations of Tasks

JSON is not part of the JAX-RS standard but most JAX-RS implementations do support it. This quickstart can be modified to support JSON by uncommenting a few lines. Look for comment lines containing `JSON:`:

1. Open the `pom.xml` file and remove the comments from the dependency with artifactId `resteasy-jackson2-provider`.

        <!-- JSON: uncomment to include json support (note json is not part of the JAX-RS standard) -->
        <!--
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jackson2-provider</artifactId>
            <scope>provided</scope>
        </dependency>
        -->

2. Open the `src/main/java/org/jboss/as/quickstarts/tasksrs/model/Task.java` file and remove the comments from the following two lines.

        // import com.fasterxml.jackson.annotation.JsonIgnore;

        // @JsonIgnore

3. Open the `src/main/java/org/jboss/as/quickstarts/tasksrs/service/TaskResource.java` file and make sure the *GET* methods produce "application/json" as well as "application/xml". Again, look for lines beginning with `// JSON:`.
    * Remove comments from this line:

            //@Produces({ "application/xml", "application/json" })
    * Add comments to this line:

            @Produces({ "application/xml" })
4. Rebuild and redeploy the quickstart.

5. [Create a Task](#create-a-task) as you did for the XML version of this quickstart.


6. View task resources in JSON media type by specifying the correct *Accept* header. For example, using the cURL tool, type the following command:

        curl -H "Accept: application/json" -u 'quickstartUser:quickstartPwd1!' -X GET http://localhost:8080/${project.artifactId}/tasks/id/1
   You will see the following response:

        {"id":1,"title":"task1","ownerName":"quickstartUser"}


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    WFLYJCA0091: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

Be sure to [Add an Application User](#add-an-application-user) as described above.

_Note:_ When you deploy this quickstart, you see the following error. This is because JBoss Developer Studio automatically attempts to access the URL <http://localhost:8080/${project.artifactId}/>, however, all incoming requests are handled by the REST application. You can ignore this error.

    JBWEB000065: HTTP Status 404 - RESTEASY001185: Could not find resource for relative : / of full path: http://localhost:8080/${project.artifactId}/


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
