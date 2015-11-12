jaxws-pojo: An POJO JAX-WS Web Service
==================================================
Author: R Searls  
Level: Beginner  
Technologies: JAX-WS  
Summary: The `jaxws-pojo` quickstart is a working example of the web service endpoint created from a POJO.  
Target Product: WildFly    
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `jaxws-pojo` quickstart demonstrates the use of *JAX-WS* in Red Hat JBoss Enterprise Application Platform as a simple POJO web service application.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Start the WildFly Server
----------------------         

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfy:deploy

4. This will deploy `service/target/wildfly-jaxws-pojo-service.war` to the running instance of the server.

Access the application 
---------------------

You can check that the Web Service is running and deployed correctly by accessing the following URL: <http://localhost:8080/wildfly-jaxws-pojo-endpoint/JSEBean01?wsdl>. This URL will display the deployed WSDL endpoint for the Web Service.

Run the Client
--------------
1. Make sure the service deployed properly.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to run the client.

        java -jar client/target/wildfly-jaxws-pojo-client.jar   org.jboss.quickstarts.ws.client.Client
4. You should see the following response.

        JSEBean01 pojo: pojoClient calling


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfy:undeploy
