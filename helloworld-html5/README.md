# helloworld-html5: HTML5 and REST Hello World Example

Author: Jay Balunas, Burr Sutter, Douglas Campos, Bruno Olivera  
Level: Beginner  
Technologies: CDI, JAX-RS, HTML5  
Summary: The `helloworld-html5` quickstart demonstrates the use of *CDI 1.2* and *JAX-RS 2.0* using the HTML5 architecture and RESTful services on the backend.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `helloworld-html5` quickstart demonstrates the use of *CDI 1.2* and *JAX-RS 2.0* in ${product.name.full} ${product.version} or later using the HTML5 + REST architecture.

The application is basically a smart, HTML5+CSS3+JavaScript front-end using RESTful services on the backend.

 * HelloWorld.java - establishes the RESTful endpoints using JAX-RS
 * Web.xml - maps RESTful endpoints to `/hello`
 * index.html - is a jQuery augmented plain old HTML5 web page

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ is required.

With the prerequisites out of the way, you are ready to build and deploy.


## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy both the client and service applications:

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

You can also test the REST endpoint by sending an HTTP *POST* request to the URLs below. Feel free to replace `YOUR_NAME` with a name of your choosing.

* The *XML* content can be tested by sending an HTTP *POST* to the following URL: <http://localhost:8080/${project.artifactId}/hello/xml/YOUR_NAME>

    To issue the *POST* command using cURL, type the following command in terminal:

        curl -i -X POST http://localhost:8080/${project.artifactId}/hello/xml/YOUR_NAME

    You will see the following response:

        HTTP/1.1 200 OK
        Connection: keep-alive
        X-Powered-By: Undertow/1
        Server: JBoss-EAP/7
        Content-Type: application/xml
        Content-Length: 44
        Date: Tue, 13 Oct 2015 18:40:04 GMT

        <xml><result>Hello YOUR_NAME!</result></xml>

* The *JSON* content can be tested by sending an HTTP *POST* to the following URL: <http://localhost:8080/${project.artifactId}/hello/json/YOUR_NAME>

    To issue the *POST* command using cURL, type the following command in terminal:

        curl -i -X POST http://localhost:8080/${project.artifactId}/hello/json/YOUR_NAME

    You will see the following response:

        HTTP/1.1 200 OK
        Connection: keep-alive
        X-Powered-By: Undertow/1
        Server: JBoss-EAP/7
        Content-Type: application/json
        Content-Length: 29
        Date: Tue, 13 Oct 2015 06:32:20 GMT

        {"result":"Hello YOUR_NAME!"}


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Run the Arquillian Functional Tests

This quickstart provides Arquillian functional tests as well. They are located in the functional-tests/ subdirectory under the root directory of this quickstart.
Functional tests verify that your application behaves correctly from the user's point of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the ${product.name} server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-remote

5. If you prefer to run the functional tests using managed instance of the ${product.name} server, meaning the tests will start the server for you, type fhe following command:

        mvn clean verify -Parq-managed

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
