deltaspike-authorization: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike
======================================================
Author: Rafael Benevides  
Level: Beginner  
Technologies: JSF, CDI, Deltaspike  
Summary: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike  
Prerequisites: 
Target Product: ${product.name}  
Source: <https://github.com/jboss-jdf/jboss-as-quickstart/>  

What is it?
-----------

Security binding is DeltaSpike feature that restricts who can invoke a method (under the covers, it uses interceptors).

To restrict who can invoke a method, we create an annotation, called a security binding type. This quickstart has two security binding types - `@AdminAllowed` and `@GuestAllowed`.

The quickstart defines an `Authorizer` class that implements the restrictions for the security binding types. The authorizer is a CDI bean which defines methods (annotated with `@Secures) which perform the authorization checks for each security binding we create.

In this quickstart the `Authorizer` we delegate authentication to JAAS, but other authentication solutions could be used.

Methods on the `Controller` bean have been restricted using the security binding types.


System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on WildFly 10.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#addapplicationuser)


Start WildFly 10
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
4. This will deploy `target/wildfly-as-deltaspike-authorization.war` to the running instance of the server.


Access the application
---------------------

You can access the running application in a browser at the following URL: <localhost:8080/wildfly-deltaspike-authorization/>

When you access the application you are redirected to a login form, already filled in with the details of the application user you set up above. Once you have logged into the application you see a page showing your username and two buttons. 

When you click on the `Employee Method` button you will see the following message: `You executed a @EmployeeAllowed method` - you are authorized to invoke this method.

When you click on the `Admin Method` button you will be redirected to a error page with the following exception: `org.apache.deltaspike.security.api.authorization.AccessDeniedException` - you aren't authorized to invole thos method.
        
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

