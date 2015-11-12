helloworld-html5: HTML5 and REST Hello World Example
===================
Author: Jay Balunas, Burr Sutter, Douglas Campos, Bruno Olivera  
Level: Beginner  
Technologies: CDI, JAX-RS, HTML5  
Summary: The `helloworld-html5` quickstart demonstrates the use of *CDI 1.1* and *JAX-RS 2.0* using the HTML5 architecture and RESTful services on the backend.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `helloworld-html5` quickstart demonstrates the use of *CDI 1.1* and *JAX-RS 2.0* in Red Hat JBoss Enterprise Application Platform 7 or later using the HTML5 + REST architecture.

The application is basically a smart, HTML5+CSS3+JavaScript front-end using RESTful services on the backend.

 * HelloWorld.java - establishes the RESTful endpoints using JAX-RS
 * Web.xml - maps RESTful endpoints to "/hello"
 * index.html - is a jQuery augmented plain old HTML5 web page

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ is required.

With the prerequisites out of the way, you're ready to build and deploy.


Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy both the client and service applications:

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-helloworld-html5.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-helloworld-html5/>.

You can also test the REST endpoint as follows. Feel free to replace `YOUR_NAME` with a name of your choosing.

* The *XML* content can be tested by accessing the following URL: <http://localhost:8080/jboss-helloworld-html5/hello/xml/YOUR_NAME> 
* The *JSON* content can be tested by accessing this URL: <http://localhost:8080/jboss-helloworld-html5/hello/json/YOUR_NAME>


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy

Run the Arquillian Functional Tests
-----------------------------------

This quickstart provides Arquillian functional tests as well. They are located in the functional-tests/ subdirectory under the root directory of this quickstart.
Functional tests verify that your application behaves correctly from the user's point of view. The tests open a browser instance, simulate clicking around the page as a normal user would do, and then close the browser instance.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the WildFly server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-wildfly-remote

5. If you prefer to run the functional tests using managed instance of the WildFly server, meaning the tests will start the server for you, type fhe following command:

        mvn clean verify -Parq-wildfly-managed


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->



