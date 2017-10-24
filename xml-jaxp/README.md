# xml-jaxp: Upload and Parse an XML File Using DOM or SAX

Author: Bartosz Baranowski  
Level: Intermediate  
Technologies: JAXP, SAX, DOM, Servlet  
Summary: The `xml-jaxp` quickstart demonstrates how to use Servlet and JSF to upload an XML file to ${product.name} and validate and parse it using DOM or SAX.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?


The `xml-jaxp` quickstart is a simple Java EE JAXP example that demonstrates how to use Servlet 3.0 and JSF to upload an XML file to *${product.name.full}* and parse it using DOM or SAX, both of which are built into Java. It also shows how to use modules available in ${product.name}.

This quickstart provides an example XML schema and document file to use when testing this quickstart.

* The XML schema is located here: `QUICKSTART_HOME/src/main/resources/catalog.xsd`
* The XML document is located here: `QUICKSTART_HOME/src/main/resources/catalog.xml`


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

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.


## Access the Application

The application will be running at the following URL: <http://localhost:8080/${project.artifactId}/>.

To test the quickstart, follow these steps.

1. Click the `Browse` button and navigate to the `QUICKSTART_HOME/src/main/resources/catalog.xml` file.
2. Click the `Upload` button. The XML file content is parsed and displayed on the page.
3. You should see the following output in the server console that shows the DOMXMLParser was used:

        INFO  [stdout] (http-/127.0.0.1:8080-1) Parsing the document using the DOMXMLParser!

To enable the alternative SAXXMLParser parser:

1. Remove the comments that surround the alternate parser element in the `WEB-INF/beans.xml` file.
2. Redeploy the application using the instructions above and access the application in a browser at the following URL:  <http://localhost:8080/${project.artifactId}/>.
3. Click the `Browse` button and navigate to the `QUICKSTART_HOME/src/main/resources/catalog.xml` file.
4. Click the `Upload` button. The XML file content is parsed and displayed on the page.
5. You should now see following output in the server console:

        INFO  [stdout] (http-/127.0.0.1:8080-1) Parsing the document using the SAXXMLParser!


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.


        mvn dependency:sources
