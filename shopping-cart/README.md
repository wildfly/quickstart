shopping-cart: EJB 3.1 Stateful Session Bean (SFSB) Example 
=====================================
Author: Serge Pagop
Level: Intermediate
Technologies: EJB
Summary: Demonstrates a stateful session bean
Target Product: EAP

What is it?
-----------

In this example, you will learn how to deploy and run a simple Java EE 6 application named `shopping-cart` that uses a stateful session bean. The shopping-cart allows customers to buy, checkout and view their cart contents. 

The shopping-cart application consists of the following:

1. A server side component:

    This standalone Java EE module is a JAR containing EJBs. It is responsible for managing the shopping cart.
2. A Java client:

    This simple Java client is launched using a "main" method. The remote client looks up a reference to the server module's API, via JNDI. It then uses this API to perform the operations the customer requests.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7. 

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss Enterprise Application Platform 6 or JBoss AS 7 with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss server. See the instructions in the previous section.

2. Open a command line and navigate to the `shopping-cart` quickstart directory
3. To build both the server component and the remote client program, deploy the server module, change into the examples shopping-cart directory and type the following:

    For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

        mvn clean install jboss-as:deploy -s PATH_TO_QUICKSTARTS/example-settings.xml
    For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

        mvn clean install jboss-as:deploy 
4. This maven goal will deploy `server/target/jboss-as-shoppingcart-server.jar`. You can check the Application Server console to see information messages regarding the deployment.


Run the Client Application
------------------------

Now start a client that will access the beans you just deployed:

    For JBoss Enterprise Application Platform 6 (Maven user settings NOT configured): 

        mvn exec:java -f client/pom.xml -s PATH_TO_QUICKSTARTS/example-settings.xml

    For JBoss AS 7 or JBoss Enterprise Application Platform 6 (Maven user settings configured): 

        mvn exec:java -f client/pom.xml 

Investigate the Console Output
-------------------------------

You should see the following: 

1. The client sends a remote method invocation to the stateful session bean to buy two Memory sticks and one MacBook Air Laptop
2. The client sends a remote method invocation to get the contents of the cart, and print it
3. The client sends a remote method invocation to invoke checkout

Note that the checkout will remove the SFSB. The client then sends a final remote method invocation to get the cart contents. As the bean has been removed, a `NoSuchEjbException` is thrown.

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


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
