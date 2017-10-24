# contacts-jquerymobile: CRUD Example Using HTML5, jQuery Mobile and JAX-RS

Author: Joshua Wilson  
Level: Beginner  
Technologies: jQuery Mobile, jQuery, JavaScript, HTML5, REST  
Summary: The `contacts-jquerymobile` quickstart demonstrates a Java EE 7 mobile database application using HTML5, jQuery Mobile, JAX-RS, JPA, and REST.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `contact-jquerymobile` quickstart is a deployable Maven 3 project designed to help you get your foot in the door developing HTML5 based mobile web applications with Java EE 7 in ${product.name.full}. This project is setup to allow you to create a basic Java EE 7 application using HTML5, jQuery Mobile, JAX-RS, CDI, EJB, JPA, and Bean Validation. It includes a persistence unit and some sample persistence and transaction code to help you get your feet wet with database access in enterprise Java.

This application is built using a HTML5 + REST approach. This uses a pure HTML client that interacts with with the application server via restful end-points (JAX-RS). This application also uses some of the latest HTML5 features and advanced JAX-RS. And since testing is just as important with client side as it is server side, this application uses QUnit to show you how to unit test your JavaScript.

This application focuses on **CRUD** in a strictly mobile app using only **jQuery Mobile**(no other frameworks). The user will have the ability to:

* **Create** a new contact.

* **Read** a list of contacts.

* **Update** an existing contact.

* **Delete** a contact.

**Validation** is an important part of an application. Typically in an HTML5 app you can let the built-in HTML5 form validation do the work for you. However, mobile browsers do not support this feature at this time. In order to validate the forms, the `jquery.validate` plugin was added, which provides both client-side and server-side validation. Over AJAX, if there is an error, the error is returned and displayed in the form. You can see an example of this in the `Edit` form if you enter an email that is already in use. The application will attempt to insert the error message into a field if that field exists. If the field does not exist then it display it at the top. In addition, there are [qunit tests](#run-the-qunit-tests) for every form of validation.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

An HTML5 compatible browser such as Chrome, Safari 5+, Firefox 5+, or IE 9+ is required. Note that some behaviors, such as validation, will vary slightly based on browser support, especially IE 9.

Mobile web support is limited to Android and iOS devices. It should run on HP, and Black Berry devices as well. Windows Phone, and others will be supported as jQuery Mobile announces support.

With the prerequisites out of the way, you are ready to build and deploy.

## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the default profile:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat

   Note: Adding `-b 0.0.0.0` to the above commands will allow external clients, such as phones, tablets, and desktops, connect through your local network.

   For example

        For Linux:   ${jboss.home.name}/bin/standalone.sh -b 0.0.0.0
        For Windows: ${jboss.home.name}\bin\standalone.bat -b 0.0.0.0

## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This deploys `target/${project.artifactId}.war` to the running instance of the server.

## Access the Application

Access the running client application in a browser at the following URL: <http://localhost:8080/${project.artifactId}/>.

The app is made up of the following pages:

**Main page**

* Displays a list of contacts
* Search bar for the list
* Details button changes to the Detailed list
* Clicking on a contact brings up an Edit form
* Menu button (in upper left) opens menu

**Menu pullout**

* Add a new Contact
* List/Detail view switcher, depending on what is currently displayed
* About information
* Theming - apply various themes (only on the List view)

**Details page**

* Same as Main page except all information is displayed with each contact

**Add form**

* First name, Last name, Phone, Email, and BirthDate fields
* Save = submit the form
* Clear = reset the form but stay on the form
* Cancel = reset the form and go the Main page

**Edit form**

* Same as Add form
* Delete button will delete the contact currently viewed and return you to the Main page

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Minification

By default, the project uses the [wro4j](http://code.google.com/p/wro4j/) plugin, which provides the ability to concatenate, validate and minify JavaScript and CSS files. These minified files, as well as their unmodified versions are deployed with the project.

With just a few quick changes to the project, you can link to the minified versions of your JavaScript and CSS files.

First, in the `<project-root>/src/main/webapp/index.html` file, search for references to minification and comment or uncomment the appropriate lines.

Finally, wro4j runs in the compile phase so any standard build command like package, install, etc. will trigger it. The plug-in is in a profile with an id of `minify` so you will want to specify that profile in your maven build.

NOTE: By default there are turn off tests so you must use the arquillian test profile to run tests when minifying. For example:

    #No Tests
    mvn clean package wildfly:deploy -Pminify

OR

    #With Tests
    mvn clean verify wildfly:deploy -Pminify,arq-remote

## Run the Arquillian Tests

By default, tests are configured to be skipped. The reason is that the sample test is an Arquillian test, which requires the use of a container. You can activate this test by selecting one of the container configuration provided for JBoss.

To run the test in JBoss, first start the container instance. Then, run the test goal with the following profile activated:

    mvn clean verify -Parq-remote

## Run the QUnit tests

QUnit is a JavaScript unit testing framework used and built by jQuery. Because JavaScript code is the core of this HTML5 application, this quickstart provides a set of QUnit tests that automate testing of this code in various browsers. Executing QUnit test cases are quite easy.

Simply load the following HTML in the browser you wish to test.

        QUICKSTART_HOME/contacts-jquerymobile/src/test/qunit/index.html

_Note:_ If you use **Chrome**, some date tests fail. These are false failures and are known issues with Chrome. FireFox, Safari, and IE run the tests correctly. You can also display the tests using the Eclipse built-in browser.

For more information on QUnit tests see http://qunitjs.com/.

## Run the Arquillian Functional Tests

This quickstart provides Arquillian functional tests. They are located under the directory `functional-tests`. Functional tests verify that your application behaves correctly from the user's point of view - simulating clicking around the page as a normal user would do.

To run these tests, you must build the main project as described above.

1. Open a command line and navigate to the root directory of this quickstart.
2. Build the quickstart WAR using the following command:

        mvn clean package

3. Navigate to the functional-tests/ directory in this quickstart.
4. If you have a running instance of the ${product.name} server, as described above, run the remote tests by typing the following command:

        mvn clean verify -Parq-remote

5. If you prefer to run the functional tests using managed instance of the ${product.name} server, meaning the tests will start the server for you, type the following command:

_NOTE: For this to work, Arquillian needs to know the location of the ${product.name} server. This can be declared through the `JBOSS_HOME` environment variable or the `jbossHome` property in `arquillian.xml`. See [Run the Arquillian Tests](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/RUN_ARQUILLIAN_TESTS.md#run-the-arquillian-tests) for complete instructions and additional options._

        mvn clean verify -Parq-managed

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

<!-- Build and Deploy the Quickstart to OpenShift - Coming soon! -->

## Debug the Application

If you want to be able to debug into the source code or look at the Javadocs of any library in the project, you can run
either of the following two commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
