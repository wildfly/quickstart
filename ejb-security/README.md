# ejb-security:  Using Java EE Declarative Security to Control Access

Author: Sherif F. Makary  
Level: Intermediate  
Technologies: EJB, Security  
Summary: The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and EJBs in ${product.name.full}.

This quickstart takes the following steps to implement EJB security:

1. Add `application-security-domain` mappings in the `ejb3` and `undertow` subsystems to enable Elytron security for the quickstart EJB and Web components.
2. Add the `@SecurityDomain("other")` security annotation to the EJB declaration to tell the EJB container to apply authorization to this EJB.
3. Add the `@RolesAllowed({ "guest" })` annotation to the EJB declaration to authorize access only to users with `guest` role access rights.
4. Add the `@RolesAllowed({ "guest" })` annotation to the Servlet declaration to authorize access only to users with `guest` role access rights.
5. Add a `<login-config>` security constraint to the `WEB-INF/web.xml` file to force the login prompt.
6. Add an application user with `guest` role access rights to the EJB. This quickstart defines a user `quickstartUser` with password `quickstartPwd1!` in the `guest` role. The `guest` role matches the allowed user role defined in the `@RolesAllowed` annotation in the EJB.
7. Add a second user that has no `guest` role access rights.


## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on
 how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Add the Application Users

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |
| user1 | ApplicationRealm | password1! | app-user |

The first application user has access rights to the application. The second application user is not authorized to access the application.

To add the application users, open a command prompt and type the following commands:

        For Linux:        
          ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}/bin/add-user.sh -a -u 'user1' -p 'password1!' -g 'app-user'

        For Windows:
          ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}\bin\add-user.bat -a -u 'user1' -p 'password1!' -g 'app-user'

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).

## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-elytron.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-elytron.cli` file in the root of this quickstart directory. This script adds the configuration that enables Elytron security for the quickstart components. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-elytron.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-elytron.cli
    You should see the following result when you run the script:

        The batch executed successfully
5. Stop the ${product.name} server.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `application-security-domain` mapping was added to the `ejb3` subsystem:

        <application-security-domains>
            <application-security-domain name="other" security-domain="ApplicationDomain"/>
        </application-security-domains>

    The `application-security-domain` essentially enables Elytron security for the quickstart EJBs. It maps the `other` security domain that was set in the EJBs via annotation to the Elytron `ApplicationDomain` that will be responsible for authenticating and authorizing access to the EJBs.
2. The following `application-security-domain` mapping was added to the `undertow` subsystem:

        <application-security-domains>
            <application-security-domain name="other" http-authentication-factory="application-http-authentication"/>
        </application-security-domains>

   The mapping in the `undertow` subsystem is similar to the one in the `ejb3` subsystem but in this case it maps the `other` security domain to the `http-authentication-factory` that will be used to authenticate and authorize access to the Web components.

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

## Access the Application

The application will be running at the following URL <http://localhost:8080/${project.artifactId}/>.

When you access the application, you are presented with a browser login challenge.

1. If you attempt to login with a user name and password combination that has not been added to the server, the login challenge will be redisplayed.
2. When you login successfully using `quickstartUser`/`quickstartPwd1!`, the browser displays the following security info:

        Successfully called Secured EJB

        Principal : quickstartUser
        Remote User : quickstartUser
        Authentication Type : BASIC

3. Now close and reopen the brower session and access the application using the `user1`/`password1!` credentials. In this case, the Servlet, which only allows the `guest` role, restricts the access and you get a security exception similar to the following:

        HTTP Status 403 - Access to the requested resource has been denied

        type Status report
        message Access to the requested resource has been denied
        description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.

4. Next, change the EJB (SecuredEJB.java) to a different role, for example, `@RolesAllowed({ "other-role" })`. Do not modify the `guest` role in the Servlet (SecuredEJBServlet.java). Build and redeploy the quickstart, then close and reopen the browser and login using `quickstartUser`/`quickstartPwd1!`. This time the Servlet will allow the `guest` access, but the EJB, which only allows the role `other-role`, will throw an EJBAccessException:

        HTTP Status 500

        message
        description  The server encountered an internal error () that prevented it from fulfilling this request.
        exception
        javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public java.lang.String org.jboss.as.quickstarts.ejb_security.SecuredEJB.getSecurityInfo() of bean: SecuredEJB is not allowed


## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Restore the Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script reverts the changes made to the `ejb3` and `undertow` subsystems. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to [Add the Application Users](#add-the-application-users) as described above.
* Be sure to configure the server by running the JBoss CLI script as described above under [Configure the Server](#configure-the-server).
* To deploy the server project, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run on Server`.
* You are presented with a browser login challenge. Enter the credentials as described above to access and test the running application.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
