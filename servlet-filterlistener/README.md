# servlet-filterlistener: How to Write Servlet Filters and Listeners

Author: Jonathan Fuerth  
Level: Intermediate  
Technologies: Servlet Filter, Servlet Listener  
Summary: The `servlet-filterlistener` quickstart demonstrates how to use Servlet filters and listeners in an application.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `servlet-filterlistener` quickstart demonstrates how to use Servlet filters and listeners in ${product.name.full}.

This example contains the following classes:

* `FilterExampleServlet`: A simple servlet that prints a form that contains an input field and a *Send* button.
* `VowelRemoverFilter`: A servlet filter that removes the vowels from the text entered in the form.
* `ParameterDumpingRequestListener`: A simple servlet request listener that creates a map of the original HTTP request parameter values before the filter is applied.


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

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

You are presented with a form containing an input field and a *Send* button. To test the quickstart:

1. Enter some text in the field, for example:

        This is only a test!
2. Click *Send*.
3. The servlet filter intercepts and removes the vowels from the text entered in the form and displays:

        You Typed: Ths s nly tst!
4. The server console displays the following log messages.

   The following messages appear in the log when you access the application URL. This is because the ParameterDumpingRequestListener, which is a ServletRequestListener, and the VowelRemoverFilter both map to the application context and print logs for every request. Note that the VowelRemoveFilter contains logic to handle empty input field arguments.

        INFO  [io.undertow.servlet] (default task-46) ParameterDumpingRequestListener: request has been initialized. It has 0 parameters:
        INFO  [io.undertow.servlet] (default task-46) VowelRemoverFilter initialized
        INFO  [io.undertow.servlet] (default task-46) VowelRemoverFilter invoking filter chain...
        INFO  [io.undertow.servlet] (default task-46) VowelRemoverFilter done filtering request
        INFO  [io.undertow.servlet] (default task-46) ParameterDumpingRequestListener: request has been destroyed
        INFO  [io.undertow.servlet] (default task-48) ParameterDumpingRequestListener: request has been initialized. It has 0 parameters:
        INFO  [io.undertow.servlet] (default task-48) VowelRemoverFilter invoking filter chain...
        INFO  [io.undertow.servlet] (default task-48) VowelRemoverFilter done filtering request
        INFO  [io.undertow.servlet] (default task-48) ParameterDumpingRequestListener: request has been destroyed


    The following messages appear in the log when you type "This is only a test!" in the input field and click `Send`.

        INFO  [io.undertow.servlet] (default task-50) ParameterDumpingRequestListener: request has been initialized. It has 1 parameters:
        INFO  [io.undertow.servlet] (default task-50)   userInput=This is only a test!
        INFO  [io.undertow.servlet] (default task-50) VowelRemoverFilter invoking filter chain...
        INFO  [io.undertow.servlet] (default task-50) VowelRemoverFilter done filtering request
        INFO  [io.undertow.servlet] (default task-50) ParameterDumpingRequestListener: request has been destroyed

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
