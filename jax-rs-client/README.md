jax-rs-client: External JAX-RS Client
======================
Author: Blaine Mincey
Level: Intermediate
Technologies: JAX-RS
Summary: Demonstrates the use an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses CDI 1.0 and JAX-RS
Prerequisites: helloworld-rs
Target Product: EAP

What is it?
-----------

This example demonstrates an external JAX-RS RestEasy client which interacts with a JAX-RS Web service that uses *CDI 1.0* and *JAX-RS* 
in *JBoss Enterprise Application Platform 6* or *JBoss AS 7*.  

This client "calls" the HelloWorld JAX-RS Web Service that was created in the `helloworld-rs` quickstart. See the **Prerequisite** section below for details on how to build and deploy the `helloworld-rs` quickstart.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Prerequisites
-----------

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs README](../helloworld-rs/README.md) file for details on how to deploy it.

You can verify the deployment of the `helloworld-rs` quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-as-helloworld-rs/rest/xml> 
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-as-helloworld-rs/rest/json>



Run the Arquillian Tests 
-------------------------

This quickstart provides Arquillian tests. 

_NOTE: The following commands assume you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Run the Arquillian Tests](../README.md#arquilliantests) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Make sure the `helloworld-rs` quickstart has been deployed on the server as noted in the **Prerequisites** section above.
3. Open a command line and navigate to the root directory of this quickstart.
4. Type the following command to run the test goal with the following profile activated:

        mvn clean test 


Investigate the Console Output
----------------------------

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following if the execution is successful:

===============================================
URL: http://localhost:8080/jboss-as-helloworld-rs/rest/xml
MediaType: application/xml

*** Response from Server ***

<xml><result>Hello World!</result></xml>

===============================================
===============================================
URL: http://localhost:8080/jboss-as-helloworld-rs/rest/json
MediaType: application/json

*** Response from Server ***

{"result":"Hello World!"}

===============================================


OpenShift
---------

To make this quickstart more interesting, deploy the RESTful service to OpenShift.  The following instructions will guide you as to the modifications that must be made to successfully execute the jax-rs-client against a service deployed to OpenShift.


Build and Deploy the Quickstart - to OpenShift
-------------------------

_IMPORTANT_: This quickstart depends on the deployment of the `helloworld-rs` quickstart to OpenShift for its test. Follow the instructions [Build and Deploy the Quickstart - to OpenShift](../helloworld-rs/README.md#openShiftInstructions) in the helloworld-rs README to deploy that application to OpenShift. Do NOT yet follow the step "Destroy the OpenShift Application".

As it says in the helloworld-rs instructions, you can verify the deployment of the `helloworld-rs` quickstart by accessing the following content. Be sure to replace the `quickstart` in the URL with your domain name.

* <http://helloworldrs-quickstart.rhcloud.com/rest/xml> if you want *xml* or
* <http://helloworldrs-quickstart.rhcloud.com/rest/json> if you want *json*


### Modify the jax-rs-client quickstart pom.xml

Now that you have deployed the application, it is time to make changes to the jax-rs-client quickstart Arquillian tests. 

1. Open a shell command prompt and navigate to the `QUICKSTART_HOME/jax-rs-client/` directory.
2. Make a backup copy of the `pom.xml` file.
3. Open the `pom.xml` file in an editor and modify the `xmlUrl` and `jsonUrl` property values as follows. Be sure to replace the `quickstart` in the URL with your domain name.

        <property>
            <name>xmlUrl</name>
            <value>http://helloworldrs-quickstart.rhcloud.com/rest/xml</value>
        </property>
        <property>
            <name>jsonUrl</name>
            <value>http://helloworldrs-quickstart.rhcloud.com/rest/json</value>
        </property>


### Run the Maven test

Type the following command to run the jax-rs-client:

        mvn test

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you should see the following if the execution is successful:

===============================================
URL: http://helloworldrs-quickstart.rhcloud.com/rest/xml
MediaType: application/xml

*** Response from Server ***

<xml><result>Hello World!</result></xml>

===============================================
===============================================
URL: http://helloworldrs-quickstart.rhcloud.com/rest/json
MediaType: application/json

*** Response from Server ***

{"result":"Hello World!"}

===============================================

When you are finished testing, restore the `pom.xml` file to the previous version if you want to test locally.

### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldrs

_Note_: There is a limit to the number of applications you can deploy concurrently to OpenShift. If the `rhc app create` command returns an error indicating you have reached that limit, you must destroy an existing application before you continue. 

* To view the list of your OpenShift applications, type: `rhc domain show`
* To destroy an existing application, type the following, substituting the application name you want to destroy: `rhc app destroy -a APPLICATION_NAME_TO_DESTROY`
