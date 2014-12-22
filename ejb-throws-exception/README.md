ejb-throws-exception: Deployment of an EAR Containing a JSF WAR and an EJB JAR that throws a custom exception
====================================================================
Author: Brad Maxwell

What is it?
-----------
This example extends the `ejb-in-ear` quickstart and demonstrates how to handle Exceptions across JARs in an EAR. In this quickstart, an EJB in the EJB JAR throws a custom Exception. The web application in the client JAR catches the Exception and displays it in a nicely formatted message. The EAR contains: *JSF 2.2 WAR*, an *EJB 3.2* JAR and a client library JAR containg classes that both the WAR and EJB JAR use.

The example is composed of three Maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB code and can be built independently to produce the JAR archive.  The EJB has a single method `sayHello` which will take in a String `name` and return "Hello <name>" if the `name` is not null or an empty String.  If the `name` is null or an empty String, then it will throw a custom Exception (`GreeterException`) back to the client.

2. `web`: This project contains the JSF pages and the CDI managed bean.  The CDI Managed Bean (GreeterBean) will be bound to the JSF page (index.xhtml) and will invoke the GreeterEJB and display the response back from the EJB.  The GreeterBean catches the custom Exception (GreeterException) thrown by GreeterEJB and displays the Exception message in the response text on the JSF page.

3. `ear`: This project builds the EAR artifact and pulls in the ejb, web, and client artifacts.

4. `ejb-api`: This project builds the ejb-api library artifact which is used by the ejb, web, as well as remote client artifacts. The ejb-api directory contains the EJB interfaces, custom exceptions the EJB throws and any other transfer objects which the EJB may receive or send back to the client.  The EJB interfaces, custom exceptions, and other transfer objects are split into a separate jar which is packaged in the ear/lib. This allows all sub deployments of the ear to see the classes of the ejb-api jar in their classpath.  This is also useful for remote clients.  The ejb-api jar can be distributed to a remote client and give the remote clients the classes that are needed to interact with the EJB

The root `pom.xml` builds each of the subprojects in the above order and deploys the EAR archive to the server.


The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page (http://localhost:8080/wildfly-ejb-throws-exception-web/) asks for the user name.
2. On clicking `Say Hello`, the value of the `Name` input text is sent to a managed bean named `GreeterBean`.
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected to the managed bean. Notice the field annotated with `@EJB`.
4. The EJB responds with `Hello <name>` or throws an Exception if the name is empty or null.
5. The response or exception's message from invoking the `GreeterEJB` is stored in a field (response) of the managed bean.
6. The managed bean is annotated as `@RequestScoped`, so the same managed bean instance is used only for the request/response. 

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.


Configure Maven 
-------------

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

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-ejb-in-ear.ear` to the running instance of the server.

 

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/wildfly-ejb-throws-exception-web/>.

Enter a name in the input field `Name` and click the `Say Hello` button to see the response.

The `Response` output text will display the response from the EJB.
If the `Name` input text box is not empty, then the `Response` output text will display `Hello <name>`
If the `Name` input text box is empty, then the `Response` output text will display the message of the exception throw back from the EJB.


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
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
