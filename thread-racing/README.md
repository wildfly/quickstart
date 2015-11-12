thread-racing: A thread racing web application, which examples usage of technology introduced/updated by the latest Java EE specification.
===================================================
Author: Eduardo Martins  
Level: Beginner  
Technologies: Batch 1.0, CDI, EE Concurrency 1.0, JAX-RS 2.0, JMS 2.0, JPA 2.1, JSON 1.0, Web Sockets 1.0  
Summary: Demonstrates technologies introduced/updated by latest Java EE  
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The Java EE Thread Racing quickstart provides a web application that aims to demo new/updated technologies, introduced by the Java EE 7 specification, through simple use cases.

The web application allows the user to trigger a race between 4 threads, and follow, in real time, each thread progress till the race ends.

The race itself is composed by multiple stages, each demonstrating usage of a specific new/updated Java EE 7 technology, in concrete:

 * Batch 1.0
 * EE Concurrency 1.0
 * JAX-RS 2.0
 * JMS 2.0
 * JSON 1.0

Details about each stage are provided in the respective race stage Java class. Each stage has its own java package at src/main/java/org/jboss/as/quickstarts/threadracing/stage.

Web Sockets 1.0 is one of the most relevant new technologies introduced by Java EE 7, and instead of being used in a race stage, a Web Sockets 1.0 ServerEndpoint provides the remote application interface.
A new race is run when a client establishes a session. That session is then used to update the client in real time, with respect to the race progress and results. The Web Sockets Server Endpoint class is src/main/java/org/jboss/as/quickstarts/threadracing/WebSocketRace.java,
and is a good entry point when studying how the application code works.

JPA 2.1 is also present in the application code, specifically it is used to store race results in the, also new to Java EE, default data source instance. Further details are included in class src/main/java/org/jboss/as/quickstarts/threadracing/results/RaceResults.java

System requirements
-------------------

All you need to build this project is Java 8 (Java SDK 1.8) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
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

4. This will deploy `target/wildfly-thread-racing.war` to the running instance of the server.


Access the application 
---------------------

The application will be running at the following URL <http://localhost:8080/wildfly-thread-racing/>. To start a race press the "Insert Coin" button.


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

Build and Deploy the Quickstart - to OpenShift WildFly App
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift WildFly Application

Open a shell command prompt and change to a directory of your choice. Enter the following command for quickstarts running on WildFly 10:

    rhc app create wildflyThreadRacing jboss-wildfly-8

The domain name for this application will be `wildflyThreadRacing-<YOUR_DOMAIN_NAME>.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `wildflyThreadRacing` and will run the application inside the `wildfly-8` container. You should see some output similar to the following:

    Application Options
    -------------------
    Domain:     quickstart
    Cartridges: jboss-wildfly-8
    Gear Size:  default
    Scaling:    no

    Creating application 'wildflyThreadRacing' ...

    ...

    Your application 'wildflyThreadRacing' is now available.

    Run 'rhc show-app wildflyThreadRacing' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://wildflyThreadRacing-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

    git rm -r src pom.xml

Copy the source for the `thread-racing` quickstart into this new git repository:

    cp -r QUICKSTART_HOME/thread-racing/src .
    cp QUICKSTART_HOME/thread-racing/pom.xml .

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

    git add src pom.xml
    git commit -m "thread-racing quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes.

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments` directory and deployed without a context path.


### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL in your Web browser.

* <http://wildflyThreadRacing-quickstart.rhcloud.com/>

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a wildflyThreadRacing

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue.

