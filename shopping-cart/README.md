EJB 3.1 Stateful Session Bean Example
=====================================

Author: Serge Pagop

What is it?
-----------

In this example, you will learn how to deploy and run a simple Java EE 6 application named shopping-cart using stateful session bean. The shopping-cart allows customers to buy, checkout and view their cart contents. 
The shopping-cart application consists of a standalone Java EE module (which is deployed to JBoss AS as a jar containing EJBs) and a simple Java client, launched using a "main" method.  The server module is responsible for managing the shopping cart. The remote client looks up a reference to the server module's API, via JNDI. It then uses this API to perform the operations the customer requests.

System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven
3.0 or better.

The application this project produces is designed to be run on a JBoss Enterprise Application Platform 6 or JBoss AS 7.
 
With the prerequisites out of the way, you're ready to build and deploy.

Deploying the application
-------------------------
 
First you need to start JBoss Enterprise Application Platform 6 or JBoss AS 7. To do this, run
  
    $JBOSS_HOME/bin/standalone.sh
  
or if you are using windows
 
    $JBOSS_HOME/bin/standalone.bat

To build both the server component and the remote client program, deploy the server module, change into the examples shopping-cart directory and type:

    mvn clean install jboss-as:deploy

This maven goal will deploy `server/target/jboss-as-shoppingcart-server.jar`. You can check the Application Server console to see information messages regarding the deployment.

Note that you can also start JBoss Enterprise Application Platform 6 or JBoss AS 7 and deploy the project using Eclipse. See the JBoss AS 7 <a href="https://docs.jboss.org/author/display/AS71/Getting+Started+Developing+Applications+Guide" title="Getting Started Developing Applications Guide">Getting Started Developing Applications Guide</a>  for more information.

Now start a client that will access the beans you just deployed:

    mvn exec:java -f client/pom.xml 

You should see output showing 

1. the client sending a remote method invocation to the stateful session bean to buy two Memory sticks and one MacBook Air Laptop
2. the client sending a remote method invocation to get the contents of the cart, and print it
3. the client sending a remote method invocation to invoke checkout

Note that the checkout will remove the SFSB; the client sends a final remote method invocation to get the cart contents. As the bean has been removed, a `NoSuchEjbException` is thrown.

On the client console, you should see output similar to:

    Mar 23, 2012 12:59:40 PM org.jboss.ejb.client.EJBClient <clinit>
    INFO: JBoss EJB Client version 1.0.5.Final
    Mar 23, 2012 12:59:40 PM org.xnio.Xnio <clinit>
    INFO: XNIO Version 3.0.3.GA
    Mar 23, 2012 12:59:40 PM org.xnio.nio.NioXnio <clinit>
    INFO: XNIO NIO Implementation Version 3.0.3.GA
    Mar 23, 2012 12:59:40 PM org.jboss.remoting3.EndpointImpl <clinit>
    INFO: JBoss Remoting version 3.2.3.GA
    Mar 23, 2012 12:59:40 PM org.jboss.ejb.client.remoting.VersionReceiver handleMessage
    INFO: Received server version 1 and marshalling strategies [river]
    Mar 23, 2012 12:59:40 PM org.jboss.ejb.client.remoting.RemotingConnectionEJBReceiver associate
    INFO: Successful version handshake completed for receiver context EJBReceiverContext{clientContext=org.jboss.ejb.client.EJBClientContext@2ad1918a, receiver=Remoting connection EJB receiver [connection=Remoting connection <6b28215d>,channel=jboss.ejb,nodename=ptarmigan]} on channel Channel ID a2f59bb1 (outbound) of Remoting connection 5caccd65 to localhost/127.0.0.1:4447
    Mar 23, 2012 12:59:40 PM org.jboss.ejb.client.remoting.ChannelAssociation$ResponseReceiver handleMessage
    WARN: Unsupported message received with header 0xffffffff
    
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    Obtained the remote interface to the shopping cart
    Buying a "JBoss Enterprise Application Platform 6"
    Buying another "JBoss Enterprise Application Platform 6"
    Buying a "JBoss SOA Platform 6"
    
    Print cart:
    1     JBoss SOA Platform 6
    2     JBoss Enterprise Application Platform 6
    
    Checkout
    Mar 23, 2012 12:59:41 PM org.jboss.ejb.client.remoting.ChannelAssociation resultReady
    INFO: Discarding result for invocation id 6 since no waiting context found
    ERROR: Cannot access cart after Checkout!
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

On the server console, you should see output similar to:

    18:22:06,896 INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-2) JNDI bindings for session bean named ShoppingCartBean in deployment unit deployment "jboss-as-shoppingcart-server.jar" are as follows:

    	java:global/jboss-as-shoppingcart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:app/jboss-as-shoppingcart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:module/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:global/jboss-as-shoppingcart-server/ShoppingCartBean
    	java:app/jboss-as-shoppingcart-server/ShoppingCartBean
    	java:module/ShoppingCartBean

    18:22:07,865 INFO  [org.jboss.as.server] (management-handler-threads - 2) JBAS018559: Deployed "jboss-as-shoppingcart-server.jar"
    18:29:53,757 INFO  [stdout] (pool-9-thread-8) implementing checkout() left as exercise for the reader!
    18:29:53,794 INFO  [org.jboss.as.ejb3] (pool-9-thread-8) JBAS014101: Failed to find {[-22, 53, -53, 71, 41, 47, 72, -112, -113, -93, -43, -23, -2, -49, 119, 40]} in cache
    18:29:53,798 ERROR [org.jboss.ejb3.invocation] (pool-9-thread-9) JBAS014134: EJB Invocation failed on component ShoppingCartBean for method public abstract java.util.HashMap org.jboss.as.quickstarts.sfsb.ShoppingCart.getCartContents(): javax.ejb.NoSuchEJBException: Could not find SFSB ShoppingCartBean

Undeploy the application
------------------------

To undeploy the server side component:

    mvn jboss-as:undeploy

Downloading the sources and Javadocs
====================================

If you want to be able to debug into the source code or look at the Javadocs of any library in the project, you can run either of the following two commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
