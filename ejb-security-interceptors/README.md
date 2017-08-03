# ejb-security-interceptors: Use Interceptors to Switch Identities for an EJB Call

Author: Darran Lofthouse, Stefan Guilhen  
Level: Advanced  
Technologies: EJB, Security  
Summary: The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call in ${product.name.full}. It also demonstrates how Elytron supports identity switching based on permissions.

By default, when you make a remote call to an EJB deployed to the application server, the connection to the server is authenticated and any request received over this connection is executed as the identity that authenticated the connection. This is true for both client-to-server and server-to-server calls. Although it is possible to switch identities without requiring a new connection to be established, in this quickstart a single identity is used to establish the connection to and the interceptors are then used to switch the connection identity to a different identity for the EJB call. This is achieved with the addition of the following two components:

1. A client side interceptor to pass the requested identity to the remote server.
2. A server side interceptor to receive the identity and request that the call switches to that identity.

The quickstart then makes use of two EJBs, `SecuredEJB` and `IntermediateEJB`, to verify that the propagation and identity switching is correct and a `RemoteClient` standalone client.

### SecuredEJB

The `SecuredEJB` has three methods:

    String getSecurityInformation();
    boolean roleOneMethod();
    boolean roleTwoMethod();

The first method can be called by all users that are created in this quickstart. The purpose of this method is to return a String containing the name of the Principal that called the EJB along with the user's authorized role information, for example:

        [Principal={ConnectionUser}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=false]

The next two methods are annotated to require that the calling user is authorized for roles `RoleOne` and `RoleTwo` respectively.

### IntermediateEJB

The `IntermediateEJB` contains a single method. Its purpose is to make use of a remote connection and invoke each of the methods on the `SecuredEJB`. A summary is then returned with the outcome of the calls.

### RemoteClient

Finally there is the `RemoteClient` stand-alone client. The client makes calls using the identity of the established connection and also makes calls switching the identity to the different users.

In the real world, remote calls between servers in the servers-to-server scenario would truly be remote and separate. For the purpose of this quickstart, we make use of a loopback connection to the same server so we do not need two servers just to run the test.

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

The application this project produces is designed to be run on ${product.name.full} ${product.version}.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

## Prerequisites

This quickstart uses the default standalone configuration plus the modifications described here.

It is recommended that you test this approach in a separate and clean environment before you attempt to port the changes in your own environment.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Add the Application Users

This quickstart uses secured management interfaces and is built around the default `ApplicationRealm` as configured in the ${product.name} distribution.  You must create the following application users to access the running application:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| ConnectionUser| ApplicationRealm | ConnectionPassword1!| User |
| AppUserOne | ApplicationRealm | AppPasswordOne1! | User, RoleOne |
| AppUserTwo | ApplicationRealm | AppPasswordTwo1! | User, RoleTwo |
| AppUserThree | ApplicationRealm | AppPasswordThree1! | User, RoleOne, RoleTwo |

To add the users, open a command prompt and type the following commands:

        For Linux:
          ${jboss.home.name}/bin/add-user.sh -a -u 'ConnectionUser' -p 'ConnectionPassword1!' -g 'User'
          ${jboss.home.name}/bin/add-user.sh -a -u 'AppUserOne' -p 'AppPasswordOne1!' -g 'User,RoleOne'
          ${jboss.home.name}/bin/add-user.sh -a -u 'AppUserTwo' -p 'AppPasswordTwo1!' -g 'User,RoleTwo'
          ${jboss.home.name}/bin/add-user.sh -a -u 'AppUserThree' -p 'AppPasswordThree1!' -g 'User,RoleOne,RoleTwo'

        For Windows:
          ${jboss.home.name}\bin\add-user.bat -a -u 'ConnectionUser' -p 'ConnectionPassword1!' -g 'User'
          ${jboss.home.name}\bin\add-user.bat -a -u 'AppUserOne' -p 'AppPasswordOne1!' -g 'User,RoleOne'
          ${jboss.home.name}\bin\add-user.bat -a -u 'AppUserTwo' -p 'AppPasswordTwo1!' -g 'User,RoleTwo'
          ${jboss.home.name}\bin\add-user.bat -a -u 'AppUserThree' -p 'AppPasswordThree1!' -g 'User,RoleOne,RoleTwo'

The first user establishes the actual connection to the server. The subsequent two users demonstrate how to switch identities on demand. The final user can access everything but can not participate in identity switching.

Note that within the quickstart, we do not make use of the passwords for any of the `App` users. The passwords specified for those users are only suggested values that meet password minimum requirements.

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).

## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-elytron.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Back up the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-elytron.cli` file in the root of this quickstart directory. This script adds the configuration that enables Elytron security for the quickstart deployment. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-elytron.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-elytron.cli
    You should see the following result when you run the script:

        The batch executed successfully
5. Because this example quickstart demonstrates security, exceptions are thrown when secured EJB access is attempted by an invalid user. If you want to review the security exceptions in the server log, you can skip this step. If you want to suppress these exceptions in the server log, run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-system-exception.cli
    You should see the following result when you run the script:

        The batch executed successfully
6. Stop the ${product.name} server.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `application-security-domain` was added to the `ejb3` subsystem:

        <application-security-domains>
            <application-security-domain name="quickstart-domain" security-domain="ApplicationDomain"/>
        </application-security-domains>

    The `application-security-domain` essentially enables Elytron security for the quickstart EJBs. It maps the `quickstart-domain` that was set in the EJBs via annotation to the Elytron `ApplicationDomain` that will be responsible for authenticating and authorizing access to the EJBs.
2. The following `ejb-outbound-configuration` authentication configuration and `ejb-outbound-context` authentication context were added to the `elytron` subsystem:

        <authentication-configuration name="ejb-outbound-configuration" authentication-name="ConnectionUser" realm="ApplicationRealm" sasl-mechanism-selector="-JBOSS-LOCAL-USER #ALL">
            <credential-reference clear-text="ConnectionPassword1!"/>
        </authentication-configuration>
        <authentication-context name="ejb-outbound-context">
            <match-rule authentication-configuration="ejb-outbound-configuration"/>
        </authentication-context>

    The `ejb-outbound-configuration` contains the authentication configuration that will be used when invoking a method on a remote EJB (i.e. when `IntermediateEJB` calls the methods on the `SecuredEJB`). It contains the user/credential pair that should be used when establishing the connection to the remote EJB and the `sasl-mechanism-selector` defines the SASL mechanisms that should be tried. In the configuration above it allows for all mechanisms except for `JBOSS-LOCAL-USER`.

    The `ejb-outbound-context` is the authentiation context that is used by the remote outbound connection and it automatically selects the `ejb-outbound-configuration`.
3. The following permissions were added to the `constant-permission-mapper` in the `elytron` subsystem:

        <permission class-name="org.wildfly.security.auth.permission.RunAsPrincipalPermission" target-name="AppUserOne"/>
        <permission class-name="org.wildfly.security.auth.permission.RunAsPrincipalPermission" target-name="AppUserTwo"/>

    When switching identities via `createRunAsIdentity`, Elytron checks if the identity making the switch has permissions to do so. The above configuration essentially grants any identity permission to create a run-as identity. 

    For the purpose of this quickstart this permissions configuration is acceptable but in a real world scenario the recommendation would be to use a more fine-grained permissions configuration and grant the `RunAsPrincipalPermission`s only to the `ConnectionUser` identity. This could be done as follows:
    Create a `simple-permission-mapper` that assigns the necessary permissions to the `ConnectionUser`.

        <simple-permission-mapper name="run-as-permission-mapper">
            <permission-mapping principals="ConnectionUser">
                <permission class-name="org.wildfly.security.auth.permission.RunAsPrincipalPermission" target-name="AppUserOne"/>
                <permission class-name="org.wildfly.security.auth.permission.RunAsPrincipalPermission" target-name="AppUserTwo"/>
            </permission-mapping>
        </simple-permission-mapper>
    Create a `logical-permission-mapper` that expands the permissions of the `default-permission-mapper` to include the new `simple-permission-mapper`.

        <logical-permission-mapper name="quickstart-permission-mapper" logical-operation="or" left="default-permission-mapper" right="run-as-permission-mapper"/>
    Change the `ApplicationDomain` to use the extended mapper instead of the `default-permission-mapper`.

        <security-domain name="ApplicationDomain" default-realm="ApplicationRealm" permission-mapper="quickstart-permission-mapper" security-event-listener="local-audit">

    By doing that the same result is achieved but in a more controlled way, without granting permissions to all identities in the `constant-permission-mapper`.

4. The following `ejb-outbound` outbound-socket-binding connection was created within the `standard-sockets` socket-binding-group:

        <outbound-socket-binding name="ejb-outbound">
            <remote-destination host="localhost" port="8080"/>
        </outbound-socket-binding>

    For the purpose of the quickstart we just need an outbound connection that loops back to the same server. This will be sufficient to demonstrate the server-to-server capabilities.
5. The following `ejb-outbound-connection` remote-outbound-connection was added to the outbound-connections within the `remoting` subsytem:

        <outbound-connections>
            <remote-outbound-connection name="ejb-outbound-connection" outbound-socket-binding-ref="ejb-outbound" authentication-context="ejb-outbound-context"/>
        </outbound-connections>
6. Finally the `http-connector` in the `remoting` subsystem was updated to use the `application-sasl-authentication` authentication factory. It allows for the identity that was established in the connection authentication to be propagated to the components.

        <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ApplicationRealm" sasl-authentication-factory="application-sasl-authentication"/>
7. If you chose to run the script to suppress system exceptions, you should see the following configuration in the `ejb3` subsystem.

        <log-system-exceptions value="false"/>

## Start the Server

1. Open a command prompt and navigate to the root of the ${product.name} directory.
2. The following shows the command line to start the server:

        For Linux:   ${jboss.home.name}/bin/standalone.sh
        For Windows: ${jboss.home.name}\bin\standalone.bat

## Build and Deploy the Quickstart

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/${project.artifactId}.jar` to the running instance of the server.

## Run the Client

Before you run the client, make sure you have already successfully deployed the EJBs to the server in the previous step and that your command prompt is still in the same folder.

Type this command to execute the client:

        mvn exec:exec

## Investigate the Console Output

When you run the `mvn exec:exec` command, you see the following output. Note there may be other log messages interspersed between these.

    -------------------------------------------------
    * * About to perform test as ConnectionUser * *

    * Making Direct Calls to the SecuredEJB

    * getSecurityInformation()=[Principal={ConnectionUser}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=false]
    * Can call roleOneMethod()=false
    * Can call roleTwoMethod()=false

    * Calling the IntermediateEJB to repeat the test server to server

    * * IntermediateEJB - Begin Testing * *
    SecuredEJBRemote.getSecurityInformation()=[Principal={ConnectionUser}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=false]
    Can call roleOneMethod=false
    Can call roleTwoMethod=false
    * * IntermediateEJB - End Testing * *
    * * Test Complete * *

    -------------------------------------------------
    * * About to perform test as AppUserOne * *

    * Making Direct Calls to the SecuredEJB

    * getSecurityInformation()=[Principal={AppUserOne}, In role {User}=true, In role {RoleOne}=true, In role {RoleTwo}=false]
    * Can call roleOneMethod()=true
    * Can call roleTwoMethod()=false

    * Calling the IntermediateEJB to repeat the test server to server

    * * IntermediateEJB - Begin Testing * *
    SecuredEJBRemote.getSecurityInformation()=[Principal={AppUserOne}, In role {User}=true, In role {RoleOne}=true, In role {RoleTwo}=false]
    Can call roleOneMethod=true
    Can call roleTwoMethod=false
    * * IntermediateEJB - End Testing * *
    * * Test Complete * *

    -------------------------------------------------
    * * About to perform test as AppUserTwo * *

    * Making Direct Calls to the SecuredEJB

    * getSecurityInformation()=[Principal={AppUserTwo}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=true]
    * Can call roleOneMethod()=false
    * Can call roleTwoMethod()=true

    * Calling the IntermediateEJB to repeat the test server to server

    * * IntermediateEJB - Begin Testing * *
    SecuredEJBRemote.getSecurityInformation()=[Principal={AppUserTwo}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=true]
    Can call roleOneMethod=false
    Can call roleTwoMethod=true
    * * IntermediateEJB - End Testing * *
    * * Test Complete * *

    -------------------------------------------------
    * * About to perform test as AppUserThree * *

    * Making Direct Calls to the SecuredEJB

    * * Test Complete * *

    -------------------------------------------------
    Call as 'AppUserThree' correctly rejected.

## Investigate the Server Console Output

If you chose not to run the script to suppress system exceptions, you should see the following exceptions in the ${product.name} server console or log. The exceptions are logged for each of the tests where a request is rejected because the user is not authorized. The stacktraces were removed from this text for readability.

    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 10) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 5) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 8) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 3) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 2) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 5) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (default task-54) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.getSecurityInformation(): org.wildfly.security.authz.AuthorizationFailureException: ELY01088: Attempting to run as "AppUserThree" authorization operation failed

## Server Log: Expected warnings and errors

_Note:_ You will see the following warning appear twice in the server log. You can ignore these warnings.

    WARN  [org.jboss.as.dependency.deprecated] (MSC service thread 1-7) WFLYSRV0221: Deployment "deployment.jboss-ejb-security-interceptors.jar" is using a deprecated module ("org.jboss.as.core-security-api:main") which may be removed in future versions without notice.

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Restore the Original Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Original Server Configuration by Running the JBoss CLI Script

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required
3. If you chose to run the script to suppress system exceptions, run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-system-exception.cli

     You should see the following result when you run the script:

        The batch executed successfully

### Restore the Original Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


This quickstart requires additional configuration and deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Be sure to [Add the Application Users](#add-the-application-users) as described above.
2. Follow the steps above to [Configure the Server](#configure-the-server). Stop the server at the end of that step.
3. To deploy the application to the ${product.name} server, right-click on the `ejb-security-interceptors` project and choose `Run As` --> `Run on Server`.
4. To access the application, right-click on the `ejb-security-interceptors` project and choose `Run As` --> `Java Application`.
5. Choose `RemoteClient - org.jboss.as.quickstarts.ejb_security_interceptors` and click `OK`.
6. Review the output in the console window.
7. Be sure to [Restore the Original Server Configuration](#restore-the-original-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
