kitchensink-html5-mobile: More Complex Example of HTML5, Mobile and JAX-RS 
=========================================================================================================
Author: Jay Balunas  
Level: Beginner   
Technologies: CDI, HTML5, REST  
Summary: The `kitchensink-html5-mobile` quickstart is based on `kitchensink`, but uses HTML5 and jQuery Mobile, making it suitable for mobile and tablet computers.  
Target Product: WildFly    
Source: https://github.com/wildfly/wildfly-quickstart  

What is it?
-----------

The `kitchensink-html5-mobile` quickstart is based on the `kitchensink` quickstart and demonstrates a Java EE 7 mobile database application using HTML5, jQuery Mobile, JAX-RS, JPA, and REST in Red Hat JBoss Enterprise Application Platform.

This application is built using a HTML5 + REST approach.  This uses a pure HTML client that interacts with the application server via restful end-points (JAX-RS).  This application also uses some of the latest HTML5 features and advanced JAX-RS. And since testing is just as important with client side as it is server side, this application uses QUnit to show you how to unit test your JavaScript.

What is a modern web application without mobile web support? This application also integrates jQuery mobile and basic client side device detection to give you both a desktop and mobile  version of the interface. Both support the same features, including form validation, member registration, etc. However the mobile version adds in mobile layout, touch, and performance  improvements needed to get you started with mobile web development on JBoss.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ are required. and note that some behaviors will vary slightly (ex. validations) based on browser support, especially IE 9.

Mobile web support is limited to Android and iOS devices.  It should run on HP, and Black Berry devices as well.  Windows Phone, and others will be supported as  jQuery Mobile announces support.
 
With the prerequisites out of the way, you're ready to build and deploy.


Start the WildFly Server
-----------------------

1. Open a command line and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat

   Note: Adding "-b 0.0.0.0" to the above commands will allow external clients (phones, tablets, desktops, etc...) connect through your local network.

   For example

        For Linux:   WILDFLY_HOME/bin/standalone.sh -b 0.0.0.0
        For Windows: WILDFLY_HOME\bin\standalone.bat -b 0.0.0.0


Build and Deploy the Quickstart
-------------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean wildfly:deploy

4. This deploys `target/wildfly-kitchensink-html5-mobile.war` to the running instance of the server.


Access the application
----------------------

Access the running client application in a browser at the following URL: <http://localhost:8080/jboss-kitchensink-html5-mobile/>.


Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 



<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->


Minification
-----------------

By default, the project uses the [wro4j](http://code.google.com/p/wro4j/) plugin, which provides the ability to concatenate, validate and minify JavaScript and CSS files. These minified files, as well as their unmodified versions are deployed with the project.

With just a few quick changes to the project, you can link to the minified versions of your JavaScript and CSS files.

First, in the <project-root>/src/main/webapp/index.html file, search for references to minification and comment or uncomment the appropriate lines.

Finally, wro4j runs in the compile phase so any standard build command like package, install, etc. will trigger it. The plugin is in a profile with an id of "minify" so you will want to specify that profile in your maven build.

NOTE: By default there are turn off tests so you must use the arquillian test profile to run tests when minifying.
For example:

    #No Tests
    mvn clean wildfly:deploy -Pminify

OR

    #With Tests
    mvn clean wildfly:deploy -Pminify,arq-wildfly-remote
 
Run the Arquillian tests
-------------------------------------

By default, tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. You can activate this test by selecting one of the container configuration provided  for JBoss.

To run the test in JBoss, first start the container instance. Then, run the test goal with the following profile activated:

    mvn clean test -Parq-wildfly-remote

Run the QUnit tests
-------------------------------------

QUnit is a JavaScript unit testing framework used and built by jQuery. Because JavaScript code is the core of an HTML5 application, this quickstart provides a set of QUnit tests that automate testing of this code in various browsers. 

Executing QUnit test cases is quite easy. Simply load the following HTML file in the browser you want to test.

        QUICKSTART_HOME/kitchensink-html5-mobile/src/test/qunit/index.html

You can also display the QUnit tests using the Eclipse built-in browser.

For more information on QUnit tests see <http://docs.jquery.com/QUnit>


Import the Project into an IDE
-------------------------------------

If you created the project using the Maven archetype wizard in your IDE (Eclipse, NetBeans or IntelliJ IDEA), then there is nothing to do. You should already have an IDE project.

If you created the project from the command line using archetype:generate, then you need to import the project into your IDE. If you are using NetBeans 6.8 or IntelliJ IDEA 9, then all you have to do is open the project as an existing project. Both of these IDEs recognize Maven projects natively.

Debug the Application
-------------------------------------

If you want to be able to debug into the source code or look at the Javadocs of any library in the project, you can run either of the following two commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
