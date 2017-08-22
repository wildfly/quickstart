# ejb-security-jaas: Using the legacy JAAS security domains to secure JEE applications

Author: Stefan Guilhen  
Level: Intermediate  
Technologies: EJB, Security  
Summary: The `ejb-security-jaas` quickstart demonstrates how legacy `JAAS` security domains can be used in conjunction with `Elytron`
to secure JEE applications.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `ejb-security-jaas` quickstart demonstrates how legacy `JAAS`-based security domains can be used in conjunction with `WildFly Elytron` to secure JEE applications. The secured EJB component can be accessed indirectly using a web application and it can also be directly invoked by a remote client. This quickstart shows how ${product.name.full}  must be configured to support both scenarios using the legacy `JAAS` integration.

The overall steps required to use the `JAAS` integration are as follows:
1. Specify a `JAAS` security domain in the legacy `security` subsystem.
2. Export an `Elytron`-compatible security realm that delegates to the legacy JAAS security domain.
3. Create a `security-domain` in the `elytron` subsystem that uses the exported realm.
4. Setup an `http-authentication-factory` in the `elytron` subsystem to handle the web requests.
5. Setup a `sasl-authentication-factory` in the `elytron` subsystem to handle the requests made by remote clients.
6. Add the `application-security-domain` mappings to both `ejb3` and `undertow` subsystems to enable `Elytron` security for the EJB3 and web components.

## System Requirements

The applications these projects produce are designed to be run on ${product.name.full} ${product.version} or later.

All you need to build these projects is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.

To run these quickstarts with the provided build scripts, you need the ${product.name} distribution ZIP. For information on  how to install and run JBoss, see the [${product.name.full} Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-enterprise-application-platform/) _Getting Started Guide_ located on the Customer Portal.

You can also use [JBoss Developer Studio or Eclipse](#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) to run the quickstarts.

## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).

## Create the Properties Files for the JAAS Security Domain

1. Open a command line and navigate to the ${product.name} server `configuration` directory:

        For Linux:   ${jboss.home.name}/standalone/configuration
        For Windows: ${jboss.home.name}\standalone\configuration
2. Create a file named `users.properties` and add the following username/password pair:

        quickstartUser=quickstartPwd1!
3. Create a file named `roles.properties` and add the following username/roles pair:

        quickstartUser=guest
    This concludes the configuration required by the legacy `JAAS` login module used in this quickstart.
## Configure the Server

These steps assume you are running the server in standalone mode and using the default `standalone.xml` supplied with the distribution.

You configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-elytron-jaas.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Back up the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-elytron-jaas.cli` file in the root of this quickstart directory. This script adds the configuration that enables Elytron security for the quickstart components. Comments in the script describe the purpose of each block of commands.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-elytron-jaas.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-elytron-jaas.cli
    You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required
5. Stop the ${product.name} server.

## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following `security-domain` was added to the legacy `security` subsystem:

        <security-domain name="quickstart-domain" cache-type="default">
            <authentication>
                <login-module code="Remoting" flag="optional">
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
                <login-module code="UsersRoles" flag="required">
                    <module-option name="usersProperties" value="../standalone/configuration/users.properties"/>
                    <module-option name="rolesProperties" value="../standalone/configuration/roles.properties"/>
                    <module-option name="password-stacking" value="useFirstPass"/>
                </login-module>
            </authentication>
            <mapping>
                <mapping-module code="SimpleRoles" type="role">
                    <module-option name="quickstartUser" value="admin"/>
                </mapping-module>
            </mapping>
        </security-domain>

    The `quickstart-domain`is used to authenticate and authorize users. The `Remoting` login module is added to properly authenticate requests made from remote clients. A `mapping-module` is added that can be used to provide an extra role (`admin`). It is used later on to show how the legacy role mappers can be enabled and disabled.

2. The following `elytron-realm` was added to the legacy `security` subsystem:

        <elytron-integration>
            <security-realms>
                <elytron-realm name="LegacyRealm" legacy-jaas-config="quickstart-domain" apply-role-mappers="false"/>
            </security-realms>
        </elytron-integration>

    This block tells the `security` subsystem to export an `Elytron`-compatible realm called `LegacyRealm` that will delegate authentication and authorization decisions to the legacy `quickstart-domain`. Setting the `apply-role-mappers` attribute to `false` indicates to the exported realm that it should not use any role mappers defined in the legacy security domain.

3. The following `security-domain` was added to the `elytron` subsystem:

        <security-domain name="LegacyDomain" default-realm="LegacyRealm" permission-mapper="default-permission-mapper" security-event-listener="local-audit">
            <realm name="LegacyRealm"/>
        </security-domain>

4. The following `http-authentication-factory` was added to the `elytron` subsystem:

        <http-authentication-factory name="quickstart-http-authentication" http-server-mechanism-factory="global" security-domain="LegacyDomain">
            <mechanism-configuration>
                <mechanism mechanism-name="BASIC">
                    <mechanism-realm realm-name="Legacy Realm"/>
                </mechanism>
            </mechanism-configuration>
        </http-authentication-factory>
    It creates the HTTP authentication factory that will handle BASIC requests by delegating the security domain created in step 3.

5. The following `application-security-domain` mapping was added to the `undertow` subsystem:

        <application-security-domains>
            <application-security-domain name="legacy-domain" http-authentication-factory="quickstart-http-authentication"/>
        </application-security-domains>
    It tells `Undertow` to use the HTTP authentication factory created in step 4 for web applications that specify the security domain `legacy-domain` in their metadata. The quickstart application specifies this domain both for the web layer, in the `jboss-web.xml` file, and the EJB component, using annotation in the code.

6. The following `sasl-authentication-factory` was added to the `elytron` subsystem:

        <sasl-authentication-factory name="quickstart-sasl-authentication" sasl-server-factory="configured" security-domain="LegacyDomain">
            <mechanism-configuration>
                <mechanism mechanism-name="PLAIN"/>
            </mechanism-configuration>
        </sasl-authentication-factory>

7. The `http-remoting-connector` in the `remoting` subsystem was updated to use the `sasl-authentication-factory` created in step 6:

        <http-connector name="http-remoting-connector" connector-ref="default" security-realm="ApplicationRealm" sasl-authentication-factory="quickstart-sasl-authentication"/>
    Authentication performed by the quickstart remote client is handled by this SASL authentication factory.

8. Finally, the following `application-security-domain` mapping was added to the `ejb3` subsystem:

        <application-security-domains>
            <application-security-domain name="legacy-domain" security-domain="LegacyDomain"/>
        </application-security-domains>
    This mapping basically enables `Elytron` security for EJB3 applications that specify the security domain `legacy-domain`
    in their metadata (either via jboss-ejb3.xml or annotations). The quickstart application uses the `@SecurityDomain`
    annotation in the bean class to specify this security domain.

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
        Has admin permission : false
        Authentication Type : BASIC

3. The application can also be accessed directly by a remote client. Type the following command in the root directory of the quickstart:

        mvn exec:exec
    The remote client application runs and displays the results of calling the secured bean:

            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


            Called secured bean, caller principal quickstartUser

            Principal has admin permission: false


            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

4. Next, change the exported realm so that it now uses the legacy role mappers as defined in the legacy `JAAS` security domain.

    Make sure you are still in the root directory of this quickstart, and run the following command, replacing ${jboss.home.name}
    with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=enable-role-mappers.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=enable-role-mappers.cli
    You should see the following result when you run the script:
        {
            "outcome" => "success",
            "response-headers" => {
                "operation-requires-reload" => true,
                "process-state" => "reload-required"
            }
        }

5. If you didn't close your web browser, re-load the quickstart application page. Otherwise open a new browser, point it to the
URL <http://localhost:8080/${project.artifactId}/> and login with `quickstartUser/quickstartPwd1!`. It should now display a
page confirming the user now has the `admin` role that was provided by the legacy role mapper:

        Successfully called Secured EJB

        Principal : quickstartUser
        Remote User : quickstartUser
        Has admin permission : true
        Authentication Type : BASIC

6. The same result can be observed when re-running the remote client application:

            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *


            Called secured bean, caller principal quickstartUser

            Principal has admin permission: true


            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

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
    This script reverts the changes made to the `ejb3`, `elytron`, `security` and `undertow` subsystems. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually

1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the backup copy of the file.

## Remove the Properties Files from the Server

After you are done with this quickstart, remember to remove the `users.properties` and `roles.properties` files from the
server configuration directory (`${jboss.home.name}/standalone/configuration/`).

## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to [Create the Properties Files for the JAAS Security Domain](#create-the-properties-files-for-the-jaas-security-domain) as described above.
* Be sure to configure the server by running the JBoss CLI script as described above under [Configure the Server](#configure-the-server). Stop the server at the end of that step.
* To deploy the application to the ${product.name} server, right-click on the `${project.artifactId}` project and choose `Run As` --> `Run on Server`.
* You are presented with a browser login challenge. Enter the credentials as described above under [Access the Application](#access-the-application) to see the running application. Note that `Has admin permission` is `false`.
* Leave the application running in JBoss Developer Studio. To configure the server to use the legacy role mappers, open a terminal, and run the `enable-role-mappers.cli` script as described above under [Access the Application](#access-the-application).
* Go back to JBoss Developer Studio and click `Refresh the current page`. Note that `Has admin permission` is now `true`.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

    mvn dependency:sources
