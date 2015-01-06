ejb-security-interceptors:  Using client and server side interceptors to switch the identity for an EJB call.
====================
Author: Darran Lofthouse
Level: Advanced
Technologies: EJB, Security
Summary: Demonstrates how interceptors can be used to switch the identity for EJB calls on a call by call basis.
Target Project: EAP
Source: <https://github.com/wildfly/quickstart/>

What is it?
-----------

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

JBoss WildFly allow client side interceptors for EJB invocations. Such interceptors are expected to implement the `org.jboss.ejb.client.EJBClientInterceptor` interface. User applications can then plug in such interceptors in the 'EJBClientContext' either programatically or through the ServiceLoader mechanism.

- The programmatic way involves calling the `org.jboss.ejb.client.EJBClientContext.registerInterceptor(int order, EJBClientInterceptor interceptor)` API and passing the 'order' and the 'interceptor' instance. The 'order' is used to decide where exactly in the client interceptor chain, this 'interceptor' is going to be placed.
- The ServiceLoader mechanism is an alternate approach which involves creating a `META-INF/services/org.jboss.ejb.client.EJBClientInterceptor` file and placing/packaging it in the classpath of the client application. The rules for such a file are dictated by the [Java ServiceLoader Mechanism](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). This file is expected to contain in each separate line the fully qualified class name of the EJB client interceptor implementation, which is expected to be available in the classpath. EJB client interceptors added via the ServiceLoader mechanism are added to the end of the client interceptor chain, in the order they were found in the classpath.

This quickstart uses the ServiceLoader mechanism for registering the EJB client interceptor and places the `META-INF/services/org.jboss.ejb.client.EJBClientInterceptor` in the classpath, with the following content:

	# EJB client interceptor(s) that will be added to the end of the interceptor chain during an invocation
	# on EJB. If these interceptors are to be added at a specific position, other than last, then use the
	# programmatic API in the application to register it explicitly to the EJBClientContext

	org.jboss.as.quickstarts.ejb_security_interceptors.ClientSecurityInterceptor


System requirements
-------------------

All you need to build this project is Java 6.0 (Java SDK 1.6) or better, Maven 3.0 or better.

The application this project produces is designed to be run on JBoss WildFly 8.0.1.Final 

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.

Prerequisites
-------------

_Note_: Unlike most of the quickstarts, this one requires JBoss WildFly 8.0.1.Final or later.

This quickstart uses the default standalone configuration plus the modifications described here.

It is recommended that you test this approach in a separate and clean environment before you attempt to port the changes in your own environment.

Configure the JBoss WildFly server
---------------------------

These steps assume that you are running the server in standalone mode and using the default standalone.xml supplied with the distribution.

_NOTE - Before you begin:_

1. If it is running, stop the JBoss WildFly Server.
2. Backup the file: `WILDFLY_HOME/standalone/configuration/standalone.xml`
3. After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.


1. Start the JBoss WildFly Server by typing the following: 

        For Linux:  WILDFLY_HOME/bin/standalone.sh 
        For Windows:  WILDFLY_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing JBOSS_HOME with the path to your server:

        WILDFLY_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
This script adds the `quickstart-domain` domain to the `security` subsystem in the server configuration and configures authentication access. You should see the following result when you run the script:

        #1 /subsystem=security/security-domain=quickstart-domain:add(cache-type=default)
        #2 /subsystem=security/security-domain=quickstart-domain/authentication=classic:add
        #3 /subsystem=security/security-domain=quickstart-domain/authentication=classic/login-module=DelegationLoginModule:add(code=org.jboss.as.quickstarts.ejb_security_interceptors.DelegationLoginModule,flag=optional,module-options={password-stacking=useFirstPass})
        #4 /subsystem=security/security-domain=quickstart-domain/authentication=classic/login-module=Remoting:add(code=Remoting,flag=optional,module-options={password-stacking=useFirstPass})
        #5 /subsystem=security/security-domain=quickstart-domain/authentication=classic/login-module=RealmDirect:add(code=RealmDirect,flag=required,module-options={password-stacking=useFirstPass})
        #6 /core-service=management/security-realm=ejb-outbound-realm:add
        #7 /core-service=management/security-realm=ejb-outbound-realm/server-identity=secret:add(value="Q29ubmVjdGlvblBhc3N3b3JkMSE=")
        #8 /socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=ejb-outbound:add(host=localhost,port=8080)
        #9 /subsystem=remoting/remote-outbound-connection=ejb-outbound-connection:add(outbound-socket-binding-ref=ejb-outbound,username=ConnectionUser,security-realm=ejb-outbound-realm)
        #10 /subsystem=remoting/remote-outbound-connection=ejb-outbound-connection/property=SSL_ENABLED:add(value=false)
        The batch executed successfully.
        {"outcome" => "success"}

###The Delegation Login Module

This login module will load the properties file `delegation-mapping.properties` from the deployment. The location of this properties file can be overridden with the module-option `delegationProperties`.

At runtime, this login module is used to decide if the user of the connection to the server is allowed to ask that the request is executed as the specified user.

There are four variations of how the key can be specified in the properties file: 

 - `user@realm`        - Exact match of user and realm.
 - `user@*`            - Allow a match of user for any realm.
 - `*@realm`           - Match for any user in the realm specified.
 - `*`                 - Match for all users in all realms.

When a request is received that involves switching the user, the identity of the user that opened the connection is used to check the properties file for an entry. The check is performed in the order listed above until the first match is found. Once a match is found, further entries that could match are not read.

The value in the properties file can either be a wildcard `*` or it can be a comma separated list of users. Be aware that in the value/mapping side there is no notion of the realm.

For this quickstart we use the following entry: 

	ConnectionUser@ApplicationRealm=AppUserOne,AppUserTwo

This means that the ConnectionUser added above can only ask that a request is executed as either AppUserOne or AppUserTwo. It is not allowed to ask to be executed as AppUserThree.

All users are permitted to execute requests as themselves, as in that case, the login module is not called. That is the default behavior that exists without the addition of the interceptors in this quickstart.

Add the Application Users
---------------

This quickstart is built around the default `ApplicationRealm` as configured in the JBoss Enterprise Application  Platform 6.1 or JBoss AS 7.2 server distribution. Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| ConnectionUser| ApplicationRealm | ConnectionPassword1!| User |
| AppUserOne | ApplicationRealm | AppPasswordOne1! | User, RoleOne |
| AppUserTwo | ApplicationRealm | AppPasswordTwo1! | User, RoleTwo |
| AppUserThree | ApplicationRealm | AppPasswordThree1! | User, RoleOne, RoleTwo |

The first user establishes the actual connection to the server. The subsequent two users demonstrate how to switch identities on demand.  The final user can access everything but can not participate in identity switching.

Also do note that within the quickstart we do not make use of the passwords for any of the 'App' users, the passwords specified for those users are a suggestion for values that will be accepted by the ass-user utility.

For an example of how to use the add-user utility, see instructions in the root README file located here: [Add User](../README.md#addapplicationuser).

### Add Users Manually

Alternatively you can edit the properties file for the users and manually add the required entries:

1. Add the user accounts by editing the file `WILDFLY_HOME/standalone/configuration/application-users.properties` and pasting in the following lines:

		ConnectionUser=90dbe097e651ce2e84d5fa9d6463ed3d
		AppUserOne=a2ad45ac5d53d6c0db68262b94deafda
		AppUserTwo=3a39e0b35ad46d625b45dacfe708777a
		AppUserThree=5dc106218f4c01b8ff5525cf8e2d1568

2. Add the users roles by editing the file `WILDFLY_HOME/standalone/configuration/application-roles.properties` and pasting in the following lines: -

		ConnectionUser=User
		AppUserOne=User,RoleOne
		AppUserTwo=User,RoleTwo
		AppUserThree=User,RoleOne,RoleTwo

The application server checks the properties files for modifications at runtime so there is no need to restart the server after changing these files.


Start JBoss WildFly
-------------------------

1. Open a command line and navigate to the root of the WildFly server directory.
2. The following shows the command line to start the server with the web profile:

		For Linux:   WILDFLY_HOME/bin/standalone.sh
		For Windows: WILDFLY_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

		mvn clean package wildfly:deploy

4. This will deploy `target/jboss-as-ejb-security-interceptors.jar` to the running instance of the server.


Run the client
---------------------

The step here assumes you have already successfully deployed the EJBs to the server in the previous step and that your command prompt is still in the same folder.

1.  Type this command to execute the client:

		mvn exec:exec


Investigate the Console Output
----------------------------

When you run the `mvn exec:exec` command, you see the following output.

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


Investigate the Server Console Output
----------------------------

Look at the JBoss Application Server console or Server log and you should see exceptions (the stacktraces were not included here) in the log like the following:

    09:16:01,133 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 6) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,198 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,215 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 4) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,224 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 1) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,267 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 8) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,282 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 4) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,297 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 10) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,316 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,332 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 1) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.getSecurityInformation(): javax.ejb.EJBAccessException: JBAS013323: Invalid User
    09:16:01,344 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 3) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,356 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 10) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,370 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,378 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 2) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,419 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 6) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,435 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 9) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleTwoMethod() of bean: SecuredEJB is not allowed
    09:16:01,458 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 7) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,481 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 10) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod(): javax.ejb.EJBAccessException: JBAS014502: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.roleOneMethod() of bean: SecuredEJB is not allowed
    09:16:01,496 ERROR [org.jboss.as.ejb3.invocation] (EJB default - 2) JBAS014134: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_interceptors.SecuredEJBRemote.getSecurityInformation(): javax.ejb.EJBAccessException: JBAS013323: Invalid User


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

		mvn wildfly:undeploy


Remove the Security Domain Configuration
----------------------------

You can remove the security domain configuration by running the  `remove-security-domain.cli` script provided in the root directory of this quickstart.

### Remove the Security Domain Configuration by Running the JBoss CLI Script

1. Start the JBoss WildFly Server by typing the following: 

        For Linux:  WILDFLY_HOME/bin/standalone.sh
        For Windows:  WILDFLY_HOME\bin\standalone.bat
2. Open a new command line, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        WILDFLY_HOME/bin/jboss-cli.sh --connect --file=remove-security-domain.cli 
This script removes the `quickstart-domain` domain from the `security` subsystem and the outbound remoting connection in the server configuration. You should see the following result when you run the script:

        #1 /subsystem=remoting/remote-outbound-connection=ejb-outbound-connection:remove
        #2 /socket-binding-group=standard-sockets/remote-destination-outbound-socket-binding=ejb-outbound:remove
        #3 /core-service=management/security-realm=ejb-outbound-realm:remove
        #4 /subsystem=security/security-domain=quickstart-domain:remove
        The batch executed successfully.
        {"outcome" => "success"}

Run the Quickstart in JBoss Developer Studio or Eclipse
-------------------------------------

You can also start the server and deploy the quickstarts from Eclipse using JBoss tools. For more information, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](../README.md#useeclipse) 


Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc
