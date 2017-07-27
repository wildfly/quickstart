# ejb-security-programmatic-auth: Using the programmatic API to invoke a remote EJB using different identities

Author: Stefan Guilhen  
Level: Intermediate  
Technologies: EJB, Security  
Summary: The `ejb-security-programmatic-auth` quickstart demonstrates how to programmatically setup different identities when 
invoking a remote secured EJB.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security-programmatic-auth` quickstart demonstrates how to invoke a remote secured EJB  using the `Elytron` client
API to establish different identities. The quickstart client application accomplishes that by looking up and invoking the
secured EJB under different `AuthenticationContext`s. Each context is setup to use a different identities and credentials.

## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on
 how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The
installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Add the Application Users

Using the add-user utility script, you must add the following users to the `ApplicationRealm`:

| **UserName** | **Realm** | **Password** | **Roles** |
|:-----------|:-----------|:-----------|:-----------|
| quickstartUser| ApplicationRealm | quickstartPwd1!| guest |
| superUser | ApplicationRealm | superPwd1! | guest,admin |

The first application user has `guest` access rights to the application but no `admin` rights. The second user has both rights.

To add the application users, open a command prompt and type the following commands:

        For Linux:        
          ${jboss.home.name}/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}/bin/add-user.sh -a -u 'superUser' -p 'superPwd1!' -g 'guest,admin'

        For Windows:
          ${jboss.home.name}\bin\add-user.bat  -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest'
          ${jboss.home.name}\bin\add-user.bat -a -u 'superUser' -p 'superPwd1!' -g 'guest,admin'

If you prefer, you can use the add-user utility interactively.
For an example of how to use the add-user utility, see the instructions located here: [Add an Application User](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CREATE_USERS.md#add-an-application-user).

## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands
into a `configure-elytron.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-elytron.cli` file in the root of this quickstart directory. This script adds the configuration
that enables Elytron security for the quickstart components. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing 
${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-elytron.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-elytron.cli
    You should see the following result when you run the script:

        The batch executed successfully
5. Stop the ${product.name} server.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `application-security-domain` mapping was added to the `ejb3` subsystem:

        <application-security-domains>
            <application-security-domain name="quickstart-domain" security-domain="ApplicationDomain"/>
        </application-security-domains>
    The `application-security-domain` essentially enables Elytron security for the quickstart EJBs. It maps the `quickstart-domain` 
    that was set in the EJBs via annotation to the Elytron `ApplicationDomain` that will be responsible for authenticating
    and authorizing access to the EJBs.

2. The `http-connector` in the `remoting` subsystem was updated to use the `application-sasl-authentication` authentication factory: 
   
        <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ApplicationRealm" sasl-authentication-factory="application-sasl-authentication"/>
    This allows for the identity that was established in the connection authentication to be propagated to the components.

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

## Access the Application

Before you run the client, make sure you have already successfully deployed the EJBs to the server in the previous step and
that your command prompt is still in the same folder.

Type this command to execute the client:

        mvn exec:exec

## Investigate the Console Output

When you run the `mvn exec:exec` command, you see the following output. Note there may be other log messages interspersed between these.

    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    
    Called secured bean, caller principal quickstartUser
    
    Principal has admin permission: false
    
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    
    
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    
    Called secured bean, caller principal superUser
    
    Principal has admin permission: true
    
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
As expected, the `quickstart` user is able to call the methods available for `guest`s but does not have the `admin` permission
to call administrative methods on the remote EJB. The `superUser` on the other hand has permissions to call both methods.

## Undeploy the Archive

1. Make sure you have started the ${product.name} server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

## Restore the Server Configuration

You can restore the original server configuration by running the  `restore-configuration.cli` script provided in the root directory
of this quickstart or by manually restoring the back-up copy the configuration file.

### Restore the Server Configuration by Running the JBoss CLI Script

1. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
2. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name}
with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=restore-configuration.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=restore-configuration.cli
    This script reverts the changes made to the `ejb3` and `remoting` subsystems. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to [Create the Properties Files for the JAAS Security Domain](#create-the-properties-files-for-the-jaas-security-domain) as described above.
* Be sure to configure the server by running the JBoss CLI script as described above under [Configure the Server](#configure-the-server).
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
