ejb-security-interceptors: Use Interceptors to Switch Identities for an EJB Call
====================
Author: Darran Lofthouse  
Level: Advanced  
Technologies: EJB, Security  
Summary: The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `ejb-security-interceptors` quickstart demonstrates how to use client and server side interceptors to switch the identity for an EJB call in Red Hat JBoss Enterprise Application Platform.

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

The next two methods are annotated to require that the calling user is authorized for roles 'RoleOne' and 'RoleTwo' respectively.

### IntermediateEJB

The `IntermediateEJB` contains a single method. Its purpose is to make use of a remote connection and invoke each of the methods on the `SecuredEJB`. A summary is then returned with the outcome of the calls.

### RemoteClient

Finally there is the `RemoteClient` stand-alone client. The client makes calls using the identity of the established connection and also makes calls switching the identity to the different users.

In the real world, remote calls between servers in the servers-to-server scenario would truly be remote and separate. For the purpose of this quickstart, we make use of a loopback connection to the same server so we don't need two servers just to run the test.

Note on EJB client interceptors
-----------------------

WildFly allows client side interceptors for EJB invocations. Such interceptors are expected to implement the `org.jboss.ejb.client.EJBClientInterceptor` interface. User applications can then plug in such interceptors in the 'EJBClientContext' either programatically or through the ServiceLoader mechanism.

- The programmatic way involves calling the `org.jboss.ejb.client.EJBClientContext.registerInterceptor(int order, EJBClientInterceptor interceptor)` API and passing the 'order' and the 'interceptor' instance. The 'order' is used to decide where exactly in the client interceptor chain, this 'interceptor' is going to be placed.
- The ServiceLoader mechanism is an alternate approach which involves creating a `META-INF/services/org.jboss.ejb.client.EJBClientInterceptor` file and placing/packaging it in the classpath of the client application. The rules for such a file are dictated by the [Java ServiceLoader Mechanism](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). This file is expected to contain in each separate line the fully qualified class name of the EJB client interceptor implementation, which is expected to be available in the classpath. EJB client interceptors added via the ServiceLoader mechanism are added to the end of the client interceptor chain, in the order they were found in the classpath.

This quickstart uses the ServiceLoader mechanism for registering the EJB client interceptor and places the `META-INF/services/org.jboss.ejb.client.EJBClientInterceptor` in the classpath, with the following content:

        # EJB client interceptor(s) that will be added to the end of the interceptor chain during an invocation
        # on EJB. If these interceptors are to be added at a specific position, other than last, then use the
        # programmatic API in the application to register it explicitly to the EJBClientContext

        org.jboss.as.quickstarts.ejb_security_interceptors.ClientSecurityInterceptor


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Prerequisites
-------------

This quickstart uses the default standalone configuration plus the modifications described here.

It is recommended that you test this approach in a separate and clean environment before you attempt to port the changes in your own environment.

Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Add the Application Users
---------------

This quickstart uses secured management interfaces and is built around the default `ApplicationRealm` as configured in the WildFly distribution.  You must create the following application users to access the running application:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| ConnectionUser| ApplicationRealm | ConnectionPassword1!| User |
| AppUserOne | ApplicationRealm | AppPasswordOne1! | User, RoleOne |
| AppUserTwo | ApplicationRealm | AppPasswordTwo1! | User, RoleTwo |
| AppUserThree | ApplicationRealm | AppPasswordThree1! | User, RoleOne, RoleTwo |

To add the users, open a command prompt and type the following commands:

        For Linux:
          WILDFLY_HOME/bin/add-user.sh -a -u 'ConnectionUser' -p 'ConnectionPassword1!' -g 'User'
          WILDFLY_HOME/bin/add-user.sh -a -u 'AppUserOne' -p 'AppPasswordOne1!' -g 'User,RoleOne'
          WILDFLY_HOME/bin/add-user.sh -a -u 'AppUserTwo' -p 'AppPasswordTwo1!' -g 'User,RoleTwo' 
          WILDFLY_HOME/bin/add-user.sh -a -u 'AppUserThree' -p 'AppPasswordThree1!' -g 'User,RoleOne,RoleTwo'

        For Windows:
          WILDFLY_HOME\bin\add-user.bat -a -u 'ConnectionUser' -p 'ConnectionPassword1!' -g 'User'
          WILDFLY_HOME\bin\add-user.bat -a -u 'AppUserOne' -p 'AppPasswordOne1!' -g 'User,RoleOne'
          WILDFLY_HOME\bin\add-user.bat -a -u 'AppUserTwo' -p 'AppPasswordTwo1!' -g 'User,RoleTwo' 
          WILDFLY_HOME\bin\add-user.bat -a -u 'AppUserThree' -p 'AppPasswordThree1!' -g 'User,RoleOne,RoleTwo'

The first user establishes the actual connection to the server. The subsequent two users demonstrate how to switch identities on demand. The final user can access everything but can not participate in identity switching.

Note that within the quickstart, we do not make use of the passwords for any of the 'App' users. The passwords specified for those users are only suggested values that meet password minimum requirements.

If you prefer, you can use the add-user utility interactively. 
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).


Configure the WildFly Server
---------------------------

These steps assume you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the WildFly server.
    * Backup the file: `WILDFLY_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the WildFly server by typing the following: 

        For Linux:  WILDFLY_HOME/bin/standalone.sh 
        For Windows:  WILDFLY_HOME\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `quickstart-domain` domain to the `security` subsystem in the server configuration and configures authentication access. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=configure-security-domain.cli
   You should see the following result when you run the script:

        The batch executed successfully
        {
            "outcome" => "success",
            "result" => undefined
        }
5. Stop the WildFly server.

Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `WILDFLY_HOME/standalone/configuration/standalone.xml` file and review the changes.

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



Start the WildFly Server 
-------------------------

1. Open a command prompt and navigate to the root of the WildFly directory.
2. The following shows the command line to start the server:

        For Linux:   WILDFLY_HOME/bin/standalone.sh
        For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/wildfly-ejb-security-interceptors.jar` to the running instance of the server.


Run the client
---------------------

The step here assumes you have already successfully deployed the EJBs to the server in the previous step and that your command prompt is still in the same folder.

1.  Type this command to execute the client:

        mvn exec:exec


Investigate the Console Output
----------------------------

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

Investigate the Server Console Output
----------------------------

Look at the WildFly server console or log and you should see the following exceptions. The exceptions are logged for each of the tests where a request is rejected because the user is not authorized. The stacktraces were removed from this text for readability.

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




Undeploy the Archive
--------------------

1. Make sure you have started the WildFly server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Remove the Security Domain Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file. 

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the WildFly server by typing the following: 

        For Linux:  WILDFLY_HOME/bin/standalone.sh
        For Windows:  WILDFLY_HOME\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=remove-security-domain.cli 
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=remove-security-domain.cli 
This script removes the `test` queue from the `messaging` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Remove the Security Domain Configuration Manually
1. If it is running, stop the WildFly server.
2. Replace the `WILDFLY_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.



Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

_NOTE:_ Be sure to configure the security domain by running the JBoss CLI commands as described in the section above entitled *Configure the WildFly Server*. Stop the server at the end of that step.

This quickstart requires additional configuration and deploys and runs differently in JBoss Developer Studio than the other quickstarts.

This quickstart requires that you add users, configure the WildFly server, and access the deployed application from a Java program using the Maven `exec` goal, so it deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Be sure to [Add the Application Users](#add-the-application-users) as described above.
2. Follow the steps above to [Configure the WildFly Server](#configure-the-jboss-eap-server).
3. To deploy the application to the WildFly server, right-click and choose `Run As` --> `Run on Server`.
4. To access the application, right-click and choose `Run As` --> `Maven Build`.
5. Enter "ejb-security-interceptors" for the `Name`, enter "exec:exec" for the `Goals:`, and click `Run`.
6. Review the output in the console window.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

        mvn dependency:sources

