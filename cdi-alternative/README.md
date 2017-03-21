# cdi-alternative: Demonstrates CDI Alternatives

Author: Nevin Zhu  
Level: Intermediate  
Technologies: CDI, Servlet, JSP  
Summary: The `cdi-alternative` quickstart demonstrates how to create a bean that can be implemented for different purposes without changing the source code.   
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `cdi-alternative` quickstart demonstrates how to create beans that can be implemented for different purposes in ${product.name.full} without changing the Java source code. Instead, you define the default and alternative (`@Alternative`) bean implementations during the development phase. Then at deployment time, rather than modifying the source code, you can choose to deploy the default or alternative beans by modifying the `<alternatives>` element in the `WEB-INF/beans.xml` file.

Alternatives can be used to customize deployments for specific situations, to handle client-side business logic that is determined at runtime, and to create dummy beans to be used for test purposes.

This quickstart demonstrates how to deploy an example with alternative sales tax rates. It defines the following classes, interfaces, and `WEB-INF/beans.xml` files:

* `Demo`: This class extends `HttpServlet` and handles the incoming servlet request. It gets the tax rate and returns it to the page.
* `Tax`: This interface defines the `getRate()` method to get the tax rate.
* `TaxImpl_1`: This is the default class that returns the `Tax_1` rate.
* `TaxImpl_2`: This is an alternative class that returns the `Tax_2` rate. Note the `@Alternative` annotation in the class.
* `WEB-INF/beans.xml`: This file specifies the `TaxImpl_2` alternative should be used by the quickstart. To use the default `TaxImpl_1` class, delete or comment out the `<alternatives>` element and redeploy the quickstart.
* `result.jsp`: The JSP page that displays the tax rate.


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

4. This deploys `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

You can specify alternative versions of the bean in the `WEB-INF/beans.xml` file by doing one of the following:

1. You can remove the `<alternatives>` tag so that it defaults to use `TaxImpl_1`.
2. You can create another alternative bean class and use that class name as an alternative.

In this quickstart, in order to switch back to the default implementation,
comment the `<alternatives>` block in the `WEB-INF/beans.xml` file and redeploy the quickstart.

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
