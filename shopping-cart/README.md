EJB 3.1 Stateful Session Bean Example
=====================================

Author: Serge Pagop

What is it?
-----------

In this example, you will learn how to deploy and run a simple Java EE 6 application named shopping-cart using stateful session bean. The purpose of the shopping-cart is to perform operations like buy, checkout and getCartContents for the customer. 
The shopping-cart application consists of an enterprise component (server module), which performs the operations for the customer and a simple Java remote client (client module) that looks up the stateful bean via JNDI and invokes the methods on them.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on a JBoss AS 7.1 or EAP 6.0.
The following instructions target JBoss AS 7.1, but they also apply to JBoss EAP 6.

Building and deploying the application
--------------------------------------

Start JBoss AS 7 (or EAP 6):

        $JBOSS_HOME/bin/standalone.sh

To build both the server component and the remote client program change into the examples shopping-cart directory and type:

        mvn clean install

The server component is packaged as a jar and needs deploying to the AS you just started:

        cd server
        mvn jboss-as:deploy

This maven goal will deploy server/target/jboss-as-shoppingcart-server.jar. You can check the Application Server console to see information messages regarding the deployment.

Note that you can also start JBoss AS 7.1 or JBoss EAP 6 and deploy the project using Eclipse. See the JBoss AS 7
<a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a> 
for more information.

Now start a client that will access the beans you just deployed:

        cd ../client
        mvn exec:exec

You should see output showing:

The client sending a method invocation to the stateful session bean to buy two Memory sticks and one MacBook Air Laptop, then a second invocation is to call the getgetCartContents() service and print the content of the cart. After that the checkout() service will be called to remove the bean instance from the object pool. For testing the client calls getCartContents() after checkout() invocation again and this cause an "javax.ejb.NoSuchEJBException".
Check the console where you're executing the client and Application Server Console for Server information. This can look this:

- Client side console

INFO: XNIO NIO Implementation Version 3.0.0.CR5
Feb 12, 2012 6:29:52 PM org.jboss.remoting3.EndpointImpl <clinit>
INFO: JBoss Remoting version 3.2.0.CR6
Feb 12, 2012 6:29:52 PM org.jboss.ejb.client.remoting.VersionReceiver handleMessage
INFO: Received server version 1 and marshalling strategies [river]
Feb 12, 2012 6:29:52 PM org.jboss.ejb.client.remoting.RemotingConnectionEJBReceiver associate
INFO: Successful version handshake completed for receiver context org.jboss.ejb.client.EJBReceiverContext@7b36a43c on channel Channel ID c0d6d96c (outbound) of Remoting connection 62c09554 to localhost/127.0.0.1:4447

&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
Obtained a remote stateful counter for invocation
Buying 1 memory stick
Buying another memory stick
Buying a laptop

Print cart:
1     MacBook Air Laptop
2     Red Hat Memory stick

Checkout
Should throw an object not found exception by invoking on cart after @Remove method
Feb 12, 2012 6:29:53 PM org.jboss.ejb.client.remoting.ChannelAssociation resultReady
INFO: Discarding result for invocation id 6 since no waiting context found
Successfully caught no such object exception.
&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


- Server side console

18:22:06,896 INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-2) JNDI bindings for session bean named ShoppingCartBean in deployment unit deployment "jboss-as-shoppingcart-server.jar" are as follows:

	java:global/jboss-as-shoppingcart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
	java:app/jboss-as-shoppingcart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
	java:module/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
	java:global/jboss-as-shoppingcart-server/ShoppingCartBean
	java:app/jboss-as-shoppingcart-server/ShoppingCartBean
	java:module/ShoppingCartBean

18:22:07,865 INFO  [org.jboss.as.server] (management-handler-threads - 2) JBAS018559: Deployed "jboss-as-shoppingcart-server.jar"
18:29:53,757 INFO  [stdout] (pool-9-thread-8) To be implemented
18:29:53,794 INFO  [org.jboss.as.ejb3] (pool-9-thread-8) JBAS014101: Failed to find {[-22, 53, -53, 71, 41, 47, 72, -112, -113, -93, -43, -23, -2, -49, 119, 40]} in cache
18:29:53,798 ERROR [org.jboss.ejb3.invocation] (pool-9-thread-9) JBAS014134: EJB Invocation failed on component ShoppingCartBean for method public abstract java.util.HashMap org.jboss.as.quickstarts.sfsb.ShoppingCart.getCartContents(): javax.ejb.NoSuchEJBException: Could not find SFSB ShoppingCartBean

Undeploy the application
------------------------

To undeploy the server side component from JBoss AS:

        cd ../server
        mvn jboss-as:undeploy

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs
of any library in the project, you can run either of the following two
commands to pull them into your local repository. The IDE should then detect
them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

