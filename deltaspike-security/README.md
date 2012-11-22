jboss-as-deltaspike-projectstage: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike
======================================================
Author: Rafael Benevides
Level: Beginner
Technologies: JSF, CDI, Deltaspike
Summary: Demonstrate the creation of a custom authorization example using @SecurityBindingType from DeltaSpike
Prerequisites: 
Target Product: WFK

What is it?
-----------

SecurityBinding is a feature of the security module that acts by intercepting method calls, and performing a security check before invocation is allowed to proceed.

To use it, it's needed to create a security parameter binding annotation. In this application we created `@AdminAllowed` and `@GuestAllowed` annotations.

The application also defines an `Authorizer` class that implements behavior for both `SecurityBindingType`. This class is simply a CDI bean which declares a @Secures method, qualified with the security binding annotation we created.

This `Authorizer` is integrated with JAAS so the check is delegated to JAAS API through `FacesContext`, but any other ways to check if the method is allowed could be used. 

Both annotations was applied to methods on `SecuredController` class.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.



Add an Application User
----------------
This quickstart uses secured management interfaces and requires that you create an application user to access the running application. Instructions to set up the quickstart application user can be found here: [Add an Application User](../README.md#addapplicationuser)


Start JBoss Enterprise Application Platform 6 or JBoss AS 7
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

        mvn clean package jboss-as:deploy
4. This will deploy `target/jboss-as-deltaspike-security.war` to the running instance of the server.

Access the application
---------------------

Access the running application in a browser at the following URL:  <localhost:8080/jboss-as-deltaspike-security/>

When you try to access the application, you're redirected to a Login form already filled. (remember to setup the Application User).

Log in application and you see the secured page showing your username and two buttons. 

Click on `Guest Method` button and realize that you will see the following message: `You executed a @GuestAllowed method`.

Now, click on `Admin Method` button and you will be redirected to a error page with the following exception: `org.apache.deltaspike.security.api.authorization.AccessDeniedException`
        
Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

