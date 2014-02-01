shopping-cart: EJB 3.1 Stateful Session Bean (SFSB) Example 
=====================================
Author: Serge Pagop
Level: Intermediate
Technologies: EJB
Summary: Demonstrates a stateful session bean
Target Project: WildFly
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

In this example, you will learn how to deploy and run a simple Java EE 7 application named `shopping-cart` that uses a stateful session bean. The shopping-cart allows customers to buy, checkout and view their cart contents.

The shopping-cart application consists of the following:

1. A server side component:

    This standalone Java EE module is a JAR containing EJBs. It is responsible for managing the shopping cart.
2. A Java client:

    This simple Java client is launched using a "main" method. The remote client looks up a reference to the server module's API, via JNDI. It then uses this API to perform the operations the customer requests.


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
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

    mvn clean install wildfly:deploy

4. This maven goal will deploy `server/target/wildfly-shoppingcart-server.jar`. You can check the Application Server console to see information messages regarding the deployment.


Run the Client Application
------------------------

Now start a client that will access the beans you just deployed:

    mvn exec:java -f client/pom.xml

Investigate the Console Output
-------------------------------

You should see the following: 

1. The client sends a remote method invocation to the stateful session bean to buy two "JBoss Enterprise Application Platform 6" subscriptions and one "JBoss SOA Platform 6" subscription.
2. The client sends a remote method invocation to get the contents of the cart and prints it to the console.
3. The client sends a remote method invocation to invoke checkout. Note the `checkout()` method in the server `ShoppingCartBean` has the `@Remove` annotation. This means the container will destroy shopping cart after the call and it will no longer be available. 
4. The client calls `getCartContents()` to make sure the shopping cart was removed after checkout. This results in a `javax.ejb.NoSuchEJBException` trace in the server, proving the cart was removed.

On the client console, you should see output similar to:

    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    Obtained the remote interface to the shopping cart
    Buying a "JBoss Enterprise Application Platform 6"
    Buying another "JBoss Enterprise Application Platform 6"
    Buying a "JBoss SOA Platform 6"

    Print cart:
    1     JBoss SOA Platform 6
    2     JBoss Enterprise Application Platform 6

    Checkout
    Cart was correctly removed, as expected, after Checkout
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


On the server console, you should see output similar to (remember the server messages might change for different versions):

    INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-2) JNDI bindings for session bean named ShoppingCartBean in deployment unit deployment "jboss-shopping-cart-server.jar" are as follows:

    	java:global/jboss-shopping-cart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:app/jboss-shopping-cart-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:module/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
    	java:global/jboss-shopping-cart-server/ShoppingCartBean
    	java:app/jboss-shopping-cart-server/ShoppingCartBean
    	java:module/ShoppingCartBean

    INFO  [org.jboss.as.server] (management-handler-threads - 2) JBAS018559: Deployed "jboss-shopping-cart-server.jar"
    INFO  [stdout] (pool-9-thread-8) implementing checkout() left as exercise for the reader!

_Note_: You also see the following `EJB Invocation failed` and `NoSuchEJBException` messages in the server log. This is the expected result because method is annotated with `@Remove`. This means the next invocation after the shopping cart checkout fails because the container has destroyed the instance and it is no longer available.

    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 5) JBAS014134: EJB Invocation failed on component ShoppingCartBean for method public abstract java.util.HashMap org.jboss.as.quickstarts.sfsb.ShoppingCart.getCartContents(): javax.ejb.NoSuchEJBException: JBAS014300: Could not find EJB with id {...]}

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
---------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc
