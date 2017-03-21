# ejb-security-interceptors: Use Interceptors to Switch Identities for an EJB Call

Author: Darran Lofthouse  
Level: Advanced  
Technologies: EJB, Security  
Summary: The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call in ${product.name.full}.

By default, when you make a remote call to an EJB deployed to the application server, the connection to the server is authenticated and any request received over this connection is executed as the identity that authenticated the connection. This is true for both client-to-server and server-to-server calls. If you need to use different identities from the same client, you normally need to open multiple connections to the server so that each one is authenticated as a different identity.

Rather than open multiple client connections, this quickstart offers an alternative solution. The identity used to authenticate the connection is given permission to execute a request as a different user. This is achieved with the addition of the following three components:

1. A client side interceptor to pass the requested identity to the remote server.
2. A server side interceptor to receive the identity and request that the call switches to that identity.
3. A JAAS LoginModule to decide if the user of the connection is allowed to execute requests as the specified identity.

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

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `quickstart-domain` security domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-security-domain.cli
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

1. The following `quickstart-domain` security-domain was added to the `security` subsystem.

        <security-domain name="quickstart-domain" cache-type="default">
            <authentication>
                <login-module name="DelegationLoginModule" code="org.jboss.as.quickstarts.ejb_security_interceptors.DelegationLoginModule" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="Remoting" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="RealmDirect" flag="required">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
        </security-domain>

    The EJB side of this quickstart makes use of a new security domain called `quickstart-domain`, which delegates to the `ApplicationRealm`. The `DelegationLoginModule` is used to support identity switching in this quickstart.

    The login module can either be added before or after the existing `Remoting` login module in the domain, but it MUST be somewhere before the existing `RealmDirect` login module. If the majority of requests will involve an identity switch, then it is recommended to have this module as the first module in the list. However, if the majority of requests will run as the connection user with occasional switches, it is recommended to place the `Remoting` login module first and this one second.

    The login module loads the properties file `delegation-mapping.properties` from the deployment. The location of this properties file can be overridden with the module-option `delegationProperties`. At runtime, this login module is used to decide if the user of the connection to the server is allowed to execute the request as the specified user.

    There are four ways the key can be specified in the properties file:

        user@realm  - Exact match of user and realm.
        user@*      - Allow a match of user for any realm.
        *@realm     - Match for any user in the realm specified.
        *           - Match for all users in all realms.

    When a request is received to switch the user, the identity of the user that opened the connection is used to check the properties file for an entry. The check is performed in the order listed above until the first match is found. Once a match is found, further entries that could match are not read. The value in the properties file can either be a wildcard `*` or it can be a comma separated list of users. Be aware that in the value/mapping side there is no notion of the realm. For this quickstart we use the following entry:

        ConnectionUser@ApplicationRealm=AppUserOne,AppUserTwo

    This means that the ConnectionUser added above can only ask that a request is executed as either AppUserOne or AppUserTwo. It is not allowed to ask to be executed as AppUserThree.

    All users are permitted to execute requests as themselves. In that case, the login module is not called. This is the default behavior that exists without the addition of the interceptors in this quickstart.

    Taking this further, the `DelegationLoginModule` can be extended to provide custom delegation checks. One thing not currently checked is if the user being switched to actually exists. If the module is extended, the following method can be overridden to provide a custom check.

         protected boolean delegationAcceptable(String requestedUser, OuterUserCredential connectionUser);

     For the purpose of the quickstart we just need an outbound connection that loops back to the same server. This will be sufficient to demonstrate the server-to-server capabilities.
2.  The following `ejb-outbound-realm` security-realm was added to the `management` security-realms. Note the Base64-encoded password is for the ConnectionUser account created above.

        <security-realm name="ejb-outbound-realm">
            <server-identities>
                <secret value="Q29ubmVjdGlvblBhc3N3b3JkMSE="/>
            </server-identities>
        </security-realm>
3. The following `ejb-outbound` outbound-socket-binding connection was created within the `standard-sockets` socket-binding-group:

        <outbound-socket-binding name="ejb-outbound">
            <remote-destination host="localhost" port="8080"/>
        </outbound-socket-binding>
4. The following `ejb-outbound-connection` remote-outbound-connection was added to the outbound-connections within the `remoting` subsytem:

        <outbound-connections>
            <remote-outbound-connection name="ejb-outbound-connection" outbound-socket-binding-ref="ejb-outbound" security-realm="ejb-outbound-realm" username="ConnectionUser">
                <properties>
                    <property name="SSL_ENABLED" value="false"/>
                </properties>
            </remote-outbound-connection>
        </outbound-connections>
5. If you chose to run the script to suppress system exceptions, you should see the following configuration in the `ejb3` subsystem.

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

4. This will deploy `target/${project.artifactId}.war` to the running instance of the server.

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

    This second round of tests is using the (PicketBox) ClientLoginModule with LoginContext API to set the desired Principal.

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

    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

## Investigate the Server Console Output

If you chose not to run the script to suppress system exceptions, you should see the following exceptions in the ${product.name} server console or log. The exceptions are logged for each of the tests where a request is rejected because the user is not authorized. The stacktraces were removed from this text for readability.

    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 3) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 6) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 1) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 6) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 5) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    ...
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.getSecurityInformation(): javax.ejb.EJBAccessException: WFLYSEC0027: Invalid User
    ...
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
    ERROR [org.jboss.as.ejb3.invocation] (EJB default - 8) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.getSecurityInformation(): javax.ejb.EJBAccessException: WFLYSEC0027: Invalid User


## Server Log: Expected warnings and errors

_Note:_ You will see the following warning appear twice in the server log. You can ignore these warnings.

    WARN  [org.jboss.as.dependency.deprecated] (MSC service thread 1-7) WFLYSRV0221: Deployment "deployment.jboss-ejb-security-interceptors.jar" is using a deprecated module ("org.jboss.as.core-security-api:main") which may be removed in future versions without notice.


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


## Remove the Security Domain Configuration

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=remove-security-domain.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=remove-security-domain.cli
    This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required
3. If you chose to run the script to suppress system exceptions, run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-system-exception.cli

     You should see the following result when you run the script:

        The batch executed successfully

### Remove the Security Domain Configuration Manually
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
7. Be sure to [Remove the Security Domain Configuration](#remove-the-security-domain-configuration) when you have completed testing this quickstart.


## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources
