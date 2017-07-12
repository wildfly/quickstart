# servlet-security:  Using Java EE Declarative Security to Control Servlet Access

Author: Sherif F. Makary, Pedro Igor  
Level: Intermediate  
Technologies: Servlet, Security  
Summary: The `servlet-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and Security in ${product.name}.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `servlet-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and Security in ${product.name.full}.

When you deploy this example, two users are automatically created for you: user `quickstartUser` with password `quickstartPwd1!` and user `guest` with password `guestPwd1!`. This data is located in the `src/main/resources/import.sql` file.

This quickstart takes the following steps to implement Servlet security:

1. Web Application:
	* Adds a security constraint to the Servlet using the `@ServletSecurity` and `@HttpConstraint` annotations.
	* Adds a security domain reference to `WEB-INF/jboss-web.xml`.
	* Adds a `login-config` that sets the `auth-method` to `BASIC` in the `WEB-INF/web.xml`.
2. Application Server (`standalone.xml`):
	* Defines a security domain in the `elytron` subsystem that uses the JDBC security realm to obtain the security data used to authenticate and authorize users.
	* Defines an `http-authentication-factory` in the `elytron` subsystem that uses the security domain created in step 1 for BASIC authentication.
	* Adds an `application-security-domain` mapping in the `undertow` subsystem to map the Servlet security domain to the HTTP authentication factory defined in step 2.
3. Database Configuration:
	* Adds an application user with access rights to the application.

        User Name: quickstartUser
        Password: quickstartPwd1!
        Role: quickstarts
	* Adds another user with no access rights to the application.

        User Name: guest
        Password: guestPwd1!
        Role: notauthorized

_Note: This quickstart uses the H2 database included with ${product.name.full} ${product.version}. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

## System Requirements

The application this project produces is designed to be run on ${product.name.full} ${product.version} or later.

All you need to build this project is ${build.requirements}. See [Configure Maven for ${product.name} ${product.version}](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


## Use of ${jboss.home.name}

In the following instructions, replace `${jboss.home.name}` with the actual path to your ${product.name} installation. The installation path is described in detail here: [Use of ${jboss.home.name} and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_${jboss.home.name}.md#use-of-eap_home-and-jboss_home-variables).


## Configure the Server

You can configure the server by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-server.cli` script provided in the root directory of this quickstart.

1. Before you begin, back up your server configuration file
    * If it is running, stop the ${product.name} server.
    * Backup the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the ${product.name} server by typing the following:

        For Linux:  ${jboss.home.name}/bin/standalone.sh
        For Windows:  ${jboss.home.name}\bin\standalone.bat
3. Review the `configure-server.cli` file in the root of this quickstart directory. This script adds security domain and HTTP authentication factory to the `elytron` subsystem in the server configuration and also configures the `undertow` subsystem to use the configured HTTP authentication factory for the Web application.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing ${jboss.home.name} with the path to your server:

        For Linux: ${jboss.home.name}/bin/jboss-cli.sh --connect --file=configure-server.cli
        For Windows: ${jboss.home.name}\bin\jboss-cli.bat --connect --file=configure-server.cli
You should see the following result when you run the script:

        The batch executed successfully
5. Stop the ${product.name} server.


## Review the Modified Server Configuration

After stopping the server, open the `${jboss.home.name}/standalone/configuration/standalone.xml` file and review the changes.

1. The following datasource was added to the `datasources` subsystem.

        <datasource jndi-name="java:jboss/datasources/ServletSecurityDS" pool-name="ServletSecurityDS">
            <connection-url>jdbc:h2:mem:servlet-security;DB_CLOSE_ON_EXIT=FALSE</connection-url>
            <driver>h2</driver>
            <security>
                <user-name>sa</user-name>
                <password>sa</password>
            </security>
        </datasource>

2. The following `security-realm` was added to the `elytron` subsystem.

        <jdbc-realm name="servlet-security-jdbc-realm">
            <principal-query sql="SELECT PASSWORD FROM USERS WHERE USERNAME = ?" data-source="ServletSecurityDS">
                <clear-password-mapper password-index="1"/>
            </principal-query>
            <principal-query sql="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?" data-source="ServletSecurityDS">
                <attribute-mapping>
                    <attribute to="roles" index="1"/>
                </attribute-mapping>
            </principal-query>
        </jdbc-realm>
The `security-realm` is responsible for verifying the credentials for a given principal and for obtaining security attributes (like roles) that are associated with the authenticated identity.

3. The following `role-decoder` was added to the `elytron` subsystem.

        <simple-role-decoder name="from-roles-attribute" attribute="roles"/>
The `jdbc-realm` in this quickstart stores the roles associated with a principal in an attribute named roles. Other realms might use different attributes for roles (such as `group`). The purpose of a `role-decoder` is to instruct the security domain how roles are to be retrieved from an authorized identity.

4. The following `security-domain` was added to the `elytron` subsystem.

        <security-domain name="servlet-security-quickstart-sd" default-realm="servlet-security-jdbc-realm" permission-mapper="default-permission-mapper">
            <realm name="servlet-security-jdbc-realm" role-decoder="from-roles-attribute"/>
        </security-domain>

5. The following `http-authentication-factory` was added to the `elytron` subsystem.

        <http-authentication-factory name="servlet-security-quickstart-http-auth" http-server-mechanism-factory="global" security-domain="servlet-security-quickstart-sd">
            <mechanism-configuration>
                <mechanism mechanism-name="BASIC">
                    <mechanism-realm realm-name="RealmUsersRoles"/>
                </mechanism>
            </mechanism-configuration>
        </http-authentication-factory>
It basically defines an HTTP authentication factory for the BASIC mechanism that relies on the `servlet-security-quickstart-sd` security domain to authenticate and authorize access to Web applications.

6. The following `application-security-domain` was added to the `undertow` subsystem.

        <application-security-domains>
            <application-security-domain name="servlet-security-quickstart" http-authentication-factory="servlet-security-quickstart-http-auth"/>
        </application-security-domains>
This configuration tells `Undertow` that applications with the `servlet-security-quickstart` security domain (as defined in `jboss-web.xml` or via `@SecurityDomain` annotation in the Servlet class) should use the `http-authentication-factory` named `servlet-security-quickstart-http-auth`. If no `application-security-domain` is defined for a particular security domain, `Undertow` assumes the legacy JAAS based security domains should be used for authentication/authorization (and in this case the security domain defined in the Web application must match a security domai in the legacy `security` subsystem. The presence of an `application-security-domain` configuration is what enables Elytron authentication for a Web application.

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

When you access the application, you should get a browser login challenge.

Log in using the username `quickstartUser` and password `quickstartPwd1!`. The browser will display the following security info:

    Successfully called Secured Servlet

    Principal : quickstartUser
    Remote User : quickstartUser
    Authentication Type : BASIC

Now close the browser. Open a new browser and log in with username `guest` and password `guestPwd1!`. The browser will display the following error:

    Forbidden


## Server Log: Expected Warnings and Errors

_Note:_ You will see the following warning in the server log. You can ignore it.

    HHH000431: Unable to determine H2 database version, certain features may not work


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
This script removes the `application-security-domain` configuration from the `undertow` subsystem, the `http-authentication-factory`, `security-domain`, `security-realm` and `role-decoder` configuration from the `elytron` subsystem and it also removes the `datasource` used for this quickstart. You should see the following result when you run the script:

        The batch executed successfully
        process-state: reload-required

### Restore the Server Configuration Manually
1. If it is running, stop the ${product.name} server.
2. Replace the `${jboss.home.name}/standalone/configuration/standalone.xml` file with the back-up copy of the file.


## Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse

You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a ${product.name} server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](${use.eclipse.url}).

* Be sure to configure the server by running the JBoss CLI commands as described above under [Configure the ${product.name} Server](#configure-the-server). Stop the server at the end of that step.
* Be sure to [Restore the Server Configuration](#restore-the-server-configuration) when you have completed testing this quickstart.

## Debug the Application

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
