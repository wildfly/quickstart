Remote EJB Client Example
=========================

What is it?
-----------

This example shows how to access an EJB from a remote Java client program. It
demonstrates the use of *EJB 3.1* and *JNDI* in *JBoss AS 7*.

There are two parts to the example: a server side component and a remote client program
that accesses it. Each part is in its own standalone Maven module, however the quickstart
does provide a top level module to simplify the packaging of the artifacts.

The server component is comprised of a stateful and a stateless EJB. The client program looks
up the stateless and stateful beans via JNDI and invokes methods on them.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7 or EAP 6.
The following instructions target JBoss AS 7, but they also apply to JBoss EAP 6.

Building and deploying the application
-------------------------

Start JBoss AS 7 (or EAP 6):

        $JBOSS_HOME/bin/standalone.sh

To build both the server side component and a remote client program change into the
examples quickstart directory and type:

        mvn clean package

The server side component is packaged as a jar and needs deploying to the AS you just started:

        cd server-side
        mvn jboss-as:deploy

This maven goal will deploy server-side/target/jboss-as-ejb-remote-app.jar. You can check the AS
console to see information messages regarding the deployment.

Note that you can also start JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7
Getting Started Guide for Developers for more information.

Now start a client that will access the beans you just deployed:

        cd ../client
        mvn exec:exec

You should see output showing:

* the client sending a method invocation to the stateless bean to add
two numbers and then a second invocation to subtract two numbers.

* the client sends a method invocation to the statful bean asking it to increment a counter
followed by a request to get the current value of the counter. To demonstrate that the bean
is stateful it performs the same two invocations a number of times proving that the bean
is maintaining the state of the counter between invocations.

To undeploy the server side component from JBoss AS:

        cd ../server-side
        mvn jboss-as:undeploy

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

