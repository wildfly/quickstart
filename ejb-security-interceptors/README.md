ejb-security-interceptors:  Using client and server side interceptors to switch the identity for an EJB call.
====================
Author: Darran Lofthouse
Level: Advanced
Technologies: EJB, Security
Summary: Demonstrates how interceptors can be used to switch the identity for EJB calls on a call by call basis.
Target Product: EAP

What is it?
-----------

By default for Remote calls to EJBs deployed to the application server the connection to the server is authenticated
and then any request received over this connection is executed as the identity which authenticated the connection.  This
is true for both client to server and server to server calls, where different identities are required from the same client
this requires multiple connections to be opened to the server - each authenticated as a different identity.

This quickstart offers an alternative solution where the identity used to authenticate the connection is given the permission
to execute a request as a different user, this is achieved with the addition of the following three components: -
 * A client side interceptor to pass the requested identity to the remote server.
 * A server side interceptor to receive the identity and request that the call switches to that identity.
 * A JAAS LoginModule to decide if the user of the connection is allowed to execute requests as the user specified.
 
The quickstart then makes use of two EJBs to verify that the propagation and identity switching is happening as required,
the first of these is the SecuredEJB which has three methods: -

    String getSecurityInformation();
    boolean roleOneMethod();
    boolean roleTwoMethod();

The first of these methods can be called by all users that are created in the steps below, the purpose of the method is to 
return a String containing the name of the Principal that called the EJB and also the results of checking if the user has the
roles 'RoleOne' and 'RoleTwo' e.g.

   [Principal={ConnectionUser}, In role {User}=true, In role {RoleOne}=false, In role {RoleTwo}=false]
   
The next two methods are annotated to require that the calling user has roles 'RoleOne' and 'RoleTwo' respectively.

The next EJB is the 'IntermediateEJB', this EJB contains a single method and it's purpose is to make use of a remote connection 
and invoke each of the methods on the 'SecuredEJB' - a summary is then returned with the outcome of the calls.

Finally the class 'RemoteClient' is a stand alone client to make the calls, the client makes calls using the identity of 
the connection established and also makes calls switching the identity to the users that are subsequently defined.

One point to keep in mind is that for the server to server propagation scenarios in normal circumstances this would be to 
servers that are truely remote to each other - however for the purpose of the quickstart we make use of a loopback connection
to the same server so we don't need two servers just to run the test.               

System requirements
-------------------

This quick start is based around JBoss AS 7.2.0 or JBoss EAP 6.1.0 using the default standalone configuration plus the
modifications described here.

If you are reviewing this quickstart with a view to making use of this approach within your own environment it is still
recommended to try this using a clean installation first and then porting to your own environment. 


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](../README.md#mavenconfiguration) before testing the quickstarts.


Add the Application Users
---------------

This quick start is built around the default 'ApplicationRealm' as configured in the AS7 / EAP 6 distribution, the following four 
users should be added using the add-user utility.

'ConnectionUser' with role 'User' and password 'ConnectionPassword1!'.
'AppUserOne' with roles 'User' and 'RoleOne', any password can be specified for this user.
'AppUserTwo' with roles 'User' and 'RoleTwo', again any password can be specified for this user.
'AppUserThree' with roles 'User', 'RoleOne', and 'RoleTwo' and again any password.  

The first user is used for establishing the actual connection to the server, the subsequent two users are the users that this
quickstart demonstrates can be switched to on demand.  The final user is a user that can access everything but can not be switched to.

Add the LoginModule
---------------

The EJB side of this quick start makes use of the 'other' security domain which by default delegates to the 'ApplicationRealm',
in order to support identity switching an additional login module needs to be added to the domain definition.

  <login-module code="org.jboss.as.quickstarts.ejb_security_interceptors.DelegationLoginModule" flag="optional">
    <module-option name="password-stacking" value="useFirstPass"/>
  </login-module>
  
This login module can either be added before or after the existing 'Remoting' login module in the domain but it MUST be somewhere before
the existing RealmDirect login module.  

If this approach is used and the majority of requests will involve an identity switch then it would recommended to have this module as
the first module in the list, however if the majority of requests will run as the connection user with occasional switches it would
be recommended ot place the 'Remoting' login module first and this one second.

This login module will load the properties file 'delegation-mapping.properties' from the deployment, the location of this properties
file can be overridden with the module-option 'delegationProperties'.

At runtime this login module is used to decide if the user of the connection to the server is allowed to ask that the request is executed
as the specified user.

There are four variations of how the key can be specified in the properties file: -

 - user@realm        - Exact match of user and realm.
 - user@*            - Allow a match of user for any realm.
 - *@realm           - Match for any user in the realm specified.
 - *                 - Match for all users in all realms.
 
When a request is received that involves switching the user the identity of the user that opened the connection is used to 
check the properties file for an entry, the check is performed in the order listed above until the first match is found - once
a match is found further entries that could match are not read.

The value in the properties file can either be a wildcard '*' or it can be a comma separated list of users, do be aware
that in the value/mapping side there is no notion of the realm.

For this quick start we use the following entry: -

  ConnectionUser@ApplicationRealm=AppUserOne,AppUserTwo
  
This means that the ConnectionUser added above can only ask that a request is executed as either AppUserOne or AppUserTwo, it is not
allowed to ask for it to be executed as AppUserThree.

All users are permitted to execute requests as themselves as in that case the login module is not called, that is the default behaviour
that exists without the addition of the interceptors in this quick start.    

* Further Use *

Taking this further the DelegationLoginModule can be extended to provide custom delegation checks, one thing not currently 
checked is if the user being switched to actually exists, if the module is extended the following method can be overridden to
provide a custom check.

  protected boolean delegationAcceptable(String requestedUser, OuterUserCredential connectionUser);   

Server to Server Connection
-------------------------

For the purpose of the quickstart we just need an outbound connection that loops back to the same server, this will be
sufficient to demonstrate the server to server capabilities.

Add the following security realm, note the Base64 password is for the ConnectionUser account created above.

   <security-realm name="ejb-outbound-realm">
      <server-identities>
         <secret value="Q29ubmVjdGlvblBhc3N3b3JkMSE="/>
      </server-identities>
   </security-realm>
            
Within the socket-binding-group 'standard-sockets' add the following outbound connection: -

   <outbound-socket-binding name="ejb-outbound">
      <remote-destination host="localhost" port="4447"/>
   </outbound-socket-binding>          

Within the Remoting susbsytem add the following outbound connection: -

   <outbound-connections>
      <remote-outbound-connection name="ejb-outbound-connection" outbound-socket-binding-ref="ejb-outbound" security-realm="ejb-outbound-realm" username="ConnectionUser">
         <properties>
            <property name="SSL_ENABLED" value="false"/>
         </properties>
      </remote-outbound-connection>
   </outbound-connections>

Start JBoss Enterprise Application Platform 6 or JBoss AS 7
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat


Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](../README.md#buildanddeploy) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package jboss-as:deploy

4. This will deploy `target/jboss-as-ejb-security-interceptors.jar` to the running instance of the server.


Run the client 
---------------------

The step here assumes you have already successfully deployed the EJBs to the server in the previous step
and that your command prompt is still in the same folder.

1.  Type this command to execute the client:

        mvn exec:exec
        
        

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
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

    mvn dependency:sources
    mvn dependency:resolve -Dclassifier=javadoc
