# ejb-throws-exception: Handle Exceptions across JARs in an EAR

Author: Brad Maxwell  
Level: Intermediate  
Technologies: EJB, EAR  
Summary: The `ejb-throws-exception` quickstart demonstrates how to throw and handle Exceptions across JARs in an EAR.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-throws-exception` quickstart extends the [ejb-in-ear](../ejb-in-ear/README.md) quickstart and demonstrates how to handle Exceptions across JARs in an EAR deployed to ${product.name.full}. In this quickstart, an EJB in the EJB JAR throws a custom Exception. The web application in the client JAR catches the Exception and displays it in a nicely formatted message. The EAR contains: *JSF WAR*, an *EJB* JAR and a client library JAR containg classes that both the WAR and EJB JAR use.

The example is composed of three Maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB code and can be built independently to produce the JAR archive.  The EJB has a single method `sayHello` which will take in a String `name` and return `Hello <name>` if the `name` is not null or an empty String.  If the `name` is null or an empty String, then it will throw a custom Exception (`GreeterException`) back to the client.

2. `web`: This project contains the JSF pages and the CDI managed bean.  The CDI Managed Bean (GreeterBean) will be bound to the JSF page (index.xhtml) and will invoke the GreeterEJB and display the response back from the EJB.  The GreeterBean catches the custom Exception (GreeterException) thrown by GreeterEJB and displays the Exception message in the response text on the JSF page.

3. `ear`: This project builds the EAR artifact and pulls in the ejb, web, and client artifacts.

4. `ejb-api`: This project builds the ejb-api library artifact which is used by the ejb, web, as well as remote client artifacts. The ejb-api directory contains the EJB interfaces, custom exceptions the EJB throws and any other transfer objects which the EJB may receive or send back to the client.  The EJB interfaces, custom exceptions, and other transfer objects are split into a separate JAR which is packaged in the ear/lib. This allows all sub deployments of the EAR to see the classes of the ejb-api JAR in the classpath.  This is also useful for remote clients.  The ejb-api JAR can be distributed to a remote client and give the remote clients the classes that are needed to interact with the EJB.

The root `pom.xml` builds each of the subprojects in the above order and deploys the EAR archive to the server.


The example follows the common `Hello World` pattern. These are the steps that occur:

1. A JSF page (http://localhost:8080/${project.artifactId}/) asks for the user name.
2. On clicking `Say Hello`, the value of the `Name` input text is sent to a managed bean named `GreeterBean`.
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected to the managed bean. Notice the field annotated with `@EJB`.
4. The EJB responds with `Hello <name>` or throws an Exception if the name is empty or null.
5. The response or exception's message from invoking the `GreeterEJB` is stored in a field (response) of the managed bean.
6. The managed bean is annotated as `@RequestScoped`, so the same managed bean instance is used only for the request/response.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


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

4. This will deploy `ear/target/${project.artifactId}.ear` to the running instance of the server.


## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

Enter a name in the input field `Name` and click the `Say Hello` button to see the response.

The `Response` output text will display the response from the EJB.
If the `Name` input text box is not empty, then the `Response` output text will display `Hello <name>`
If the `Name` input text box is empty, then the `Response` output text will display the message of the exception throw back from the EJB.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
