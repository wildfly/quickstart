jax-rs-client: External JAX-RS Client
======================

Author: Blaine Mincey, blaine.mincey@redhat.com

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

IMPORTANT: This quickstart depends on the deployment of the 'helloworld-rs' quickstart for its test. Before running this quickstart, see the [helloworld-rs README](../hellworld-rs/README.md) file for details on how to deploy it.

You can verify the deployment of the `helloworld-rs` quickstart by accessing the following content:

* The *XML* content can be viewed by accessing the following URL: <http://localhost:8080/jboss-as-helloworld-rs/xml> 
* The *JSON* content can be viewed by accessing this URL: <http://localhost:8080/jboss-as-helloworld-rs/json>



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
URL: http://localhost:8080/jboss-as-helloworld-rs/xml
MediaType: application/xml

*** Response from Server ***

<xml><result>Hello World!</result></xml>

===============================================
===============================================
URL: http://localhost:8080/jboss-as-helloworld-rs/json
MediaType: application/json

*** Response from Server ***

{"result":"Hello World!"}

===============================================

OpenShift
---------

To make this quickstart more interesting, deploy the RESTful service to OpenShift.  The following instructions will guide you as to the modifications that must be made to successfully execute the jax-rs-client against a service deployed to OpenShift.


Build and Deploy the Quickstart - to OpenShift
-------------------------

_NOTE: At the time of this writing, JBoss Enterprise Application Platform 6 is not yet available on OpenShift, so only the JBoss AS 7 version of this quickstart can be deployed to OpenShift_.

### Create an OpenShift Express Account and Domain

If you do not yet have an OpenShift account and domain, [Sign in to OpenShift](https://openshift.redhat.com/app/login) to create the account and domain. [Get Started with OpenShift](https://openshift.redhat.com/app/getting_started) will show you how to install the OpenShift Express command line interface.

### Create the OpenShift Application

Open a shell command prompt and change to a directory of your choice. Enter the following command:

        rhc app create -a helloworldrs -t jbossas-7

_NOTE_: The domain name for this application will be `helloworldrs-YOUR_DOMAIN_NAME.rhcloud.com`. Here we use the _quickstart_ domain. You will need to replace it with your own OpenShift domain name.

This command creates an OpenShift application called `helloworldrs` and will run the application inside a `jbossas-7` container. You should see some output similar to the following:

    Creating application: helloworldrs
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'helloworldrs-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'helloworldrs' is available:  Success!
    
    helloworldrs published:  http://helloworldrs-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@helloworldrs-quickstart.rhcloud.com/~/git/helloworldrs.git/
    Successfully created application: helloworldrs

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldrs-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget.

### Migrate the Quickstart Source

Now that you have confirmed it is working you can now migrate the quickstart source. You no longer need the default application so change directory into the new git repository and tell git to remove the source files and pom:

        cd helloworldrs
        git rm -r src pom.xml

Copy the source for the `helloworld-rs` quickstart into this new git repository:

        cp -r <quickstarts>/helloworld-rs/src .
        cp <quickstarts>/helloworld-rs/pom.xml .

You can now deploy the changes to your OpenShift application using git as follows:

        git add src pom.xml
        git commit -m "helloworld-rs quickstart on OpenShift"
        git push

The final push command triggers the OpenShift infrastructure to build and deploy the changes. 

Note that the `openshift` profile in `pom.xml` is activated by OpenShift, and causes the war build by openshift to be copied to the `deployments` directory, and deployed without a context path.

When the push command returns you can retest the application by getting the following URLs either via a browser or using tools such as curl or wget:

* <http://helloworldrs-quickstart.rhcloud.com/xml> if you want *xml* or
* <http://helloworldrs-quickstart.rhcloud.com/json> if you want *json*

You can use the OpenShift command line tools or the OpenShift web console to discover and control the application.

### Modify the jax-rs-client pom.xml
Now that you have deployed the helloworld-rs application, it is time to make the appropriate changes to the jax-rs-client.  Open up the jax-rs-client
pom.xml.  Edit the values for xmlUrl and jsonUrl.  It should resemble the following:

<property>
        <name>xmlUrl</name>
        <value>http://helloworldrs-quickstart.rhcloud.com/xml</value>
</property>
<property>
        <name>jsonUrl</name>
        <value>http://helloworldrs-quickstart.rhcloud.com/json</value>
</property>


### Run the Maven test
Type the following command to run the jax-rs-client:

        mvn test

This command will compile the example and execute a test to make two separate requests to the Web Service.  Towards the end of the Maven build output, you 
should see the following if the execution is successful:

===============================================
URL: http://helloworldrs-quickstart.rhcloud.com/xml
MediaType: application/xml

*** Response from Server ***

<xml><result>Hello World!</result></xml>

===============================================
===============================================
URL: http://helloworldrs-quickstart.rhcloud.com/json
MediaType: application/json

*** Response from Server ***

{"result":"Hello World!"}

===============================================


### Destroy the OpenShift Application

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldrs

