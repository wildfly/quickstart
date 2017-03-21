# jsonp: JSON-P Object-based JSON generation and Stream-based JSON consuming

Author: Rafael Benevides  
Level: Beginner  
Technologies: CDI, JSF, JSON-P  
Summary: The `jsonp` quickstart demonstrates how to use the JSON-P API to produce object-based structures and then parse and consume them as stream-based JSON strings.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  


## What is it?

The `jsonp` quickstart creates a JSON string through object-based JSON generation and then parses and consumes it using stream-based JSON.

It shows how to use the JSON-P API to generate, parse, and consume JSON files.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Start the Server

1. Open a command line and navigate to the root of the  ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy
4. This will deploy `target/${project.artifactId}` to the running instance of the server.


## Access the Application

Access the running application in a browser at the following URL:  <http://localhost:8080/${project.artifactId}/>

You are presented with a simple form that is pre-filled with personal data. You can change those values if you prefer.

Click on `Generate JSON String from Personal Data` button. The textarea below the button presents a JSON string representing the data and values from the completed form.

Note that the JSON string contains String, number, boolean and array values.

Now, click on `Parse JSON String using Stream` button. The textarea below the button shows the events generated from the parsed JSON string.


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


<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->
