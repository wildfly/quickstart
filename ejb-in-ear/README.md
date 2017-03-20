# ejb-in-ear: Deployment of an EAR Containing a JSF WAR and EJB JAR

Author: Paul Robinson  
Level: Intermediate  
Technologies: EJB, EAR  
Summary: The `ejb-in-ear` quickstart demonstrates how to deploy an EAR archive that contains a *JSF* WAR and an *EJB* JAR.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-in-ear` quickstart demonstrates the deployment of an EAR artifact to ${product.name.full}. The EAR contains: *JSF* WAR and an *EJB* JAR.

The example is composed of three Maven projects, each with a shared parent. The projects are as follows:

1. `ejb`: This project contains the EJB code and can be built independently to produce the JAR archive.

2. `web`: This project contains the JSF pages and the managed bean.

3. `ear`: This project builds the EAR artifact and pulls in the EJB and Web artifacts.

The root `pom.xml` builds each of the subprojects in the above order and deploys the EAR archive to the server.

The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking _Greet_, the name is sent to a managed bean named `Greeter`.
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected to the managed bean. Notice the field annotated with `@EJB`.
4. The response from invoking the `GreeterEJB` is stored in a field (message) of the managed bean.
5. The managed bean is annotated as `@SessionScoped`, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is displayed to the user.

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

Enter a name in the input field and click the _Greet_ button to see the response.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

For this quickstart, follow the special instructions to build [Quickstarts Containing an EAR](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#quickstarts-containing-an-ear)

1. Right-click on the `${project.artifactId}` subproject, and choose `Run As` --> `Run on Server`.
2. Choose the server and click `Finish`.
3. This starts the server, deploys the application, and opens a browser window that accesses the running application at <http://localhost:8080/ejb-in-ear>.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
