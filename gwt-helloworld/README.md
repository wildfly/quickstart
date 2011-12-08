GWT Hello World Example
=======================

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7* with a GWT front-end client.
GWT is basically a typesafe, statically checked programming model for producing HTML5+CSS3+JavaScript
front-ends. In this example, we use RESTful services on the backend.

You can test the REST endpoint at the URL http://localhost:8080/jboss-as-gwt-helloworld/hello/json/David


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7. 
 
NOTE:
This project retrieves artifacts from the JBoss Community Maven repository, a
superset of the Maven central repository.

With the prerequisites out of the way, you're ready to build and deploy.


Deploying the Application
-------------------------
 
First you need to start JBoss AS 7. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn clean package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-gwt-helloworld.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-gwt-helloworld/>.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.


Running the Application in GWT Dev Mode
---------------------------------------

GWT Dev Mode provides an edit-save-refresh development experience. If you plan to try 
modifying this demo, we recommend you start the application in Dev Mode so you don't 
have to repackage the whole application every time you change it.

Deploy the war file and start JBoss AS 7 as described above.

Then execute the command:

    mvn gwt:run
