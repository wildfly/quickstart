# ejb-security-context-propagation: Demonstrate security context propagation in EJB to remote EJB calls

Author: Stefan Guilhen  
Level: Advanced  
Technologies: EJB, Security  
Summary: The `ejb-security-context-propagation` quickstart demonstrates how the security context can be propagated to a remote EJB using a remote outbound connection configuration  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security-context-propagation` quickstart demonstrates how the security context of an EJB can be propagated to a
remote EJB in ${product.name.full}.

The quickstart makes use of two EJBs, `SecuredEJB` and `IntermediateEJB`, to verify that the security context propagation is
correct, and a `RemoteClient` standalone client.

### SecuredEJB

The `SecuredEJB` has four methods:

    String getSecurityInformation();
    String guestMethod();
    String userMethod();
    String adminMethod();

The first method can be called by all users that are created in this quickstart. The purpose of this method is to return a
String containing the name of the Principal that called the EJB along with the user's authorized role information, for example:

    [Principal=[quickstartUser], In role [guest]=true, In role [user]=true, In role [admin]=false]

The next three methods are annotated to require that the calling user is authorized for roles `guest`, `user` and `admin` respectively.

### IntermediateEJB

The `IntermediateEJB` contains a single method. Its purpose is to make use of a remote connection and invoke each of the
methods on the `SecuredEJB`. A summary is then returned with the outcome of the calls.

### RemoteClient

Finally there is the `RemoteClient` stand-alone client. The client makes calls using the identity of the established connection.

In the real world, remote calls between servers in the servers-to-server scenario would truly be remote and separate. For
the purpose of this quickstart, we make use of a loopback connection to the same server so we do not need two servers just
to run the test.

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version}.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on
how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Prerequisites

This quickstart uses the default standalone configuration plus the modifications described here.

It is recommended that you test this approach in a separate and clean environment before you attempt to port the changes in your own environment.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The
installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Add the Application Users

This quickstart uses secured management interfaces and is built around the default `ApplicationRealm` as configured in the ${product.name} distribution. You must create the following application user to access the running application:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest,user |
| superUser| ApplicationRealm | superPwd1!| guest,user,admin |

To add the user, open a command prompt and type the following commands:

    For Linux:
      ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest,user'
      ${jboss.home.name}/bin/add-user.sh -a -u 'superUser' -p 'superPwd1!' -g 'guest,user,admin'

    For Windows:
      ${jboss.home.name}\bin\add-user.bat -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest,user'
      ${jboss.home.name}\bin\add-user.bat -a -u 'superUser' -p 'superPwd1!' -g 'guest,user,admin'

The `quickstart` user establishes the actual connection to the server.

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
        process-state: reload-required

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

    The `application-security-domain` essentially enables Elytron security for the quickstart EJBs. It maps the `quickstart-domain` that was set in the EJBs via annotation to the Elytron `ApplicationDomain` that will be responsible for authenticating and     authorizing access to the EJBs.
2. The following `ejb-outbound-configuration` authentication configuration and `ejb-outbound-context` authentication context were added to the `elytron` subsystem:

        <authentication-configuration name="ejb-outbound-configuration" security-domain="ApplicationDomain" sasl-mechanism-selector="PLAIN"/>
        <authentication-context name="ejb-outbound-context">
            <match-rule authentication-configuration="ejb-outbound-configuration"/>
        </authentication-context>

    The `ejb-outbound-configuration` contains the authentication configuration that will be used when invoking a method on a remote EJB, for example when `IntermediateEJB` calls the methods on the `SecuredEJB`. The above configuration specifies that the identity that is currently
    authenticated to the `ApplicationDomain` will be used to establish the connection to the remote EJB. The `sasl-mechanism-selector`
    defines the SASL mechanisms that should be tried. In this quickstart the `PLAIN` mechanism has been chosen because other     challenge-response mechanisms such as `DIGEST-MD5` can't provide the original credential to establish the connection to
    the remote EJB.

    The `ejb-outbound-context` is the authentication context that is used by the remote outbound connection and it automatically     selects the `ejb-outbound-configuration`.

3. The following `ejb-outbound` outbound-socket-binding connection was created within the `standard-sockets` socket-binding-group:

        <outbound-socket-binding name="ejb-outbound">
            <remote-destination host="localhost" port="8080"/>
        </outbound-socket-binding>

    For the purpose of the quickstart we just need an outbound connection that loops back to the same server. This will be sufficient
    to demonstrate the server-to-server capabilities.
4. The following `ejb-outbound-connection` remote-outbound-connection was added to the outbound-connections within the `remoting` subsytem:

        <outbound-connections>
            <remote-outbound-connection name="ejb-outbound-connection" outbound-socket-binding-ref="ejb-outbound" authentication-context="ejb-outbound-context"/>
        </outbound-connections>
5. The `http-connector` in the `remoting` subsystem was updated to use the `application-sasl-authentication` authentication factory. It allows for the identity that was established in the connection authentication to be propagated to the components.

        <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ApplicationRealm" sasl-authentication-factory="application-sasl-authentication"/>

6. Finally, the `application-sasl-authentication` factory was updated in the `elytron` subsystem to include the `PLAIN` mechanism:

        <sasl-authentication-factory name="application-sasl-authentication" sasl-server-factory="configured" security-domain="ApplicationDomain">
            <mechanism-configuration>
                <mechanism mechanism-name="PLAIN"/>
                <mechanism mechanism-name="JBOSS-LOCAL-USER" realm-mapper="local"/>
                <mechanism mechanism-name="DIGEST-MD5">
                    <mechanism-realm realm-name="ApplicationRealm"/>
                </mechanism>
            </mechanism-configuration>
        </sasl-authentication-factory>

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

        mvn clean package wildfly:deploy

4. This will deploy `target/${project.artifactId}.jar` to the running instance of the server.

## Run the Client

Before you run the client, make sure you have already successfully deployed the EJBs to the server in the previous step and that your command prompt is still in the same folder.

Type this command to execute the client:

    mvn exec:exec

## Investigate the Console Output

When you run the `mvn exec:exec` command, you see the following output. Note there may be other log messages interspersed between these.

        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


        * * IntermediateEJB - Begin Testing with principal quickstartUser * *

        Remote Security Information: [Principal=[quickstartUser], In role [guest]=true, In role [user]=true, In role [admin]=false]
        Can invoke guestMethod? true
        Can invoke userMethod? true
        Can invoke adminMethod? false

        * * IntermediateEJB - End Testing * *


        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


        * * IntermediateEJB - Begin Testing with principal superUser * *

        Remote Security Information: [Principal=[superUser], In role [guest]=true, In role [user]=true, In role [admin]=true]
        Can invoke guestMethod? true
        Can invoke userMethod? true
        Can invoke adminMethod? true

        * * IntermediateEJB - End Testing * *


        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


As can be seen from the output the identities authenticated to the intermediate EJB were propagated all the way to the remote
secured EJB and their roles have been correctly evaluated.

## Investigate the Server Console Output

If you chose not to run the script to suppress system exceptions, you should see the following exceptions in the ${product.name}
server console or log. The exceptions are logged for each of the tests where a request is rejected because the user is not
authorized.

        12:26:06,556 ERROR [org.jboss.as.ejb3.invocation] (default task-57) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_context_propagation.SecuredEJBRemote.adminMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract java.lang.String org.jboss.as.quickstarts.ejb_security_context_propagation.SecuredEJBRemote.adminMethod() of bean: SecuredEJB is not allowed
            at org.jboss.as.ejb3.security.RolesAllowedInterceptor.processInvocation(RolesAllowedInterceptor.java:67)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.security.SecurityDomainInterceptor.processInvocation(SecurityDomainInterceptor.java:44)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.deployment.processors.StartupAwaitInterceptor.processInvocation(StartupAwaitInterceptor.java:22)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.component.interceptors.ShutDownInterceptorFactory$1.processInvocation(ShutDownInterceptorFactory.java:64)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.deployment.processors.EjbSuspendInterceptor.processInvocation(EjbSuspendInterceptor.java:57)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.component.interceptors.LoggingInterceptor.processInvocation(LoggingInterceptor.java:67)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ee.component.NamespaceContextInterceptor.processInvocation(NamespaceContextInterceptor.java:50)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.as.ejb3.component.interceptors.AdditionalSetupInterceptor.processInvocation(AdditionalSetupInterceptor.java:54)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.invocation.ContextClassLoaderInterceptor.processInvocation(ContextClassLoaderInterceptor.java:60)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.invocation.InterceptorContext.run(InterceptorContext.java:438)
            at org.wildfly.security.manager.WildFlySecurityManager.doChecked(WildFlySecurityManager.java:609)
            at org.jboss.invocation.AccessCheckingInterceptor.processInvocation(AccessCheckingInterceptor.java:57)
            at org.jboss.invocation.InterceptorContext.proceed(InterceptorContext.java:422)
            at org.jboss.invocation.ChainedInterceptor.processInvocation(ChainedInterceptor.java:53)
            at org.jboss.as.ee.component.ViewService$View.invoke(ViewService.java:198)
            at org.wildfly.security.auth.server.SecurityIdentity.runAsFunctionEx(SecurityIdentity.java:380)
            at org.jboss.as.ejb3.remote.AssociationImpl.invokeWithIdentity(AssociationImpl.java:492)
            at org.jboss.as.ejb3.remote.AssociationImpl.invokeMethod(AssociationImpl.java:487)
            at org.jboss.as.ejb3.remote.AssociationImpl.lambda$receiveInvocationRequest$0(AssociationImpl.java:188)
            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
            at java.lang.Thread.run(Thread.java:745)

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Restore the Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory of this quickstart or by manually restoring the backup copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script reverts the changes made to the `ejb3`, `elytron` and `remoting` subsystems. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required
3. If you choose to run the script to suppress system exceptions, run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-system-exception.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-system-exception.cli

     You should see the following result when you run the script:

        The batch executed successfully

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the backup copy of the file.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).


This quickstart requires additional configuration and deploys and runs differently in JBoss Developer Studio than the other quickstarts.

1. Be sure to [Add the Application Users](#add-the-application-users) as described above.
2. Follow the steps above to [Configure the Server](#configure-the-server). Stop the server at the end of that step.
3. To deploy the application to the ${product.name} server, right-click on the `ejb-security-context-propagation` project and choose `Run As` --> `Run on Server`.
4. To access the application, right-click on the `ejb-security-context-propagation` project and choose `Run As` --> `Java Application`.
5. Choose `RemoteClient - org.jboss.as.quickstarts.ejb_security_context_propagation` and click `OK`.
6. Review the output in the console window.
7. Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
