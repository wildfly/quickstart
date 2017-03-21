# ejb-security-plus:  Using client and server side interceptors to supply additional information for authentication before EJB calls.

Author: Darran Lofthouse  
Level: Advanced  
Technologies: EJB, Security  
Summary: Demonstrates how interceptors can be used to supply additional information to be used for authentication before EJB calls.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

By default, when you make a remote call to an EJB deployed to the application server, the connection to the server is authenticated and any request received over this connection is executed as the identity that authenticated the connection. The authentication at the connection level is dependent on the capabilities of the underlying SASL mechanisms.

Rather than writing custom SASL mechanisms or combining multiple parameters into one this quickstart demonstrates how username / password authentication can be used to open the connection to the server and subsequently supply an additional security token for authentication before the EJB invocation. This is achieved with the addition of the following three components:

1. A client side interceptor to pass the additional token to the remote server.
2. A server side interceptor to receive the security token and ensure this is passes to the JAAS domain for verification.
3. A JAAS LoginModule to perform authentication taking into account the authenticated user of the connection and the additional security token.

The quickstart then makes use of a single remote EJB, `SecuredEJB` to verify that the propagation and verification of the security token is correct and a `RemoteClient` standalone client.

### SecuredEJB

For this quickstart the `SecuredEJB` only has the following method: -

    String getPrincipalInformation();

This method is used to confirm the identity of the authenticated user, on the client side changing either the users password or the additional authentication token will cause access to this method to be unavailable.  The output from a successfull call to this method will typically look like: -

	[Principal={quickstartUser}]

### RemoteClient

Finally there is the `RemoteClient` stand-alone client. The client demonstrates how a Principal with a custom credential can be set using the `SecurityContextAssociation` and how this can subsequently be used by a `CallbackHandler` for the username/password authentication required for the SASL authentication and subsequently how the additional token can be picked up by a client side interceptor and passed to the server with the invocation.


## Note on EJB client interceptors

${product.name} allows client side interceptors for EJB invocations. Such interceptors are expected to implement the `org.jboss.ejb.client.EJBClientInterceptor` interface.  Interceptors can be established in many ways, including the following:

- Using the `@ClientInterceptors` annotation, which exists in the `org.jboss.ejb.client.annotations` package of the EJB client API.  The annotation accepts a list of classes which implement the `EJBClientInterceptor` interface and each have a public, no-argument constructor, operating in much the same way as its server-side counterpart `@Interceptors` annotation in the standard `javax.interceptor` package.  The classes themselves may optionally be annotated with the `@ClientInterceptorPriority` annotation, which assigns a numerical priority used in sorting the interceptors for invocation.  The constant integer values on that annotation type represent standard values; if no priority is given, then the value of `APPLICATION` is chosen.
- Establishing a list of interceptors in the `wildfly-client.xml` configuration file, which applies to standalone applications.
- Using the `ServiceLoader`-based mechanism, which is an alternate approach which involves creating a `META-INF/services/org.jboss.ejb.client.EJBClientInterceptor` file and placing/packaging it in the classpath of the client application. The rules for such a file are dictated by the [Java ServiceLoader Mechanism](http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html). This file is expected to contain on each separate line the fully qualified class name of the EJB client interceptor implementation, which is expected to be available in the classpath. EJB client interceptors follow the same ordering rules as annotated or configured interceptors.

This quickstart uses the annotation-based approach.

### Ordering

Interceptors are ordered according to their assigned priority, which is `APPLICATION` if none is given.  Lower numbers come earlier, and higher numbers come later.  If more than one interceptor have the same priority, they are considered in the following order:

- Method-level annotated interceptors
- Class-level annotated interceptors
- Configuration-declared method interceptors
- Configuration-declared class interceptors
- Configuration-declared global interceptors
- `ServiceLoader`-based interceptors from the class path
- System-installed JBoss interceptors

If after these rules apply, more than one interceptor are still of equal priority, then they are applied in declaration or encounter order.


## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Prerequisites

_Note_: Unlike most of the quickstarts, this one requires ${product.name.full} ${product.version} or later.

This quickstart uses the default standalone configuration plus the modifications described here.

It is recommended that you test this approach in a separate and clean environment before you attempt to port the changes in your own environment.


## Configure the Server

These steps assume that you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

_NOTE - Before you begin:_

1. If it is running, stop the ${product.name} Server.
2. Backup the `${jboss.home.name}/standalone/configuration/standalone.xml` file. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.
3. Open a command prompt and navigate to the root of the ${product.name} directory.
4. The following shows the command line to start the server:

        For Linux:   bin/standalone.sh
        For Windows: bin\standalone.bat
5. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
This script adds the `quickstart-domain` domain to the `security` subsystem in the server configuration and configures authentication access. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=quickstart-domain:add(cache-type=default)
        #2 /subsystem=security/security-domain=quickstart-domain/authentication=classic:add
        #3 /subsystem=security/security-domain=quickstart-domain/authentication=classic/login-module=DelegationLoginModule:add(code=org.jboss.as.quickstarts.ejb_security_plus.SaslPlusLoginModule,flag=optional,module-options={password-stacking=useFirstPass})
        #4 /subsystem=security/security-domain=quickstart-domain/authentication=classic/login-module=RealmDirect:add(code=RealmDirect,flag=required,module-options={password-stacking=useFirstPass})
        The batch executed successfully.
        {"outcome" => "success"}

## Add the Application Users

This quickstart is built around the default `ApplicationRealm` as configured in the ${product.name} server distribution. Using the add-user utility script, you must add the following user to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| User |

This user is used to both connect to the server and is used for the actual EJB invocation.

For an example of how to use the add-user utility, see instructions in the root README file located here: [Add User](../README.md#addapplicationuser).

## Start the Server

1. Open a command line and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server with the web profile:

		For Linux:   bin/standalone.sh
		For Windows: bin\standalone.bat


## Build and Deploy the Quickstart

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

		mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.jar` to the running instance of the server.


## Run the Client

The step here assumes you have already successfully deployed the EJB to the server in the previous step and that your command prompt is still in the same folder.

1.  Type this command to execute the client:

		mvn exec:exec


## Investigate the Console Output

When you run the `mvn exec:exec` command, you see the following output.

    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    [Principal={quickstartUser}]
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

This output is only displayed when the client has supplied the correct username, password and authentication token combination.

## Re-Run the client

You can edit the class `RemoteClient` and update any one of username, password or authentication token.

1.  Type this command to execute the client:

		mvn compile exec:exec

At this point instead of the message shown above you should see a failure.

## Undeploy the Archive

1. Make sure you have started the ${product.name} as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

		mvn wildfly:undeploy


## Remove the Security Domain Configuration

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart.

1. Open a command line, navigate to the root of the ${product.name} directory, and start the server by typing the following command:

        For Linux:  bin/standalone.sh
        For Windows:  bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-security-domain.cli

This script removes the `quickstart-domain` security domain from the `security` subsystem in the server configuration.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to [Add the Application Users](#add-the-application-users) as described above.
* To deploy the server project, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run on Server`.
* You are presented with a browser login challenge. Enter the credentials as described above to access and test the running application.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
