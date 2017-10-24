# shrinkwrap-resolver: Demonstrates Usage of Shrinkwrap Resolver

Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, Arquillian, Shrinkwrap  
Summary: The `shrinkwrap-resolver` quickstart demonstrates three common use cases for ShrinkWrap Resolver in ${product.name.full}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

This quickstart demonstrates the use of ShrinkWrap Resolver in ${product.name.full}.

With the advent of Maven and other build systems, typically third party libraries and our own dependent modules are obtained from a backing software repository. In this case we supply a series of coordinates which uniquely identifies an artifact in the repository, and resolve the target files from there.

That is precisely the aim of the ShrinkWrap Resolver project; it is a Java API to obtain artifacts from a repository system.

The `shrinkwrap-resolver` quickstart demonstrates various use cases for ShrinkWrap Resolver. This Quickstart has 3 Test Classes that demonstrates the following Shrinkwrap use cases:

* ShrinkwrapResolveGAVWithoutTransitiveDepsTest
  - resolve an artifact via G:A:V without transitive dependencies
  - return resolution result as single java.io.File

* ShrinkwrapImportFromPomTest
  - loading pom.xml from file activating and deactivating profiles
  - importing dependencies of specified scope into list of artifacts to be resolved
  - return resolution results as a java.io.File array

* ShrinkwrapResolveGAPCVCustomRepoWithoutCentralTest
  - resolve an artifact via G:A:P:C:V without transitive dependencies
  - resolve an artifact with classifier
  - disabling Maven Central
  - loading settings.xml from file (with custom repository)
  - return resolution results as a java.io.File array


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat

## Run the Arquillian Tests

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean verify -Parq-remote

You can also let Arquillian manage the ${product.name} server by using the `arq-managed` profile. For more information about how to run the Arquillian tests, see [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests).


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

You must first set the active Maven profile in project properties to be either `arq-managed` for running on managed server or `arq-remote` for running on remote server. Then, to run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


## Investigate the Console Output

When you run the tests, you are presented with the test report summary:

    -------------------------------------------------------
     T E S T S
    -------------------------------------------------------
    Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapImportFromPomTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.633 sec
    Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapResolveGAPCVCustomRepoWithoutCentralTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.439 sec
    Running org.jboss.as.quickstarts.shrinkwrap.resolver.ShrinkwrapResolveGAVWithoutTransitiveDepsTest
    Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.016 sec

    Results :

    Tests run: 3, Failures: 0, Errors: 0, Skipped: 0


## Debug the Application

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
