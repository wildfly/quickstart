ejb-in-war: Deployment of a WAR Containing an EJB
=================================================
Author: Paul Robinson 

What is it?
-----------

This example demonstrates the deployment of an *EJB 3.1* bean bundled in a war archive for deployment to *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.

The example follows the common "Hello World" pattern. These are the steps that occur:

1. A JSF page asks the user for their name.
2. On clicking submit, the name is sent to a managed bean namde `Greeter`.
3. On setting the name, the `Greeter` invokes the `GreeterEJB`, which was injected into the managed bean. Notice the field annotated with `@EJB`.
4. The response from invoking the `GreeterEJB` is stored in a field `message` of the managed bean.
5. The managed bean is annotated as `@SessionScoped`, so the same managed bean instance is used for the entire session. This ensures that the message is available when the page reloads and is displayed to the user.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 


Configure Maven 
-------------

If you have not yet done so, you must [Configure Maven](../README.html/#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

         For Linux:   JBOSS_HOME/bin/standalone.sh
         For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.html/#buildanddeploy) for complete instructions and additional options._


#### Build and Deploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

            mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-ejb-in-war.war` to the running instance of the server.

#### Undeploy the Archive

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to undeploy the archive:

            mvn jboss-as:undeploy
 

Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-as-ejb-in-war>.



Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.html/#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

      mvn dependency:sources
      mvn dependency:resolve -Dclassifier=javadoc

