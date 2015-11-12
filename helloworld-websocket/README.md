helloworld-websocket: WebSocket Hello World Example
===================
Author: Jeff Mesnil  
Level: Beginner  
Technologies: Web Socket  
Summary: Basic Web Socket |Demonstrates the use of Web Socket server endpoints  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

This example demonstrates the use of *Web Socket* in *WildFly*.

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

System requirements
-------------------

All you need to build this project is Java 8 (Java SDK 1.8) or better, Maven
3.0 or better.

The application this project produces is designed to be run on JBoss WildFly.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are
required.

With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------

### Deploying locally

First you need to start the JBoss container. To do this, run

    $JBOSS_HOME/bin/standalone.sh

or if you are using windows

    $JBOSS_HOME/bin/standalone.bat

To deploy the application, you first need to produce the archive to deploy using
the following Maven goal:

    mvn package

You can now deploy the artifact by executing the following command:

    mvn wildfly:deploy

This will deploy both the client and service applications.

The application will be running at the following URL <http://localhost:8080/wildfly-helloworld-websocket/>.

To undeploy run this command:

    mvn wildfly:undeploy

You can also start the JBoss container and deploy the project using JBoss Tools. See the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>
for more information.

Importing the project into an IDE
=================================

Detailed instructions for using Eclipse / JBoss Tools with are provided in the
<a href="https://github.com/wildfly/quickstart/guide/Introduction/" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

If you created the project from the commandline using archetype:generate, then
you need to import the project into your IDE. If you are using NetBeans 6.8 or
IntelliJ IDEA 9, then all you have to do is open the project as an existing
project. Both of these IDEs recognize Maven projects natively.

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc

Build and Deploy the Quickstart - to OpenShift
-------------------------

### Create an OpenShift Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command for quickstarts running on WildFly 10:

    rhc app create helloworldwebsocket jboss-wildfly-8

The domain name for this application will be `helloworldwebsocket-<YOUR_DOMAIN_NAME>.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldwebsocket` and will run the application inside the `wildfly-8` container. You should see some output similar to the following:

    Application Options
    -------------------
    Domain:     quickstart
    Cartridges: jboss-wildfly-8
    Gear Size:  default
    Scaling:    no

    Creating application 'helloworldwebsocket' ...

    ...

    Your application 'helloworldwebsocket' is now available.

    Run 'rhc show-app helloworldwebsocket' for more details about your app.

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldmdb-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget. Be sure to replace the `quickstart` in the URL with your domain name.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can migrate the quickstart source. You do not need the generated default application, so navigate to the new git repository directory and tell git to remove the source and pom files:

    git rm -r src pom.xml

Copy the source for the `helloworld-websocket` quickstart into this new git repository:

    cp -r QUICKSTART_HOME/helloworld-websocket/src .
    cp QUICKSTART_HOME/helloworld-websocket/pom.xml .

### Deploy the OpenShift Application

You can now deploy the changes to your OpenShift application using git as follows:

    git add src pom.xml
    git commit -m "helloworld-websocket quickstart on OpenShift"
    git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes.

Note that the `openshift` profile in the `pom.xml` file is activated by OpenShift. This causes the WAR built by OpenShift to be copied to the `deployments` directory and deployed without a context path.


### Test the OpenShift Application

When the push command returns you can test the application by getting the following URL in your Web browser.

* <http://helloworldwebsocket-quickstart.rhcloud.com/>

If the application has run succesfully you should see the upper-case version of the text when you click on the `Shout!` button.

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Delete the OpenShift Application

When you are finished with the application you can delete it as follows:

        rhc app-delete -a helloworldwebsocket

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must delete an existing application before you continue.


Copyright headers
-----------------

To update the copyright headers, just run `mvn license:format -Dyear=<current year>`


