xml-jaxp: Upload and Parse an XML File Using DOM or SAX
========================
Author: Bartosz Baranowski  
Level: Intermediate  
Technologies: JAXP, SAX, DOM, Servlet  
Summary: The `xml-jaxp` quickstart demonstrates how to use Servlet and JSF to upload an XML file to WildFly and validate and parse it using DOM or SAX.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  
What is it?
-----------

The `xml-jaxp` quickstart is a simple Java EE JAXP example that demonstrates how to use Servlet 3.0 and JSF to upload an XML file to *Red Hat JBoss Enterprise Application Platform* and parse it using DOM or SAX, both of which are built into Java. It also shows how to use modules available in WildFly.

This quickstart provides an example XML schema and document file to use when testing this quickstart.

* The XML schema is located here: `QUICKSTART_HOME/src/main/resources/catalog.xsd` 
* The XML document is located here: `QUICKSTART_HOME/src/main/resources/catalog.xml`

 
System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 7](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Start the WildFly Server
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-xml-jaxp.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/wildfly-xml-jaxp/>.

To test the quickstart, follow these steps.

1. Click the `Browse` button and navigate to the `QUICKSTART_HOME/src/main/resources/catalog.xml` file.
2. Click the `Upload` button. The XML file content is parsed and displayed on the page. 
3. You should see the following output in the server console that shows the DOMXMLParser was used:

        INFO  [stdout] (http-/127.0.0.1:8080-1) Parsing the document using the DOMXMLParser!

To enable the alternative SAXXMLParser parser:

1. Remove the comments that surround the alternate parser element in the `WEB-INF/beans.xml` file.
2. Redeploy the application using the instructions above and access the application in a browser at the following URL:  <http://localhost:8080/wildfly-xml-jaxp/>.
3. Click the `Browse` button and navigate to the `QUICKSTART_HOME/src/main/resources/catalog.xml` file.
4. Click the `Upload` button. The XML file content is parsed and displayed on the page. 
5. You should now see following output in the server console:

        INFO  [stdout] (http-/127.0.0.1:8080-1) Parsing the document using the SAXXMLParser!


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.


        mvn dependency:sources

