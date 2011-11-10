jboss-as-helloworld-ssb Example
===============================

What is it?
-----------

This quickstart demonstrates the use of *EJB 3.1 Singleton Session Bean* in JBoss AS 7.1.0.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1.0 or EAP 6. 
 
With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

First of all you need to enable the "admin" user from $JBOSS_HOME/standalone/configuration/mgmt-users.properties file, and then start JBoss AS 7 (or EAP 6). To do this, run this script
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact to JBoss AS by executing the following command:

    mvn jboss-as:deploy

This will deploy `target/jboss-as-helloworld-ssb.war`.
 
The application will be running at the following URL <http://localhost:8080/jboss-as-helloworld-ssb>.

This example demonstrates a singleton session bean that maintains state information for 2 variables: "Increment A" and "Increment B". A counter is incremented when you click on the link to the variable name. If you close and restart your browser, or if you have multiple browsers, you can see that the counter always increments the last value. These values are maintained until you restart the server. To test the singleton bean, on the home page, click on either "Increment A" or "Increment B". The result page will give you the current value of the variable.

To undeploy from JBoss AS, run this command:

    mvn jboss-as:undeploy

You can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

