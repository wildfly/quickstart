# ejb-security:  Using Java EE Declarative Security to Control Access

Author: Sherif F. Makary, Stefan Guilhen  
Level: Intermediate  
Technologies: EJB, Security  
Summary: The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to EJBs in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security` quickstart demonstrates the use of Java EE declarative security to control access to EJBs in ${product.name.full}.

This quickstart takes the following steps to implement EJB security:

1. Add an `application-security-domain` mapping in the `ejb3` subsystem to enable Elytron security for the `SecuredEJB`.
2. Add the `@SecurityDomain("other")` security annotation to the EJB declaration to tell the EJB container to apply authorization to this EJB.
3. Add the `@RolesAllowed({ "guest" })` annotation to the EJB declaration to authorize access only to users with `guest` role access rights.
4. Add the `@RolesAllowed({ "admin" })` annotation to the administrative method in the `SecuredEJB` to authorize access only
to users with `admin` role access rights.
5. Add an application user with `guest` role access rights to the EJB. This quickstart defines a user `quickstartUser` with
password `quickstartPwd1!` in the `guest` role. The `guest` role matches the allowed user role defined in the `@RolesAllowed`
annotation in the EJB but it should not be granted access to the administrative method annotated with `RolesAllowed({"admin"})`.

## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on how to install and run ${product.name}, see the ${product.name.full} [_Getting Started Guide_](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) located on the Customer Portal.

You can also [run the quickstart in Red Hat JBoss Developer Studio or Eclipse](#run-the-quickstart-in-red-hat-jboss-developer-studio-or-eclipse).

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The
installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Add the Application Users

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |

The application user has `guest` access rights to the application but no `admin` rights.

To add the application users, open a command prompt and type the following commands:

    For Linux:
      ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

    For Windows:
      ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'

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
3. Review the `configure-elytron.cli` file in the root of this quickstart directory. This script adds the configuration that enables Elytron security for the quickstart components. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-elytron.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-elytron.cli
    You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

5. Stop the ${product.name} server.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `application-security-domain` mapping was added to the `ejb3` subsystem:

        <application-security-domains>
            <application-security-domain name="other" security-domain="ApplicationDomain"/>
        </application-security-domains>

    The `application-security-domain` essentially enables Elytron security for the quickstart EJBs. It maps the `other` security domain that was set in the EJBs via annotation to the Elytron `ApplicationDomain` that will be responsible for authenticating and authorizing access to the EJBs.
2. The `http-remoting-connector` in the `remoting` subsystem was updated to use the `application-sasl-authentication` factory:

            <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ApplicationRealm" sasl-authentication-factory="application-sasl-authentication"/>

    This configuration allows for the identity that was established at the connection level to be propagated to the components.

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

Before you run the client, make sure you have already successfully deployed the EJBs to the server in the previous step and that your command prompt is still in the root directory of this quickstart.

Type this command to execute the client:

    mvn exec:exec

## Investigate the Console Output

When you run the `mvn exec:exec` command, you see the following output. Note there may be other log messages interspersed between these messages.

        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


        Successfully called secured bean, caller principal quickstartUser

        Principal has admin permission: false


        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

The username and credentials used to establish the connection to the application server are configured in the `wildfly-config.xml` file. As expected, the `quickstartUser` was able to invoke the method available for the `guest`role, but not the administrative method that requires the `admin` role.

_NOTE:_ You should also see the following `EJBAccessException` printed in the server log, followed by a stack trace. This is to be expected because the user does not have the correct permissions to access the EJB.

    07:00:15,364 ERROR [org.jboss.as.ejb3.invocation] (default task-38) WFLYEJB0034: EJB Invocation failed on component SecuredEJB for method public abstract boolean org.jboss.as.quickstarts.ejb_security.SecuredEJBRemote.administrativeMethod(): javax.ejb.EJBAccessException: WFLYEJB0364: Invocation on method: public abstract boolean org.jboss.as.quickstarts.ejb_security.SecuredEJBRemote.administrativeMethod() of bean: SecuredEJB is not allowed


As an exercise, you can rerun the `add-user` script described in the [Add the Application Users](#add-the-application-users) section, but this time grant the `quickstartUser` the admin role as follows:

        For Linux:
          ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest,admin'

        For Windows:
          ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest,admin'

After you update the `quickstartUser` user role, you must restart the server for it to take effect. Running the client again should immediately reflect the new permission level of the user:

        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


        Successfully called secured bean, caller principal quickstartUser

        Principal has admin permission: true


        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


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

1. Be sure to [Add the Application Users](#add-the-application-users) as described above.
2. Be sure to configure the server by running the JBoss CLI script as described above under [Configure the Server](#configure-the-server).
3. To deploy the server project, right-click on the `${project.artifactId}` project and choose `Run As` --> `Maven build`. Enter `clean package wildfly:deploy` for the `Goals:` and click `Run`. This deploys the `${project.artifactId}` JAR to the ${product.name} server.
4. Right-click on the `${project.artifactId}` project and choose `Run As` --> `Run Configurations`. Enter `exec:exec` for the `Goals`, and then click `Run`.
5. Review the output in the console window. You should see the same results as when running Maven in the command line.
6. To undeploy the project, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run Configurations`. Enter `wildfly:undeploy` for the `Goals` and click `Run`.
7. Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
