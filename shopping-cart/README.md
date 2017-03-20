# shopping-cart: EJB Stateful Session Bean (SFSB) Example

Author: Serge Pagop  
Level: Intermediate  
Technologies: SFSB EJB  
Summary: The `shopping-cart` quickstart demonstrates how to deploy and run a simple Java EE 7 shopping cart application that uses a stateful session bean (SFSB).   
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `shopping-cart` quickstart demonstrates how to deploy and run a simple Java EE 7 application that uses a stateful session bean (SFSB) in ${product.name.full}. The application allows customers to buy, checkout, and view their cart contents.

The `shopping-cart` application consists of the following:

1. A server side component:

    This standalone Java EE module is a JAR containing EJBs. It is responsible for managing the shopping cart.
2. A Java client:

    This simple Java client is launched using a `main` method. The remote client looks up a reference to the server module's API, via JNDI. It then uses this API to perform the operations the customer requests.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Configure the Server

This example quickstart purposely throws a `NoSuchEJBException` exception when the shopping cart is empty. This is the expected result because method is annotated with `@Remove`. This means the next invocation after the shopping cart checkout fails because the container has destroyed the instance and it is no longer available. If you do not run this script, you see the following ERROR in the server log, followed by the stacktrace

    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) WFLYEJB0034: EJB Invocation failed on component ShoppingCartBean for method public abstract java.util.Map org.jboss.as.quickstarts.sfsb.ShoppingCart.getCartContents(): javax.ejb.NoSuchEJBException: WFLYEJB0168: Could not find EJB with id UnknownSessionID [5168576665505352655054705267485457555457535250485552546568575254]

Follow the steps below to suppress system exception logging.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-system-exception.cli` file in the root of this quickstart directory. This script sets the `log-system-exceptions` attribute to `false` in the `ejb3` subsystem in the server configuration file.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-system-exception.cli
    You should see the following result when you run the script:

        The batch executed successfully
5. Stop the ${product.name} server.


## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

You should see the following configuration in the `ejb3` subsystem.

      <log-system-exceptions value="false"/>


## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat


## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server. See the instructions in the previous section.

2. Open a command prompt and navigate to the `shopping-cart` quickstart directory
3. To build both the server component and the remote client program, deploy the server module, change into the examples shopping-cart directory and type the following:

        mvn clean install wildfly:deploy
4. This Maven goal will deploy `server/target/${project.artifactId}-server.jar`. You can check the server console to see information messages regarding the deployment.

        INFO  [org.jboss.as.ejb3.deployment] (MSC service thread 1-2) WFLYEJB0473: JNDI bindings for session bean named 'ShoppingCartBean' in deployment unit 'deployment "${project.artifactId}-server.jar"' are as follows:

          java:global/${project.artifactId}-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
          java:app/${project.artifactId}-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
          java:module/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
          java:jboss/exported/${project.artifactId}-server/ShoppingCartBean!org.jboss.as.quickstarts.sfsb.ShoppingCart
          java:global/${project.artifactId}-server/ShoppingCartBean
          java:app/${project.artifactId}-server/ShoppingCartBean
          java:module/ShoppingCartBean

        INFO  [org.jboss.weld.deployer] (MSC service thread 1-4) WFLYWELD0006: Starting Services for CDI deployment: ${project.artifactId}-server.jar
        INFO  [org.jboss.weld.deployer] (MSC service thread 1-8) WFLYWELD0009: Starting weld service for deployment ${project.artifactId}-server.jar
        INFO  [org.jboss.as.server] (management-handler-thread - 3) WFLYSRV0010: Deployed "${project.artifactId}-server.jar" (runtime-name : "${project.artifactId}-server.jar")


## Run the Client Application

Now start a client that will access the beans you just deployed.

You can use the command prompt from the previous step or open a new one and navigate to the root of the `shopping-cart` quickstart directory.

Type the following command:

        mvn exec:java -f client/pom.xml         

__Note__: This quickstart requires `quickstart-parent` artifact to be installed in your local Maven repository.
To install it, navigate to quickstarts project root directory and run the following command:

        mvn clean install


## Investigate the Console Output

You should see the following:

1. The client sends a remote method invocation to the stateful session bean to buy two `32 GB USB 2.0 Flash Drive` and one `Wireless Ergonomic Keyboard and Mouse`.
2. The client sends a remote method invocation to get the contents of the cart and prints it to the console.
3. The client sends a remote method invocation to invoke checkout. Note the `checkout()` method in the server `ShoppingCartBean` has the `@Remove` annotation. This means the container will destroy shopping cart after the call and it will no longer be available.
4. The client calls `getCartContents()` to make sure the shopping cart was removed after checkout. This results in a `javax.ejb.NoSuchEJBException` trace in the server, proving the cart was removed.

On the client console, you should see output similar to:

    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
    Obtained the remote interface to the shopping cart
    Buying a "32 GB USB 2.0 Flash Drive".
    Buying another "32 GB USB 2.0 Flash Drive".
    Buying a "Wireless Ergonomic Keyboard and Mouse"

    Print cart:
    1     Wireless Ergonomic Keyboard and Mouse
    2     32 GB USB 2.0 Flash Drive

    Checkout
    Cart was correctly removed, as expected, after Checkout
    &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


In the server log, you should see:

    INFO  [stdout] (pool-9-thread-8) implementing checkout() left as exercise for the reader!


## Restore the Server Configuration

You can restore the system exception configuration by running the `restore-system-exception.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-system-exception.cli
   This script restores the  the `log-system-exceptions` attribute value to `true`. You should see the following result when you run the script:

        The batch executed successfully

### Restore the Server Configuration Manually
1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

This quickstart consists of multiple projects, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

* Be sure to configure ${product.name} to suppress system exception logging as described above under [Configure the Server](#configure-the-server). Stop the server at the end of that step.
* To deploy the server project, right-click on the `${project.artifactId}-server` project and choose `Run As` --> `Run on Server`.
* To run the client, right-click on the `${project.artifactId}-client` project and choose `Run As` --> `Java Application`. In the `Select Java Application` window, choose `Client - org.jboss.as.quickstarts.client` and click `OK`. The client output displays in the `Console` window.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
