helloworld-servlet: Demonstrates some of the more frequently used parts of the Servlet API
==========================================================================================
Author: Kylin Soong
Level: Beginner
Technologies: Servlet
Summary: Demonstrates Servlet API
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

This is a collection of examples which demonstrate some of the more frequently used parts of the Servlet API.

These examples will only work when viewed via an http URL.

Wherever you see a form, enter some data and see how the servlet reacts. When playing with the Cookie and Session Examples, jump back to the Headers Example to see exactly what your browser is sending the server.

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
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

        mvn clean package wildfly:deploy

4. This will deploy `target/wildfly-servlet-helloworld.war` to the running instance of the server.


Access the application 
---------------------

Using the URL to test relevant Servlet API:

| **URL** | **Notes** |
|:--------|:----------|
|<http://localhost:8080/helloworld-servlet/HelloWorld>   |Demonstrates basic Servlet API |
|<http://localhost:8080/helloworld-servlet/RequestInfo>  |Demonstrates how to get information via `HttpServletRequest` |
|<http://localhost:8080/helloworld-servlet/RequestHeader>|Demonstrates the content of HTTP Request Header |
|<http://localhost:8080/helloworld-servlet/RequestParam> |Demonstrates how to pass parameters via `HttpServletRequest` |
|<http://localhost:8080/helloworld-servlet/Cookie>       |Demonstrates the `Cookie` API, also how set cookie to a specific request |
|<http://localhost:8080/helloworld-servlet/Session>      |Demonstrates the `HttpSession` API, also how set attributes  to a specific Session |

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
