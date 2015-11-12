shrinkwrap-resolver: Demonstrates Usage of Shrinkwrap Resolver
==============================================================
Author: Rafael Benevides  
Level: Intermediate  
Technologies: CDI, Arquillian, Shrinkwrap  
Summary: The `shrinkwrap-resolver` quickstart demonstrates 3 common use cases for ShrinkWrap Resolver.   
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  


What is it?
-----------

This quickstart demonstrates the use of ShrinkWrap Resolver in Red Hat JBoss Enterprise Application Platform.

With the advent of Maven and other build systems, typically thirdparty libraries and our own dependent modules are obtained from a backing software repository. In this case we supply a series of coordinates which uniquely identifies an artifact in the repository, and resolve the target files from there.

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
  - resolve an artifact with classifer
  - disabling Maven Central
  - loading settings.xml from file (with custom repository)
  - return resolution results as a java.io.File array


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start the WildFly Server
-------------------------

1. Open a command line and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote

_NOTE: If you use the Maven settings command line argument with this quickstart, you need to pass an additional argument to allow ShrinkWrap Resolver to function properly:_

    mvn clean test -Parq-wildfly-remote -s /path/to/custom/settings.xml -Dorg.apache.maven.user-settings=/path/to/custom/settings.xml


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

Basically, you first set the active Maven profile in project properties to be either 'arq-wildfly-managed' for running on managed server or 'arq-wildfly-remote' for running on remote server. Then, to run the tests, right click on the project or individual classes and select Run As --> JUnit Test in the context menu.


Investigate the Console Output
------------------------------


### Maven
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


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

