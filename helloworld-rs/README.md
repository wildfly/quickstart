helloworld-rs: Helloworld Using JAX-RS (Java API for RESTful Web Services)
==========================================================================
Authors: Gustavo A. Brey, Gaston Coco and others

What is it?
-----------

This example demonstrates the use of *CDI 1.0* and *JAX-RS* in *JBoss AS 7* or *JBoss Enterprise Application Platform 6*.

System requirements
-------------------

The example can be deployed using Maven from the command line or from Eclipse using JBoss Tools.

To set up Maven or JBoss Tools in Eclipse, refer to the 
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>.

Deploying the application
-------------------------

To deploy to JBoss AS 7 or JBoss Enterprise Application Platform 6, start the JBoss application server and type:

    mvn package jboss-as:deploy 

The application is deployed to <http://localhost:8080/jboss-as-helloworld-rs>, so in order to get either both content you will have to point

* <http://localhost:8080/jboss-as-helloworld-rs/xml> if you want *xml* or
* <http://localhost:8080/jboss-as-helloworld-rs/json> if you want *json*

Deploying the Application to OpenShift
--------------------------------------

Firstly, lets assume you already have an OpenShift (express) account and have created a domain. If you don't please visit <https://openshift.redhat.com/app/login>
to create an account and follow the getting started guide which can be found at <http://docs.redhat.com/docs/en-US/OpenShift_Express/2.0/html/Getting_Started_Guide/index.html>. The guide will show you how to install the OpenShift Express command line interface.

Open a shell command prompt and change to a directory of your choice. Enter the following command:

    rhc app create -a helloworldrs -t jbossas-7

This command creates an OpenShift application called `helloworldrs` and will run the application inside a `jbossas-7` container. You should see some output similar to the following:

    Creating application: helloworldrs
    Now your new domain name is being propagated worldwide (this might take a minute)...
    Warning: Permanently added 'helloworldrs-quickstart.rhcloud.com,107.22.36.32' (RSA) to the list of known hosts.
    Confirming application 'helloworldrs' is available:  Success!
    
    helloworldrs published:  http://helloworldrs-quickstart.rhcloud.com/
    git url:  ssh://b92047bdc05e46c980cc3501c3577c1e@helloworldrs-quickstart.rhcloud.com/~/git/helloworldrs.git/
    Successfully created application: helloworldrs

The create command creates a git repository in the current directory with the same name as the application. Notice that the output also reports the URL at which the application can be accessed. Make sure it is available by typing the published url <http://helloworldrs-quickstart.rhcloud.com/> into a browser or use command line tools such as curl or wget.

Now that you have confirmed it is working you can now migrate the quickstart source. You no longer need the default application so change directory into the new git repo and tell git to remove the source files and pom:

    cd helloworldrs
    git rm -r src pom.xml

Copy the source for the helloworld-rs quickstart into this new git repo:

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

When you are finished with the application you can destroy it as follows:

        rhc app destroy -a helloworldrs

