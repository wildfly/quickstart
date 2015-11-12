servlet-security:  Using Java EE Declarative Security to Control Servlet Access
====================
Author: Sherif F. Makary, Pedro Igor  
Level: Intermediate  
Technologies: Servlet, Security  
Summary: The `servlet-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and Security in WildFly.  
Target Product: WildFly  
Source: <https://github.com/wildfly/quickstart/>  

What is it?
-----------

The `servlet-security` quickstart demonstrates the use of Java EE declarative security to control access to Servlets and Security in Red Hat JBoss Enterprise Application Platform.

When you deploy this example, two users are automatically created for you: user `quickstartUser` with password `quickstartPwd1!` and user `guest` with password `guestPwd1!`. This data is located in the `src/main/resources/import.sql` file. 

This quickstart takes the following steps to implement Servlet security:

1. Defines a security domain in the `standalone.xml` configuration file using the Database JAAS LoginModule.
2. Adds an application user with access rights to the application.

        User Name: quickstartUser
        Password: quickstartPwd1!
        Role: quickstarts
3. Adds another user with no access rights to the application.

        User Name: guest
        Password: guestPwd1!
        Role: notauthorized
4. Adds a security domain reference to `WEB-INF/jboss-web.xml`.
5. Adds a security constraint to the `WEB-INF/web.xml` .
6. Adds a security annotation to the EJB declaration.

Please note the allowed user role `quickstarts` in the annotation `@RolesAllowed` is the same as the user role defined in step 2.

_Note: This quickstart uses the H2 database included with Red Hat JBoss Enterprise Application Platform 7. It is a lightweight, relational example datasource that is used for examples only. It is not robust or scalable, is not supported, and should NOT be used in a production environment!_

_Note: This quickstart uses a `*-ds.xml` datasource configuration file for convenience and ease of database configuration. These files are deprecated in WildFly and should not be used in a production environment. Instead, you should configure the datasource using the Management CLI or Management Console. Datasource configuration is documented in the [Administration and Configuration Guide](https://access.redhat.com/documentation/en-US/JBoss_Enterprise_Application_Platform/) for Red Hat JBoss Enterprise Application Platform._

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later. See [Configure Maven for WildFly 10](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN_JBOSS_EAP7.md#configure-maven-to-build-and-deploy-the-quickstarts) to make sure you are configured correctly for testing the quickstarts.


Use of WILDFLY_HOME
---------------

In the following instructions, replace `WILDFLY_HOME` with the actual path to your WildFly installation. The installation path is described in detail here: [Use of WILDFLY_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).


Configure the WildFly Server
---------------

This quickstart authenticates users using a simple database setup. The datasource configuration is located in the `/src/main/webapp/WEB-INF/servlet-security-quickstart-ds.xml` file. You must define a security domain using the database JAAS login module. 

You can configure the security domain by running JBoss CLI commands. For your convenience, this quickstart batches the commands into a `configure-security-domain.cli` script provided in the root directory of this quickstart. 

1. Before you begin, back up your server configuration file
    * If it is running, stop the WildFly server.
    * Backup the file: `WILDFLY_HOME/standalone/configuration/standalone.xml`
    * After you have completed testing this quickstart, you can replace this file to restore the server to its original configuration.

2. Start the WildFly server by typing the following: 

        For Linux:  WILDFLY_HOME/bin/standalone.sh 
        For Windows:  WILDFLY_HOME\bin\standalone.bat
3. Review the `configure-security-domain.cli` file in the root of this quickstart directory. This script adds the `servlet-security-quickstart` security domain to the `security` subsystem in the server configuration and configures authentication access.

4. Open a new command prompt, navigate to the root directory of this quickstart, and run the following command, replacing WILDFLY_HOME with the path to your server:

        For Linux: WILDFLY_HOME/bin/jboss-cli.sh --connect --file=configure-security-domain.cli
        For Windows: WILDFLY_HOME\bin\jboss-cli.bat --connect --file=configure-security-domain.cli
You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}
5. Stop the WildFly server.


Review the Modified Server Configuration
-----------------------------------

After stopping the server, open the `WILDFLY_HOME/standalone/configuration/standalone.xml` file and review the changes.

The following `servlet-security-quickstart` security-domain element was added to the `security` subsystem.

      	<security-domain name="servlet-security-quickstart" cache-type="default">
    	      <authentication>
          	    <login-module code="Database" flag="required">
            	      <module-option name="dsJndiName" value="java:jboss/datasources/ServletSecurityDS"/>
                    <module-option name="principalsQuery" value="SELECT PASSWORD FROM USERS WHERE USERNAME = ?"/>
                    <module-option name="rolesQuery" value="SELECT R.NAME, 'Roles' FROM USERS_ROLES UR INNER JOIN ROLES R ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON U.ID = UR.USER_ID WHERE U.USERNAME = ?"/>
                </login-module>
            </authentication>
        </security-domain>
    
Please note that the security domain name `servlet-security-quickstart` must match the one defined in the `/src/main/webapp/WEB-INF/jboss-web.xml` file.


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

4. This will deploy `target/wildfly-servlet-security.war` to the running instance of the server.

Access the Application 
---------------------

The application will be running at the following URL <http://localhost:8080/jboss-servlet-security/>.

When you access the application, you should get a browser login challenge. 

Log in using the username `quickstartUser` and password `quickstartPwd1!`. The browser will display the following security info:

    Successfully called Secured Servlet

    Principal : quickstartUser
    Remote User : quickstartUser
    Authentication Type : BASIC

Now close the browser. Open a new browser and log in with username `guest` and password `guestPwd1!`. The browser will display the following error:

    HTTP Status 403 - Access to the requested resource has been denied

    type Status report
    message Access to the requested resource has been denied
    description Access to the specified resource (Access to the requested resource has been denied) has been forbidden.


Server Log: Expected warnings and errors
-----------------------------------

_Note:_ You will see the following warnings in the server log. You can ignore these warnings.

    WFLYJCA0091: -ds.xml file deployments are deprecated. Support may be removed in a future version.

    HHH000431: Unable to determine H2 database version, certain features may not work


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
This script removes the `servlet-security-quickstart` security domain from the `security` subsystem in the server configuration. You should see the following result when you run the script:

        The batch executed successfully.
        {"outcome" => "success"}


### Remove the Security Domain Configuration Manually
1. If it is running, stop the WildFly server.
2. Replace the `WILDFLY_HOME/standalone/configuration/standalone.xml` file with the back-up copy of the file.




Run the Quickstart in Red Hat JBoss Developer Studio or Eclipse
-------------------------------------
You can also start the server and deploy the quickstarts or run the Arquillian tests from Eclipse using JBoss tools. For general information about how to import a quickstart, add a WildFly server, and build and deploy a quickstart, see [Use JBoss Developer Studio or Eclipse to Run the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_JBDS.md#use-jboss-developer-studio-or-eclipse-to-run-the-quickstarts) 

_NOTE:_ Be sure to configure the security domain by running the JBoss CLI commands as described in the section above entitled *Configure the WildFly Server*. Stop the server at the end of that step.


Debug the Application
------------------------------------

If you want to debug the source code of any library in the project, run the following command to pull the source into your local repository. The IDE should then detect it.

      mvn dependency:sources
     
