# inter-app: Communicate Between Two Applications Using EJB and CDI

Author: Pete Muir  
Level: Advanced  
Technologies: EJB, CDI, JSF  
Summary: The `inter-app` quickstart shows you how to use a shared API JAR and an EJB to provide inter-application communication between two WAR deployments.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `inter-app` quickstart shows you how to easily communicate between two modular deployments to ${product.name.full}. Two WARs, with a shared API JAR, are deployed to the application server. EJB is used to provide inter-application communication, with EJB beans alised to CDI beans, making the inter-application communication transparent to clients of the bean.

CDI only provides intra-applicaion injection (i.e within a top level deployment, EAR, WAR, JAR etc). This improves performance of the application server, as to satisfy an injection point all possible candidates have to be scanned / analyzed. If inter-app injection was supported by CDI, performance would scale according to the number of deployments you have (the more deployments in the running system, the slower the deployment). Java EE injection uses unique JNDI names for the wiring, so each injection point is O(1). The approach shown here combines the two approaches such that you limit the name based wiring to one location in your code, and the main consumers of components can use CDI injection to reference these name wired components. For the name approach to work though, you still need to publish instances, and EJB singletons allow you to do that with just one extra annotation.


In all, the project has three modules:

* `${project.artifactId}-app-shared.jar` - this module contains the interfaces which define the contract between the beans exposed by the WARs. It is deployed as an EJB JAR module because Eclipse Web Tools Platform can not deploy simple JARs.
* `${project.artifactId}-app-appA.war` - the first WAR, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appB
* `${project.artifactId}-app-appB.war` - the second WAR, whiches exposes an EJB singleton, and a simple UI that allows you to read the value set on the bean in appA

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
4. This will deploy `shared/target/${project.artifactId}-app-shared.jar`, `appA/target/${project.artifactId}-app-appA.war` and `appB/target/${project.artifactId}-app-appB.war` to the running instance of the server.


## Access the Application

Access the running application in a browser at the following URLs:

* <http://localhost:8080/${project.artifactId}>
* <http://localhost:8080/${project.artifactId}>

You are presented with a form that allows you to set the value on the bean in the other application, as well as display of the value on this application's bean. Enter a new value and press `Update and Send!` to update the value on the other application. Do the same on the other application, and hit the button again on the first application. You should see the values shared between the applications.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn package wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart consists of multiple projects containing interdependencies on each other, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. In the `Servers` tab, right-click on the ${product.name} server and choose `Start`.
2. Deploy the projects in one of the following ways.
   * `Drag and Drop` mode: Click to multi-select the `${project.artifactId}-app-shared`, `${project.artifactId}-app-appA`, and `${project.artifactId}-app-appB` projects, then drag and drop them on the running ${product.name} server. This deploys the projects to the server without opening the browser.
   * `Batch` mode: In the `Servers` tab, right-click on the server and choose `Add and Remove`. If the `${project.artifactId}-app-shared`, `${project.artifactId}-app-appA`, and `${project.artifactId}-app-appB` projects are the only projects in the list, click `Add All`. Otherwise, use multi-select to select them and click `Add`. Then click `Finish`.
3. Right-click on the `${project.artifactId}-app-appA` project and choose `Run As` --> `Run on Server`. A browser window appears that accesses the running `appA` application.
4. Right-click on the `${project.artifactId}-app-appB` project and choose `Run As` --> `Run on Server`. A browser window appears that accesses the running `appB` application.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
