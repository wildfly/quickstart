# cdi-decorator: Demonstrates CDI Decorator

Author: Ievgen Shulga  
Level: Intermediate  
Technologies: CDI, JSF  
Summary: The `cdi-decorator` quickstart demonstrates the use of a CDI Decorator to intercept bean methods and modify the business logic.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `cdi-decorator` quickstart demonstrates the use of CDI decorators in ${product.name.full}.
A decorator implements one or more bean types and intercepts business method invocations of beans which implement those bean types.
These bean types are called decorated types.

Decorators are similar to interceptors, but because they directly implement operations with business semantics, they are able to implement business logic and, conversely, unable to implement the cross-cutting concerns for which interceptors are optimized.

Decorators may be associated with any managed bean that is not itself an interceptor or decorator or with any EJB session bean.
A decorator instance is a dependent object of the object it decorates.

This example represents a common decorator design pattern. We take a class and we wrap decorator class around it.
When we call the class, we always pass through the surrounding decorator class before we reach the inner class.
In this example, the decorator class simply changes the staff bonus from `100` to `200` and the staff position from `Java Developer` to `Team Lead`.
It then logs a message to the server console.

By default, all decorators are disabled, so the application will run without using decorator. We need to enable our decorator in the `WEB-INF/beans.xml` descriptor to make it work.


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

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}>.

You can specify decorator of the bean in the `WEB-INF/beans.xml` file by doing one of the following:

1. You can add a decorators tag and specify a decorator class.
2. You can specify a different decorator class name in the decorators tag.

For this example, uncomment the `<decorators>` tag in the `WEB-INF/beans.xml` file and redeploy the application. When you access the application, you will see changed information from web-browser and following in the server log:

    CDI decorator method was called!
    CDI decorator method was called!

The message appears twice because the decorator is called twice, once to get the staff position and then again to get the staff bonus.

In order to switch back to the default implementation, comment the `decorators` block in the `WEB-INF/beans.xml` file and redeploy the quickstart.

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
