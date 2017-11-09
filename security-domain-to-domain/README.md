# security-domain-to-domain:  Identity propagation to an EJB using different security domains

Author: Darran Lofthouse  
Level: Advanced  
Technologies: Servlet, EJB, Security  
Summary: The `security-domain-to-domain` quickstart demonstrates the propagation of an identity across two different deployments using different security domains.  
Target Product: ${product.name}  
Source: <${github.repo.url}>  

## What is it?

The `security-domain-to-domain` quickstart demonstrates the propagation of an identity across two different deployments using different security domains.

When you deploy this example, one user is automatically created for you: user `quickstartUser` with password `quickstartPwd1!` This data is located in the `web/src/main/resources/import.sql` file.

This quickstart takes the following steps to implement Servlet security:

1. Web Application:
    * Adds a security constraint to the Servlet using the `@ServletSecurity` and `@HttpConstraint` annotations.
    * Adds a security domain reference to `WEB-INF/jboss-web.xml`.
    * Adds a `login-config` that sets the `auth-method` to `BASIC` in the `WEB-INF/web.xml`.
2. EJB Application
    * Adds a security domain reference using the @org.jboss.ejb3.annotation.SecurityDomain annotation.
2. Application Server (`standalone.xml`):
    * Defines a security domain in the `elytron` subsystem that uses the JDBC security realm to obtain the security data used to authenticate and authorize users.
    * Defined a second security domain in the `elytron` subsystem similar to the first but with different role mappings.
    * Defines an `http-authentication-factory` in the `elytron` subsystem that uses the security domain created in step 1 for BASIC authentication.
    * Adds an `application-security-domain` mapping in the `undertow` subsystem to map the Servlet security domain to the HTTP authentication factory defined in step 3.
    * Adds an `application-security-domain` mapping in the `ejb3` subystem to map the EJBs security domain to the security domain defined in step 2.
3. Database Configuration:
    * Adds an application user with access rights to the application.

            User Name: quickstartUser
            Password: quickstartPwd1!
            
When used with the `entry-domain` this will have the role `Users`, when used with the `business-domain` this will have the role `Manager`.

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
    * Back up the file: `${jboss.home.name}/standalone/configuration/standalone.xml`
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

        <datasource jndi-name="java:jboss/datasources/SecurityDomainToDomainDS" pool-name="SecurityDomainToDomainDS">
            <connection-url>jdbc:h2:mem:servlet-security;DB_CLOSE_ON_EXIT=FALSE</connection-url>
            <driver>h2</driver>
            <security>
                <user-name>sa</user-name>
                <password>sa</password>
            </security>
        </datasource>

2. The following security realms were added to the `elytron` subsystem.

        <jdbc-realm name="entry-realm">
            <principal-query sql="SELECT PASSWORD FROM USERS WHERE USERNAME = ?" data-source="SecurityDomainToDomainDS">
                <clear-password-mapper password-index="1"/>
            </principal-query>
            <principal-query sql="SELECT R.NAME, 'Roles' FROM ENTRY_ROLES ER INNER JOIN ROLES R ON R.ID = ER.ROLE_ID INNER JOIN USERS U ON U.ID = ER.USER_ID WHERE U.USERNAME = ?" data-source="SecurityDomainToDomainDS">
                <attribute-mapping>
                    <attribute to="roles" index="1"/>
                </attribute-mapping>
            </principal-query>
        </jdbc-realm>

      The `entry-realm` security realm is responsible for verifying the credentials for a given principal and for obtaining security attributes (like roles) that are associated with the authenticated identity.

        <jdbc-realm name="business-realm">
            <principal-query sql="SELECT PASSWORD FROM USERS WHERE USERNAME = ?" data-source="SecurityDomainToDomainDS">
                <clear-password-mapper password-index="1"/>
            </principal-query>
            <principal-query sql="SELECT R.NAME, 'Roles' FROM BUSINESS_ROLES BR INNER JOIN ROLES R ON R.ID = BR.ROLE_ID INNER JOIN USERS U ON U.ID = BR.USER_ID WHERE U.USERNAME = ?" data-source="SecurityDomainToDomainDS">
                <attribute-mapping>
                    <attribute to="roles" index="1"/>
                </attribute-mapping>
            </principal-query>
        </jdbc-realm>

      The `business-realm` security realm is just used for loading the identity as it accesses the EJB.


3. The following `role-decoder` was added to the `elytron` subsystem.

        <simple-role-decoder name="from-roles-attribute" attribute="roles"/>

    The realms in this quickstart store the roles associated with a principal in an attribute named roles. Other realms might use different attributes for roles (such as `group`). The purpose of a `role-decoder` is to instruct the security domain how roles are to be retrieved from an authorized identity.

4. The following security domains were added to the `elytron` subsystem.

        <security-domain name="entry-security-domain" default-realm="entry-realm" permission-mapper="default-permission-mapper" outflow-security-domains="business-security-domain">
            <realm name="entry-realm" role-decoder="from-roles-attribute"/>
        </security-domain>

        <security-domain name="business-security-domain" default-realm="business-realm" trusted-security-domains="entry-security-domain">
            <realm name="business-realm" role-decoder="from-roles-attribute"/>
        </security-domain>
        
        The `entry-security-domain` is configured to automatically outflow any identities to the `business-security-domain` and in return the `business-security-domain` is configured to trust any identities coming from the `entry-security-domain`. 

5. The following `http-authentication-factory` was added to the `elytron` subsystem.

        <http-authentication-factory name="security-domain-to-domain-http" security-domain="entry-security-domain" http-server-mechanism-factory="global">
            <mechanism-configuration>
                <mechanism mechanism-name="BASIC">
                    <mechanism-realm realm-name="RealmUsersRoles"/>
                </mechanism>
            </mechanism-configuration>
        </http-authentication-factory>

    It basically defines an HTTP authentication factory for the BASIC mechanism that relies on the `entry-security-domain` security domain to authenticate and authorize access to Web applications.

6. The following `application-security-domain` was added to the `undertow` subsystem.

        <application-security-domains>
            <application-security-domain name="EntryDomain" http-authentication-factory="security-domain-to-domain-http"/>
        </application-security-domains>

This configuration tells `Undertow` that applications with the `EntryDomain` security domain, as defined in the `jboss-web.xml` or by using the `@SecurityDomain` annotation in the Servlet class, should use the `http-authentication-factory` named `security-domain-to-domain-http`.

7) The following `application-security-domain` was added to the `ejb3` subsystem.

        <application-security-domains>
            <application-security-domain name="BusinessDomain" security-domain="business-security-domain"/>
        </application-security-domains>

This configuration tells `EJB3` that applications with the `BusinessDomain` security domain, as defined in the `jboss.xml` or by using the `@SecurityDomain` annotation in the EJB class, should use the `security-domain` named `business-security-domain`.

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
    Identity as visible to servlet.

    Principal : quickstartUser

    Remote User : quickstartUser

    Authentication Type : BASIC

    Caller Has Role 'User'=true

    Caller Has Role 'Manager'=false
    Identity as visible to EJB.

    Principal : quickstartUser

    Caller Has Role 'User'=false

    Caller Has Role 'Manager'=true

This shows that the user `quickstartUser` calls the servlet and has role `User` but does not have the role `Manager`, as the call reaches the EJB the principal is still `quickstartUser` but now the identity does not have the role `User` and instead has the role `Manager`. 

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
This script removes the `application-security-domain` configurations from the `ejb3` and `undertow` subsystem, the `http-authentication-factory`, `security-domain`, `security-realm` and `role-decoder` configuration from the `elytron` subsystem and it also removes the `datasource` used for this quickstart. You should see the following result when you run the script:

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

