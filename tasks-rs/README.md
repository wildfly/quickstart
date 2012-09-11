tasks-rs: JAX-RS, JPA quickstart
==============================
Author: Mike Musgrove
Level: Intermediate
Technologies: JPA, JAX-RS
Summary: Demonstrates how to use JAX-RS and JPA together
Prerequisites: tasks
Target Product: EAP

What is it?
-----------

This project demonstrates how to implement a JAX-RS service that uses JPA 2.0 persistence.

* The client uses HTTP to interact with the service. It builds on the *tasks* quickstarts which provide simple Task management with secure login.

* The service interface is implemented using JAX-RS. The SecurityContext JAX-RS annotation is used to inject the security details into each REST method.

The application manages User and Task JPA entities. A user represents an authenticated principal and is associated with zero or more Tasks. Service methods validate that there is an authenticated principal and the first time a principal is seen, a JPA User entity is created to correspond to the principal. JAX-RS annotated methods are provided for associating Tasks with this User and for listing and removing Tasks.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.


Configure Maven
-------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.



Add an Application User
---------------

This quickstart uses a secured management interface and requires that you create an application user to access the running application. Instructions to set up an Application user can be found here:  [Add an Application User](../README.md#addapplicationuser).  After following these instructions. you should have created a user called `quickstartUser` with password `quickstartPassword`, belonging to the `guest` role.


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

4. This will deploy `target/jboss-as-tasks-rs.war` to the running instance of the server.


Access the Application Resources
---------------------

Application resources for this quickstart are prefixed with the URL http://localhost:8080/jboss-as-tasks-rs/ and can be accessed by an HTTP client.

* For methods that accept *GET*, a web browser can be used.
* Otherwise, you must use cURL or some other command line tool that supports HTTP *POST* and *DELETE* methods.

Below you will find instructions to create, display, and delete tasks.

<a id="create-a-task"></a>
### Create a Task

To associate a task called `task1` with the user `quickstartUser`, you must authenticate as user `quickstartUser` and send an HTTP *POST* request to the url 'http://localhost:8080/jboss-as-tasks-rs/tasks/task1'.

To issue the *POST* command using cURL, type the following command:

    curl -i -u "quickstartUser:quickstartPassword" -H "Content-Length: 0" -X POST http://localhost:8080/jboss-as-tasks-rs/tasks/task1

You will see the following response:

    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    Location: http://localhost:8080/jboss-as-tasks-rs/tasks/1
    Content-Length: 0
    Date: Sun, 15 Apr 2012 22:46:26 GMT

This is what happens when the command is issued:

* The `-i` flag tells cURL to print the returned headers. Notice that the `Location` header contains the URI of the resource corresponding to the new task you have just created.
* The `-u` flag provides the authentication information for the request.
* The `-H` flag adds a header to the outgoing request.
* The `-X` flag tells cURL which HTTP method to use. The HTTP *POST* is used to create resources.
* The `Location` header of the response contains the URI of the resource representing the newly created task.

The final argument to cURL determines the title of the task. Note that this approach is perhaps not very restful but it simplifies this quickstart. A better approach would be to *POST* to "http://localhost:8080/jboss-as-tasks-rs/tasks" passing the task title in the body of the request.


### Display the XML Representation of a Task

To display the XML representation of the newly created resource, issue a *GET* request on the task URI returned in the `Location` header during the create.

1. To issue a *GET* using a browser, open a browser and access the URI. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/jboss-as-tasks-rs/tasks/1>
2. To issue a *GET* using cURL, type the following command:

    `curl -H "Accept: application/xml" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks/1`

    The `-H flag tells the server that the client wishes to accept XML content.

Using either of the above *GET* methods, you should see the following XML:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <task id="1" ownerName="quickstartUser">
        <title>task1</title>
    </task>


### Display the XML Representation of all Tasks for a User

To obtain a list of all tasks for user `quickstartUser` in XML format, authenticate as user `quickstartUser` and send an HTTP `GET` request to the resource `tasks` URL.

1. To issue a *GET* using a browser, open a browser and access the following URL. You will be challenged to enter valid authentication credentials.

    <http://localhost:8080/jboss-as-tasks-rs/tasks>

2. To list all tasks associated with the user `quickstartUser` using cURL, type:

    curl -H "Accept: application/xml" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks

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

    curl -i -u "quickstartUser:quickstartPassword" -X DELETE http://localhost:8080/jboss-as-tasks-rs/tasks/1

You will see this response:

    HTTP/1.1 204 No Content
    Server: Apache-Coyote/1.1
    Pragma: No-cache
    Cache-Control: no-cache
    Expires: Thu, 01 Jan 1970 01:00:00 GMT
    Date: Sun, 15 Apr 2012 22:51:56 GMT

Now list all tasks associated with user `quickstartUser`:

    curl -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks

You will see a response with an empty collection:

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection/>


Modify this Quickstart to Support JSON Representations of Tasks
-----------------------------------------------------------------

JSON is not part of the JAX-RS standard but most JAX-RS implementations do support it. This quickstart can be modified to support JSON by uncommenting a few lines. Look for lines beginning with "// JSON:":

1. Open the file src/org/jboss/as/quickstarts/tasksrs/model/Task.java and remove the comments from the following two lines.

        // import org.codehaus.jackson.annotate.JsonIgnore;

        // @JsonIgnore

2. Open the file src/org/jboss/as/quickstarts/tasksrs/service/TaskResource.java and make sure the *GET* methods produce "application/json" as well as "application/xml". Again, look for lines beginning with "// JSON:".
    * Remove comments from these lines:

        //@Produces({ "application/xml", "application/json" })
    * Add comments to these lines:

        @Produces({ "application/xml" })
3. Open pom.xml and remove the comments from the dependency with artifactId `resteasy-jackson-provider`

        <!--
        <dependency>
            <groupId>org.jboss.resteasy</groupId>
            <artifactId>resteasy-jackson-provider</artifactId>
            <version>2.3.1.GA</version>
            <scope>provided</scope>
        </dependency>
        -->

4. [Create a Task](#create-a-task) as you did in the for the XML version of this quickstart.
5. Rebuild and redeploy the quickstart.


Now you can view task resources in JSON media type by specifying the correct Accept header. For example, using the cURL tool, type the following command:

    curl -H "Accept: application/json" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks/1

You will see the following response:

    {"id":1,"title":"task1","ownerName":"quickstartUser"}


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
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
