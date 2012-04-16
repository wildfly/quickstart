tasks-rs: JAX-RS, JPA quickstart
==============================

Author: [Mike Musgrove](https://community.jboss.org/people/mmusgrov)


What is it?
-----------

This project demonstrates how to implement a JAX-RS service that uses JPA 2.0 persistence.
The client uses HTTP to interact with the service. It builds on the *tasks* quickstarts which
provide simple Task management with secure log in.

The service interface is implemented using JAX-RS. The SecurityContext JAX-RS annotation
is used to inject the security details into each REST method.

The application manages User and Task JPA entities. A user represents an authenticated principal and is associated with zero or more Tasks. Service methods validate that there is an authenticated principal and the first
time a principal is seen a JPA User entity is created to correspond to the principal. JAX-RS methods are provided for associating Tasks with this User and for listing and removing Tasks.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.


Configure Maven
-------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add At Least One Application User
---------------

This quickstart uses a secured management interface and requires that you create an application user to access the running application. Instructions to set up an Application user can be found here:  [Add an Application User](../README.md#addapplicationuser). After following those instructions you should have created a user
called quickstartUser with password quickstartUser (and belonging to the guest role).


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


Access application resources
---------------------

Application resources are prefixed with the URL http://localhost:8080/jboss-as-tasks-rs/ and
can be accessed by an HTTP client. For methods that accept GET a web browser can be used otherwise
use a command line tool that supports HTTP POST and DELETE methods (for example curl) must be used.

To associate a task called task1 with the user quickstartUser, authenticate as user quickstartUser
and send an HTTP POST request to the url

    http://localhost:8080/jboss-as-tasks-rs/task/task1

The "Location" header of the response contains the URI of the resource representing the newly created
task. To delete this task, again authenticate as pricipal quickstartUser whilst sending an HTTP DELETE
request to this task URI.  Similary, to get an XML representation of the resource just created issue
a GET request on the task URI.

To obtain a list all resources for user quickstartUser in XML format, authenticate as user quickstartUser and send an HTTP GET request (with accept header application/xml) to the url

    http://localhost:8080/jboss-as-tasks-rs/tasks

For example, if you type the previous url into a browser then you will be challenged to enter valid authentication credentials and if successful the browser will display an XML document containing all the tasks belonging to the user that was just validated.

Here are some example commands to achieve the above using a tool called curl:

Create a task with title task1 and associate it with user quickstartUser:

    curl -i -u "quickstartUser:quickstartPassword" -X POST http://localhost:8080/jboss-as-tasks-rs/task/task1
    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    Location: http://localhost:8080/jboss-as-tasks-rs/task/1
    Content-Length: 0
    Date: Sun, 15 Apr 2012 22:46:26 GMT

The -i flag tells curl to print the returned headers. Notice that the Location header contains the URI of the resource corresponding to the new Task you have just created.

The -u flag provides the authentication information for the request.

The -X flag tells curl which HTTP method to use (POST is used to create resources).

The final argument to curl determines the title of the task. An alternative (and more restful) approach would be to POST to http://localhost:8080/jboss-as-tasks-rs/task passing the task title in the body of the request. Certainly a real task would contain more that just a title so it would make sense to include the title in the body.

To see an XML representation of the task perform a GET on URI that was returned in the Location header:

    curl --header "Accept: application/xml" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/task/1
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><task id="1" ownerName="quickstartUser"><title>task1</title></task>

The --header flag tells the server that the client wishes to accept XML content.

To list all tasks associated with the user quickstartUser type:

    curl --header "Accept: application/xml" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection><task id="1" ownerName="quickstartUser"><title>task1</title></task></collection>

To delete the task with id 1:

    curl -i -u "quickstartUser:quickstartPassword" -X DELETE http://localhost:8080/jboss-as-tasks-rs/task/1
    HTTP/1.1 204 No Content
    Server: Apache-Coyote/1.1
    Pragma: No-cache
    Cache-Control: no-cache
    Expires: Thu, 01 Jan 1970 01:00:00 GMT
    Date: Sun, 15 Apr 2012 22:51:56 GMT

Now if you list all tasks associated with user quickstartUser you should get back an empty collection:

    curl -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/tasks
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection/>

Modifying the quickstart to support JSON representations of tasks
-----------------------------------------------------------------

JSON is not part of the JAX-RS standard but most JAX-RS implementations do support it. The quickstart
can be modified to suport JSON by uncommenting a few lines (look for lines beginning with "// JSON:":

1. Open the file src/org/jboss/as/quickstarts/tasksrs/model/Task.java and uncomment the two lines

    import org.codehaus.jackson.annotate.JsonIgnore;

    @JsonIgnore

2. Open the file src/org/jboss/as/quickstarts/tasksrs/service/TaskResource.java and make sure the GET
methods produce "application/json" (look for lines beginning with "// JSON:")

3. Open pom.xml and uncomment the dependency with artifactId resteasy-jackson-provider

Rebuild and redeploy the quickstart.

Now you can view task resources in JSON media type by specifying the correct Accept header. For example
using the curl tool:

    curl --header "Accept: application/json" -u "quickstartUser:quickstartPassword" -X GET http://localhost:8080/jboss-as-tasks-rs/task/1
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
